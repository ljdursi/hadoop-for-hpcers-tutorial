EXAMPLE_DIR = /user/$(USER)/wordcount
INPUT_DIR   = $(EXAMPLE_DIR)/input
OUTPUT_DIR  = $(EXAMPLE_DIR)/output
OUTPUT_FILE = $(OUTPUT_DIR)/part-r-00000

CLASSPATH=$(shell hadoop classpath)

run: wordcount.jar inputs
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	hadoop jar wordcount.jar ca.scinethpc.WordCount \
	       $(INPUT_DIR) $(OUTPUT_DIR)
	hdfs dfs -cat wordcount/output/"*"

wordcount.jar: WordCount.java
	mkdir -p wordcount_classes
	javac -classpath $(CLASSPATH) -Xlint:deprecation \
 	      -d wordcount_classes WordCount.java
	jar -cvf wordcount.jar -C wordcount_classes .

inputdir:
	hdfs dfs -test -e $(EXAMPLE_DIR) || hdfs dfs -mkdir $(EXAMPLE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -mkdir $(INPUT_DIR)

outputdir:
	hdfs dfs -test -e $(OUTPUT_DIR) || hdfs dfs -mkdir $(OUTPUT_DIR)

inputs: inputdir
	hdfs dfs -test -e $(INPUT_DIR)/file01 \
	  || hdfs dfs -put ../input-small/file01 $(INPUT_DIR)/file01
	hdfs dfs -test -e $(INPUT_DIR)/file02 \
	  || hdfs dfs -put ../input-small/file02 $(INPUT_DIR)/file02

clean:
	-rm *.jar
	-rm -r *_classes
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	-hdfs dfs -rm -f -r $(EXAMPLE_DIR)

.PHONY: clean run directories inputs
