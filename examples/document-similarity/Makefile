BASE_DIR   = /user/$(USER)/document-similarity/
INPUT_DIR   = $(BASE_DIR)/input
INTERMEDIATE_DIR = $(BASE_DIR)/intermediate
OUTPUT_DIR  = $(BASE_DIR)/output

CLASSPATH=$(shell hadoop classpath)

run: inputs wordcount.jar similarity.jar
	hadoop jar wordcount.jar ca.scinethpc.WordCount \
	       $(INPUT_DIR) $(INTERMEDIATE_DIR)
	hadoop jar similarity.jar ca.scinethpc.Similarity \
	       $(INTERMEDIATE_DIR) $(OUTPUT_DIR)
	hdfs dfs -cat $(OUTPUT_DIR)/"*" | sort -n -k 3

wordcount.jar: WordCount.java
	mkdir -p wordcount_classes
	javac -classpath $(CLASSPATH) \
 	      -d wordcount_classes WordCount.java
	jar -cvf $@ -C wordcount_classes .

similarity.jar: Similarity.java
	mkdir -p similarity_classes
	javac -classpath $(CLASSPATH) \
 	      -d similarity_classes Similarity.java
	jar -cvf $@ -C similarity_classes .

inputs:
	hdfs dfs -test -e $(BASE_DIR) || hdfs dfs -mkdir $(BASE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -put input $(BASE_DIR)

clean:
	-rm -f *.jar
	-rm -rf *_classes
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(INTERMEDIATE_DIR)
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)

.PHONY: clean run
