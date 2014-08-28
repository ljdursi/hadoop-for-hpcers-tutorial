from pywebhdfs.webhdfs import PyWebHdfsClient

hdfs = PyWebHdfsClient(host='localhost',port='50070',user_name='hadoop-user')
my_file = 'user/hadoop-user/hdfs-test/data.dat'

print 'Status of file: ', my_file
status = hdfs.get_file_dir_status(my_file)
print status

print 'Second 500 bytes of file: ',my_file
data = hdfs.read_file(my_file,offset=500,length=500)

print data
