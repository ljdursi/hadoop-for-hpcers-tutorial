#!/bin/sh

# shut down services
${HADOOP_PREFIX}/sbin/mr-jobhistory-daemon.sh stop historyserver
${HADOOP_PREFIX}/sbin/stop-yarn.sh
${HADOOP_PREFIX}/sbin/stop-dfs.sh

# clean up anything in the hadoop filesystem
rm -rf ~/hdfs-data/hdfs/namenode/*
rm -rf ~/hdfs-data/hdfs/datanode/*
