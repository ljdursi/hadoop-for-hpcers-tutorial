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

public class InvertedIndex {

  public static class Map 
  	extends Mapper<Object, Text, Text, Text> {

    private Text word     = new Text();
    private Text location = new Text();

    private final static Set<String> stopwords = new HashSet<String>(
	Arrays.asList( new String[] {"a", "about",
	    "above", "across", "after",
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
	    "by", "call", "can", "cannot", "cant", 
	    "could", "couldnt", "cry", 
	    "describe", "detail", "do", "done", "down",
	    "due", "during", "each", "eg", "eight",
	    "either", "eleven","else", "elsewhere", "empty",
	    "enough", "etc", "even", "ever", "every",
	    "everyone", "everything", "everywhere", "except",
	    "few", "fifteen", "fifty", "fill", "find",
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
	    "yourselves"} ) );

    @Override
    public void map(Object key,
		    Text value,
		    Context context) 
		    throws IOException, InterruptedException {

      FileSplit filesplit = (FileSplit)context.getInputSplit();
      String fileName = filesplit.getPath().getName();
      Set<String> wordsOccurred = new HashSet<String>();

      location.set(fileName);
      String line = (value.toString()).replaceAll("[^a-z\\sA-Z]","");
      StringTokenizer tokenizer = new StringTokenizer(line);
      while (tokenizer.hasMoreTokens()) {
	String newWord = (tokenizer.nextToken()).toLowerCase();
	if ( (!stopwords.contains(newWord)) && (newWord.length() > 3) && (!wordsOccurred.contains(newWord))) {

		/* what should key, value be? */
		/* ... context.write( key, value ) */
	}
      }
    }
  }

  public static class Reduce 
	  extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key,
		       Iterable<Text> valueList,
		       Context context) throws IOException, InterruptedException {
      int sum = 0;
      Iterator<Text> values = valueList.iterator();
      Set<String> documentsOccurred = new HashSet<String>();
      Text alldocuments = new Text(""); /* value */
      String newDoc;

      while (values.hasNext()) {
	newDoc = values.next().toString();	
	if (!documentsOccurred.contains(newDoc)) {
		documentsOccurred.add(newDoc);
		/* what should we do with value?  alldocuments.set()... */
	}
      }
      context.write(key, alldocuments);
    }
  }


  public static void main(String[] args) throws Exception {

    if (args.length != 2) {
      System.err.println("Usage: invertedindex <in> <out>");
      System.exit(2);
    }

    Job job = Job.getInstance(new Configuration());
    job.setJobName("invertedindex");
    job.setJarByClass(InvertedIndex.class);

    job.setMapperClass(Map.class);
    job.setCombinerClass(Reduce.class);
    job.setReducerClass(Reduce.class);

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);

    FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.submit();
    job.waitForCompletion(true);
  }

}
