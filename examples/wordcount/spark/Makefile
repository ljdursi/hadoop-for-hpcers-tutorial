EXAMPLE_DIR = /user/$(USER)/wordcount-spark/
INPUT_DIR   = $(EXAMPLE_DIR)/input

TOOLLIBS_DIR=$(HADOOP_PREFIX)/share/hadoop/tools/lib/

run: inputdir
	spark-submit --master yarn[2] ./wordcount-spark.py $(INPUT_DIR)/astro_02 | sort -n -t: -k2

run-local: 
	(unset HADOOP_CONF_DIR; \
	 unset SPARK_YARN_USER_ENV; \
	 spark-submit --master local[2] --deploy-mode client ./wordcount-spark.py ./input/astro_02 | sort -n -t: -k2 )

inputdir:
	hdfs dfs -test -e $(EXAMPLE_DIR) || hdfs dfs -mkdir $(EXAMPLE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -mkdir $(INPUT_DIR)
	hdfs dfs -put ../input-large/astro_02 $(INPUT_DIR)

clean:
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(EXAMPLE_DIR)

.PHONY: directories inputs clean run run
