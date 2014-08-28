#!/bin/sh

# clean up any remnants
rm -rf ~/hdfs-data/hdfs/namenode/*
rm -rf ~/hdfs-data/hdfs/datanode/*
yes | hdfs namenode -format

# start services
yes | start-dfs.sh
yes | start-yarn.sh
yes | mr-jobhistory-daemon.sh start historyserver

# make sure home directory exists
hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/${USER}

# check to see that all services are running
jps
