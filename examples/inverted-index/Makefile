EXAMPLE_DIR = /user/$(USER)/inverted-index
INPUT_DIR   = $(EXAMPLE_DIR)/input
OUTPUT_DIR  = $(EXAMPLE_DIR)/output
OUTPUT_FILE = $(OUTPUT_DIR)/part-r-00000

CLASSPATH=$(shell hadoop classpath)
TOOLLIBS_DIR=$(HADOOP_PREFIX)/share/hadoop/tools/lib/

run: wordcount.jar inputdir
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	hadoop jar wordcount.jar ca.scinethpc.WordCount \
	       $(INPUT_DIR) $(OUTPUT_DIR)
	hdfs dfs -cat $(OUTPUT_DIR)/"*"

run-streaming: inputdir
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	hadoop jar $(TOOLLIBS_DIR)/hadoop-streaming-$(HADOOP_VERSION).jar \
		-files ./wordcount-map.py,./wordcount-reduce.py \
		-mapper ./wordcount-map.py \
		-input $(INPUT_DIR) \
		-output  $(OUTPUT_DIR) 
	hdfs dfs -cat $(OUTPUT_DIR)/"*"

runinverted: invertedindex.jar inputdir
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	hadoop jar invertedindex.jar ca.scinethpc.InvertedIndex \
	       $(INPUT_DIR) $(OUTPUT_DIR)
	hdfs dfs -cat $(OUTPUT_DIR)/"*"

invertedindex.jar: InvertedIndex.java
	mkdir -p invertedindex_classes
	javac -classpath $(CLASSPATH) \
 	      -d invertedindex_classes InvertedIndex.java
	jar -cvf $@ -C invertedindex_classes .

wordcount.jar: WordCount.java
	mkdir -p wordcount_classes
	javac -classpath $(CLASSPATH) \
 	      -d wordcount_classes WordCount.java
	jar -cvf $@ -C wordcount_classes .

inputdir:
	hdfs dfs -test -e $(EXAMPLE_DIR) || hdfs dfs -mkdir $(EXAMPLE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -put input $(EXAMPLE_DIR)

clean:
	-rm -f *.jar
	-rm -rf *_classes
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)

.PHONY: clean run directories inputdir
