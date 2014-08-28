package ca.scinethpc;

import java.io.IOException;
import java.util.*;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
import org.apache.hadoop.mapreduce.lib.join.*;
import org.apache.hadoop.util.*;

public class MatMult {

  public static class Map 
      extends  Mapper<Object, Text, Text, Text> {

    private final int maxrows=100;
    private Text emitKey   = new Text();
    private Text emitValue = new Text();

    @Override
    public void map(Object key,
		    Text value,
		    Context context) throws IOException, InterruptedException {

      String line = value.toString().trim();
      String[] items = line.split("\\s+");
      String matrix = items[0];
      int row    = Integer.parseInt(items[1]);
      int col    = Integer.parseInt(items[2]);
      int val    = Integer.parseInt(items[3]);

      if (matrix.equals("A")) {

	/* set value */
	emitValue.set( /* ... ? ... */);

	/* do something with key */
            emitKey.set( /* ... ? ... */ );
	    context.write(emitKey, emitValue);

      } else {

	/* set value */
	emitValue.set( /* ... ? ... */);

	/* do something with key */
            emitKey.set( /* ... ? ... */ );
	    context.write(emitKey, emitValue);

      }
    }
  }

  public static class Reduce 
	  extends Reducer<Text, Text, Text, IntWritable> {

    private Text rowcol = new Text("");
    private IntWritable sumIW = new IntWritable();

    @Override
    public void reduce(Text key,
		       Iterable<Text> valueList,
		       Context context) throws IOException, InterruptedException {

      int sum = 0;
      HashMap<Integer,Integer> matAvalues = new HashMap<Integer,Integer>();
      HashMap<Integer,Integer> matBvalues = new HashMap<Integer,Integer>();

      String[] keyitems = key.toString().split("\\s+");
      int row = Integer.parseInt(keyitems[0]);
      int col = Integer.parseInt(keyitems[1]);

      Iterator<Text> values = valueList.iterator();

      while (values.hasNext()) {
        String[] valitems = values.next().toString().split("\\s+");
	String matrix = valitems[0];
        int  index    = Integer.parseInt(valitems[1]);
        int  value    = Integer.parseInt(valitems[2]);

	if ( matrix.equals("A") ) {
	    matAvalues.put(index, value);
	} else {
	    matBvalues.put(index, value);
	}
      }

      rowcol.set(Integer.toString(row)+", "+Integer.toString(col));
      Iterator it = matAvalues.entrySet().iterator();

      for (Integer index : matAvalues.keySet()) {
	if (matBvalues.containsKey(index)) 
	    sum += matAvalues.get(index) * matBvalues.get(index);
      } 

      if (sum != 0) {
          sumIW.set(sum);
	  context.write( rowcol, sumIW );
      }
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: matmult <in> <out>");
      System.exit(2);
    }

    Job job = Job.getInstance(new Configuration());
    job.setJobName("matmult");
    job.setJarByClass(MatMult.class);

    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.submit();
    job.waitForCompletion(true);
  }
}
