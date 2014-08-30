EXAMPLE_DIR = /user/$(USER)/hist/
INPUT_DIR   = $(EXAMPLE_DIR)/input
OUTPUT_DIR  = $(EXAMPLE_DIR)/output
OUTPUT_FILE = $(OUTPUT_DIR)/part-00000
PARAMS_FILE = $(EXAMPLE_DIR)/params

TOOLLIBS_DIR=$(HADOOP_PREFIX)/share/hadoop/tools/lib/
NITERS=25

run: inputs
	hdfs dfs -rm -f -r $(OUTPUT_DIR) 
	hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
		-files ./mmm-map.py,./mmm-combine.py,./mmm-reduce.py \
		-mapper ./mmm-map.py  -combiner ./mmm-combine.py \
	        -reducer ./mmm-reduce.py \
		-input $(INPUT_DIR) \
		-output  $(OUTPUT_DIR)
	hdfs dfs -cat $(OUTPUT_FILE) > mmm
	cat mmm 
	hdfs dfs -rm -f -r $(OUTPUT_DIR) 
	hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
		-files ./hist-map.py,./hist-combine-reduce.py,./nbins,./mmm \
		-mapper ./hist-map.py  -combiner ./hist-combine-reduce.py \
	        -reducer ./hist-combine-reduce.py \
		-input $(INPUT_DIR) \
		-output  $(OUTPUT_DIR) 
	hdfs dfs -cat $(OUTPUT_FILE) | sort -n > hist
	cat hist

run-test: input/small-data.dat
	cat input/small-data.dat | ./mmm-map.py  | ./mmm-combine.py \
		| sort |  ./mmm-reduce.py  > mmm
	cat input/small-data.dat | ./hist-map.py  | \
		./hist-combine-reduce.py | sort -n | ./hist-combine-reduce.py

directories:
	hdfs dfs -test -e $(EXAMPLE_DIR) || hdfs dfs -mkdir $(EXAMPLE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -mkdir $(INPUT_DIR)
	hdfs dfs -test -e $(OUTPUT_DIR) || hdfs dfs -mkdir $(OUTPUT_DIR)

input/data.dat: data.py
	./data.py --npts=1000000 > $@

input/small-data.dat: data.py
	./data.py --npts=5000 > $@

inputs: directories input/data.dat 
	hdfs dfs -test -e $(INPUT_DIR)/data.dat \
	  || hdfs dfs -put input/data.dat $(INPUT_DIR)/data.dat

clean:
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	-hdfs dfs -rm -f -r $(EXAMPLE_DIR)
	-rm -f mmm hist
	-rm -f input/*.dat

.PHONY: directories inputs clean run run-test
