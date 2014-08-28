package ca.scinethpc;
 
import java.io.IOException;
import java.util.*;
 
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.util.*;

public class Diffuse {

  public static class Map 
	  extends Mapper<Object, Text, IntWritable, Text> {

    private final static IntWritable one = new IntWritable(1);
    private final static int nparts = 5;

    @Override
    public void map(Object key,
                    Text value,
                    Context context) throws IOException, InterruptedException {

      String line = value.toString().trim();
      String[] parts = line.split(":");
      Integer segment = Integer.parseInt(parts[0].trim());

      String[] data = parts[1].trim().split("\\s+");
      String leftGuardCell = data[0];
      String rightGuardCell = data[data.length-1];

      context.write(new IntWritable(segment), new Text(parts[1]));
      if (segment > 0)
	context.write(new IntWritable(segment-1),new Text("L "+leftGuardCell));
      if (segment < nparts-1)
	context.write(new IntWritable(segment+1),new Text("R "+rightGuardCell));
    }
  }

  public static class Reduce 
	  extends Reducer<IntWritable, Text, IntWritable, Text> {

    @Override
    public void reduce(IntWritable key,
                       Iterable<Text> valueList,
                       Context context) throws IOException, InterruptedException {

      Iterator<Text> values = valueList.iterator();
      double[] data = new double[0];
      double left=0., right=0.;

      double dx = 1.;
      double D  = 1.;
      double c  = 0.75;
      double dt = c * dx * dx/(2.*D);

      String v = "";     
      while (values.hasNext()) {
	String[] parts = values.next().toString().trim().split("\\s+");
        if (parts[0].equals("L")) 
	    left = Double.parseDouble(parts[1]);
        else if (parts[0].equals("R"))
	    right = Double.parseDouble(parts[1]);
	else {
	    data = new double[parts.length+2];
	    for (int i=0; i<parts.length; i++)	
		data[i+1] = Double.parseDouble(parts[i]); 
	} 
      }
      data[0] = right;
      data[data.length-1] = left;

      double[] newdata = new double[data.length];
      for (int i=1; i<data.length-1; i++) 
	  newdata[i] = data[i] + 
              (D * dt / dx*dx )*(data[i+1]-2.*data[i]+data[i-1]);
      
      String outStr=":";
      for (int i=1; i<newdata.length-1; i++)
         outStr = outStr + " " + Double.toString(newdata[i]);

      context.write(key, new Text(outStr));
    }
  }

  public static void main(String[] args) throws Exception {

    if (args.length != 2) {
      System.err.println("Usage: diffuse <in> <out>");
      System.exit(2);
    }

    Job job = Job.getInstance(new Configuration());
    job.setJobName("diffuse");
    job.setJarByClass(Diffuse.class);

    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.submit();
    job.waitForCompletion(true);
  }
}
