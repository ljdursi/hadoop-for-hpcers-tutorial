BASE_DIR   = /user/$(USER)/diffuse
INPUT_DIR   = $(BASE_DIR)/timestep-0
INTERMEDIATE_DIR = $(BASE_DIR)/intermediate
OUTPUT_DIR  = $(BASE_DIR)/output
OUTPUT_FILE = $(OUTPUT_DIR)/part-r-0000

CLASSPATH=$(shell hadoop classpath)

run: diffuse.jar inputs
	hadoop jar diffuse.jar ca.scinethpc.Diffuse \
	       		$(BASE_DIR)/timestep-0 \
	       		$(BASE_DIR)/timestep-tmp1
	for step in `seq 5`; do \
		hadoop jar diffuse.jar ca.scinethpc.Diffuse \
				$(BASE_DIR)/timestep-tmp1  \
				$(BASE_DIR)/timestep-tmp2; \
		hdfs dfs -rm -f -r $(BASE_DIR)/timestep-tmp1; \
		hadoop jar diffuse.jar ca.scinethpc.Diffuse \
				$(BASE_DIR)/timestep-tmp2 \
				$(BASE_DIR)/timestep-tmp1; \
		hdfs dfs -rm -f -r $(BASE_DIR)/timestep-tmp2; done
	hadoop jar diffuse.jar ca.scinethpc.Diffuse \
			$(BASE_DIR)/timestep-tmp1 \
			$(BASE_DIR)/timestep-1; done
	hdfs dfs -cat diffuse/timestep-0/part-00000 | awk 'BEGIN {FS=":"} {printf $$2 " "} END {print ""}' > data
	hdfs dfs -cat diffuse/timestep-1/part-r-00000 | awk 'BEGIN {FS=":"}; {printf $$2 " "} END {print ""}' >> data
	gnuplot plot.gp
	

diffuse.jar: Diffuse.java
	mkdir -p diffuse_classes
	javac -classpath $(CLASSPATH) \
 	      -d diffuse_classes Diffuse.java
	jar -cvf diffuse.jar -C diffuse_classes .

inputs:
	hdfs dfs -test -e $(BASE_DIR) || hdfs dfs -mkdir $(BASE_DIR)
	hdfs dfs -put timestep-0 $(BASE_DIR)

clean:
	-rm -f *.jar
	-rm -rf *_classes
	-rm -f data
	-hdfs dfs -rm -f -r $(BASE_DIR)

.PHONY: clean run
