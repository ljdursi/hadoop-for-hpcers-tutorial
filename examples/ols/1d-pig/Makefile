EXAMPLE_DIR = /user/$(USER)/olspig1d/
INPUT_DIR   = $(EXAMPLE_DIR)/input
OUTPUT_DIR  = $(EXAMPLE_DIR)/pig-output

run: inputs
	hdfs dfs -rm -f -r $(OUTPUT_DIR)
	pig ols.pig
	hdfs dfs -cat $(OUTPUT_DIR)/"*"

directories:
	hdfs dfs -test -e $(EXAMPLE_DIR) || hdfs dfs -mkdir $(EXAMPLE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -mkdir $(INPUT_DIR)

input/data.dat: data.py
	./data.py --npts=1000000 > $@

inputs: directories input/data.dat
	hdfs dfs -test -e $(INPUT_DIR)/data.dat \
	  || hdfs dfs -put input/data.dat $(INPUT_DIR)/data.dat

clean:
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(OUTPUT_DIR)
	-hdfs dfs -rm -f -r $(EXAMPLE_DIR)
	-rm -f pig*.log
	-rm -f input/*

.PHONY: directories inputs clean run 
