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

public class WordCount {

  public static class Map 
  	extends Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    private final static Set<String> stopwords = new HashSet<String>(
	Arrays.asList( new String[] {"a", "about",
	    "above", "above", "across", "after",
	    "afterwards", "again", "against", "all",
	    "almost", "alone", "along", "already",
	    "also","although","always","am","among",
	    "amongst", "amount",  "an", "and", "another",
	    "any","anyone","anything","anyway",
	    "anywhere", "are", "around",
	    "as",  "at", "back","be","became",
	    "because","become","becomes", "becoming", "been",
	    "before", "beforehand", "behind", "being",
	    "below", "beside", "besides", "between",
	    "beyond", "bill", "both", "bottom","but",
	    "by", "call", "can", "cannot", "cant", "co",
	    "con", "could", "couldnt", "cry", "de",
	    "describe", "detail", "do", "done", "down",
	    "due", "during", "each", "eg", "eight",
	    "either", "eleven","else", "elsewhere", "empty",
	    "enough", "etc", "even", "ever", "every",
	    "everyone", "everything", "everywhere", "except",
	    "few", "fifteen", "fify", "fill", "find",
	    "fire", "first", "five", "for", "former",
	    "formerly", "forty", "found", "four", "from",
	    "front", "full", "further", "get", "give",
	    "go", "had", "has", "hasnt", "have", "he",
	    "hence", "her", "here", "hereafter", "hereby",
	    "herein", "hereupon", "hers", "herself", "him",
	    "himself", "his", "how", "however", "hundred",
	    "ie", "if", "in", "inc", "indeed", "interest",
	    "into", "is", "it", "its", "itself",
	    "keep", "last", "latter", "latterly", "least",
	    "less", "ltd", "made", "many", "may",
	    "me", "meanwhile", "might", "mill","mine",
	    "more", "moreover", "most", "mostly",
	    "move", "much", "must", "my", "myself",
	    "name", "namely", "neither", "never",
	    "nevertheless", "next", "nine", "no",
	    "nobody", "none", "noone", "nor", "not",
	    "nothing", "now", "nowhere", "of",
	    "off", "often", "on", "once", "one",
	    "only", "onto", "or", "other", "others",
	    "otherwise", "our", "ours", "ourselves",
	    "out", "over", "own","part", "per",
	    "perhaps", "please", "put", "rather",
	    "re", "same", "see", "seem", "seemed",
	    "seeming", "seems", "serious", "several",
	    "she", "should", "show", "side",
	    "since", "sincere", "six", "sixty",
	    "so", "some", "somehow", "someone",
	    "something", "sometime", "sometimes",
	    "somewhere", "still", "such", "system",
	    "take", "ten", "than", "that", "the",
	    "their", "them", "themselves", "then",
	    "thence", "there", "thereafter", "thereby",
	    "therefore", "therein", "thereupon", "these",
	    "they", "thickv", "thin", "third", "this",
	    "those", "though", "three", "through",
	    "throughout", "thru", "thus", "to",
	    "together", "too", "top", "toward",
	    "towards", "twelve", "twenty", "two",
	    "un", "under", "until", "up", "upon",
	    "us", "very", "via", "was", "we",
	    "well", "were", "what", "whatever",
	    "when", "whence", "whenever", "where",
	    "whereafter", "whereas", "whereby", "wherein",
	    "whereupon", "wherever", "whether", "which",
	    "while", "whither", "who", "whoever",
	    "whole", "whom", "whose", "why", "will",
	    "with", "within", "without", "would",
	    "yet", "you", "your", "yours", "yourself",
	    "yourselves", "the"} ) );

    @Override
    public void map(Object key,
		    Text value,
		    Context context) 
		    throws IOException, InterruptedException {

      FileSplit filesplit = (FileSplit)context.getInputSplit();
      String fileName = filesplit.getPath().getName();

      String line = (value.toString()).replaceAll("[^a-z\\sA-Z]","");
      StringTokenizer tokenizer = new StringTokenizer(line);
      while (tokenizer.hasMoreTokens()) {
	String newWord = (tokenizer.nextToken()).toLowerCase();
	if ( (!stopwords.contains(newWord)) && (newWord.length() > 3) ) {
		word.set( fileName + " " + newWord );
		context.write(word, one);
	}
      }
    }
  }

  public static class Reduce 
	  extends Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    public void reduce(Text key,
		       Iterable<IntWritable> valueList,
		       Context context) throws IOException, InterruptedException {
      int sum = 0;
      Iterator<IntWritable> values = valueList.iterator();
      while (values.hasNext()) {
	sum += values.next().get();
      }
      context.write(key, new IntWritable(sum));
    }
  }


  public static void main(String[] args) throws Exception {

    if (args.length != 2) {
      System.err.println("Usage: wordcount <in> <out>");
      System.exit(2);
    }

    Job job = Job.getInstance(new Configuration());
    job.setJobName("wordcount");
    job.setJarByClass(WordCount.class);

    job.setMapperClass(Map.class);
    job.setCombinerClass(Reduce.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);

    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.submit();
    job.waitForCompletion(true);
  }

}
