EXAMPLE_DIR = /user/$(USER)/wordcount-stream/
INPUT_DIR   = $(EXAMPLE_DIR)/input
OUTPUT_DIR  = $(EXAMPLE_DIR)/output
OUTPUT_FILE = $(OUTPUT_DIR)/part-00000
OUTPUT_FILE_2 = $(OUTPUT_DIR)/part-00001

TOOLLIBS_DIR=$(HADOOP_PREFIX)/share/hadoop/tools/lib/

run: inputs
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
		-files ./map.py,./reduce.py \
		-mapper ./map.py \
		-reducer ./reduce.py \
		-input $(INPUT_DIR) \
		-output  $(OUTPUT_DIR) 
	hdfs dfs -cat $(OUTPUT_FILE)

run-2reducers: inputs
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
                -D mapred.reduce.tasks=2 \
		-files ./map.py,./reduce.py \
		-mapper ./map.py \
		-reducer ./reduce.py \
		-input $(INPUT_DIR) \
		-output  $(OUTPUT_DIR) 
	echo "===$(OUTPUT_FILE)==="
	hdfs dfs -cat $(OUTPUT_FILE)
	echo "===$(OUTPUT_FILE_2)==="
	hdfs dfs -cat $(OUTPUT_FILE_2)

directories:
	hdfs dfs -test -e $(EXAMPLE_DIR) || hdfs dfs -mkdir $(EXAMPLE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -mkdir $(INPUT_DIR)
	hdfs dfs -test -e $(OUTPUT_DIR) || hdfs dfs -mkdir $(OUTPUT_DIR)

inputs: directories
	hdfs dfs -test -e $(INPUT_DIR)/file01 \
	  || hdfs dfs -put ../input-small/file01 $(INPUT_DIR)/file01
	hdfs dfs -test -e $(INPUT_DIR)/file02 \
	  || hdfs dfs -put ../input-small/file02 $(INPUT_DIR)/file02

clean:
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	-hdfs dfs -rm -r -f $(EXAMPLE_DIR)

.PHONY: directories inputs clean run run-2reducers
