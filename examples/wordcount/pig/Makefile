EXAMPLE_DIR = /user/$(USER)/wordcount-pig/
INPUT_DIR   = $(EXAMPLE_DIR)/input-large
OUTPUT_DIR  = $(EXAMPLE_DIR)/output
OUTPUT_FILE = $(OUTPUT_DIR)/part-00000

TOOLLIBS_DIR=$(HADOOP_PREFIX)/share/hadoop/tools/lib/

run: inputdir
	pig wordcount.pig
	hdfs dfs -cat $(OUTPUT_DIR)/part-r-00000 | sort -n -k 2 -r | head -n 25

inputdir:
	hdfs dfs -test -e $(EXAMPLE_DIR) || hdfs dfs -mkdir $(EXAMPLE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -put ../input-large $(EXAMPLE_DIR)

clean:
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	-hdfs dfs -rm -r -f $(EXAMPLE_DIR)
	rm -r -f pig*.log

.PHONY: directories inputs clean run run
