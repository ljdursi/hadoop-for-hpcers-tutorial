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

public class Similarity {

  public static class Map 
  	extends Mapper<Object, Text, Text, Text> {


    private final String[] documents = new String[] {
	"astro_01", "astro_02", "astro_03", "astro_04",
	"astro_05", "cell_bio_01", "cell_bio_02", "cell_bio_03",
	"cell_bio_04", "cell_bio_05", "computational_finance_01",
	"computational_finance_02", "computational_finance_03",
	"computational_finance_04", "computational_finance_05",
	"crypto_01", "crypto_02", "crypto_03", "crypto_04", "crypto_05",
	"databases_03", "databases_04", "databases_05", "databases_01",
	"databases_02", "genomics_01", "genomics_02", "genomics_03",
	"genomics_04", "genomics_05", "pdes_01", "pdes_02", "pdes_03",
	"pdes_04", "pdes_05", "robotics_01", "robotics_02", "robotics_03",
	"robotics_04", "robotics_05"};

    @Override
    public void map(Object key,
		    Text value,
		    Context context) 
		    throws IOException, InterruptedException {

      String line = value.toString().trim();
      String[] items = line.split("\\s+");
      String doc = items[0];

      for ( String otherdocs : documents ) {
	  Text docpair = new Text();
	  int order = otherdocs.compareTo(doc);
          if ( order < 0 ) {	
	   	docpair.set(otherdocs + " " + doc);
		context.write(docpair, value);
    	  } else if ( order > 0 ) {
	  	docpair.set(doc + " " + otherdocs);
		context.write(docpair, value);
	  }
       }
    }
  }

  public static class Reduce 
  	extends  Reducer<Text, Text, Text, DoubleWritable> {

    @Override
    public void reduce(Text key,
		       Iterable<Text> valueList,
		       Context context) throws IOException, InterruptedException {

      Double sum = 0.0;
      String docs[] = (key.toString()).split("\\s+");
      HashMap<String,Double> doc1words = new HashMap<String,Double>();
      HashMap<String,Double> doc2words = new HashMap<String,Double>();
      Iterator<Text>values = valueList.iterator();

      while (values.hasNext()) {
	String line = values.next().toString().trim();
	String terms[] = line.split("\\s+"); 

      	if (terms.length != 3) continue;

	String docname = terms[0];
	String word    = terms[1];
	Double count   = Double.parseDouble(terms[2]);

	if ( docname.equals(docs[0]) ) {
	    doc1words.put(word, count);
	} else {
	    doc2words.put(word, count);
	}
      }

      Double doc1mag = 0.;
      Double doc2mag = 0.;

      for ( Double value : doc1words.values() ) {
	doc1mag += value*value;
      } 
      doc1mag = Math.sqrt(doc1mag);

      for ( Double value : doc2words.values() ) {
	doc2mag += value*value;
      } 
      doc2mag = Math.sqrt(doc2mag);
      
      for ( String word : doc1words.keySet() ) {
	if (doc2words.containsKey(word)) {
	    sum += doc1words.get(word)*doc2words.get(word);	
	}
      } 

      context.write( key, new DoubleWritable(sum/(doc1mag*doc2mag)) );
    }
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: wordcount <in> <out>");
      System.exit(2);
    }

    Job job = Job.getInstance(new Configuration());
    job.setJobName("similarity");
    job.setJarByClass(Similarity.class);

    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);

    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.submit();
    job.waitForCompletion(true);
  }
}
