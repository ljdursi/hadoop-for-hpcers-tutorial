EXAMPLE_DIR = /user/$(USER)/nlls-spark/
INPUT_DIR   = $(EXAMPLE_DIR)/input
NITERS=25

run: inputs 
	spark-submit --master yarn[2] ./nlls-spark.py $(INPUT_DIR)/data.dat 1.5 2.0 $(NITERS)

run-local: ../input/data.dat
	 unset HADOOP_CONF_DIR; \
	 unset SPARK_YARN_USER_ENV; \
	 spark-submit --master local[2] --deploy-mode client \
		./nlls-spark.py ../input/data.dat 1.0 1.0 $(NITERS) ;

directories:
	hdfs dfs -test -e $(EXAMPLE_DIR) || hdfs dfs -mkdir $(EXAMPLE_DIR)
	hdfs dfs -test -e $(INPUT_DIR) || hdfs dfs -mkdir $(INPUT_DIR)

../input/data.dat: ../data.py
	../data.py --npts=1000000 > $@

../input/small-data.dat: ../data.py
	../data.py --npts=5000 > $@

inputs: directories ../input/data.dat
	hdfs dfs -test -e $(INPUT_DIR)/data.dat \
	  || hdfs dfs -put ../input/data.dat $(INPUT_DIR)/data.dat

clean:
	-hdfs dfs -rm -f -r $(INPUT_DIR)
	-hdfs dfs -rm -f -r $(EXAMPLE_DIR)
	-rm -f ../input/data.dat ../input/small-data.dat

.PHONY: directories inputs clean run 
