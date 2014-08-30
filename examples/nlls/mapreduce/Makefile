EXAMPLE_DIR = /user/$(USER)/nlls/
INPUT_DIR   = $(EXAMPLE_DIR)/input
OUTPUT_DIR  = $(EXAMPLE_DIR)/output
OUTPUT_FILE = $(OUTPUT_DIR)/part-00000
PARAMS_FILE = $(EXAMPLE_DIR)/params

TOOLLIBS_DIR=$(HADOOP_PREFIX)/share/hadoop/tools/lib/
NITERS=25

run: inputs
	cp orig.params params
	for iter in `seq $(NITERS)`; do \
	    hdfs dfs -rm -f -r $(OUTPUT_DIR) ;\
  	    hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
		-files ./map.py,./combine.py,./reduce.py,./params\
		-mapper ./map.py  -combiner ./combine.py  -reducer ./reduce.py \
		-input $(INPUT_DIR) \
		-output  $(OUTPUT_DIR) ; \
	    hdfs dfs -cat $(OUTPUT_FILE) > params; \
	    cat params ;\
	done

run-test: ../input/small-data.dat
	cp orig.params params
	cat params
	for iter in `seq 40`; do \
		cat ../input/small-data.dat | ./map.py  | ./combine.py | ./reduce.py  > params.out ; \
		mv -f params.out params ; \
		cat params ; \
	done

directories:
	hdfs dfs -test -e $(EXAMPLE_DIR) || hdfs dfs -mkdir $(EXAMPLE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -mkdir $(INPUT_DIR)
	hdfs dfs -test -e $(OUTPUT_DIR) || hdfs dfs -mkdir $(OUTPUT_DIR)

../input/data.dat: ../data.py
	../data.py --npts=1000000 > $@

../input/small-data.dat: ../data.py
	../data.py --npts=5000 > $@

inputs: directories ../input/data.dat 
	hdfs dfs -test -e $(INPUT_DIR)/data.dat \
	  || hdfs dfs -put ../input/data.dat $(INPUT_DIR)/data.dat

clean:
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	-hdfs dfs -rm -f -r $(EXAMPLE_DIR)
	-rm -f params
	-rm -f ../input/small-data.dat
	-rm -f ../input/data.dat
	-rm -f params.out

.PHONY: directories inputs clean run run-test
