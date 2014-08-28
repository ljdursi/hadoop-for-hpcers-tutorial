BASE_DIR   = /user/$(USER)/matmult
INPUT_DIR   = $(BASE_DIR)/input
OUTPUT_DIR  = $(BASE_DIR)/output

TOOLLIBS_DIR=$(HADOOP_PREFIX)/share/hadoop/tools/lib/

run: inputs
	hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
		-files ./map.py,./reduce.py \
		-mapper ./map.py \
		-reducer ./reduce.py \
		-input $(INPUT_DIR) \
		-output  $(OUTPUT_DIR) 
	hdfs dfs -cat $(OUTPUT_DIR)/"*"

inputs:
	hdfs dfs -test -e $(BASE_DIR) || hdfs dfs -mkdir $(BASE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -mkdir $(INPUT_DIR)
	hdfs dfs -test -e $(INPUT_DIR)/part-00000 \
		|| hdfs dfs -put input/part-00000 $(INPUT_DIR)

clean:
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)

.PHONY: clean run inputs
