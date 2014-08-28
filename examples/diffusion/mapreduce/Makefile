BASE_DIR   = /user/$(USER)/diffuse
INPUT_DIR   = $(BASE_DIR)/timestep-0
INTERMEDIATE_DIR = $(BASE_DIR)/intermediate
OUTPUT_DIR  = $(BASE_DIR)/timestep-1
OUTPUT_FILE = $(OUTPUT_DIR)/part-0000
TMPDIR1 = $(BASE_DIR)/timestep-tmp1
TMPDIR2 = $(BASE_DIR)/timestep-tmp2

TOOLLIBS_DIR=$(HADOOP_PREFIX)/share/hadoop/tools/lib/

run: inputs
	hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
		-files ./map.py,./reduce.py \
		-mapper ./map.py \
		-reducer ./reduce.py \
		-input $(INPUT_DIR) \
		-output $(TMPDIR1)
	for step in `seq 5`; do \
		hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
			-files ./map.py,./reduce.py \
			-mapper ./map.py \
			-reducer ./reduce.py \
			-input $(TMPDIR1) \
			-output $(TMPDIR2); \
		hdfs dfs -rm -f -r $(BASE_DIR)/timestep-tmp1; \
		hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
			-files ./map.py,./reduce.py \
			-mapper ./map.py \
			-reducer ./reduce.py \
			-input $(TMPDIR2) \
			-output $(TMPDIR1); \
		hdfs dfs -rm -f -r $(BASE_DIR)/timestep-tmp2; done
	hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
		-files ./map.py,./reduce.py \
		-mapper ./map.py \
		-reducer ./reduce.py \
		-input $(TMPDIR1) \
		-output  $(OUTPUT_DIR) 
	hdfs dfs -cat diffuse/timestep-0/part-00000 | awk 'BEGIN {FS=":"} {printf $$2 " "} END {print ""}' > data
	hdfs dfs -cat diffuse/timestep-1/part-00000 | awk 'BEGIN {FS=":"}; {printf $$2 " "} END {print ""}' >> data
	gnuplot plot.gp
	
inputs:
	hdfs dfs -test -e $(BASE_DIR) || hdfs dfs -mkdir $(BASE_DIR)
	hdfs dfs -put timestep-0 $(BASE_DIR)

clean:
	-rm -f data
	-hdfs dfs -rm -f -r $(BASE_DIR)

.PHONY: clean run
