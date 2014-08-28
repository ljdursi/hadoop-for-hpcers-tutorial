lines = load 'wordcount-pig/input-large/*' 
        using PigStorage
        as (line:chararray);
words = foreach lines
        generate flatten(TOKENIZE(line))
        as word;
word_groups = group words by word;
word_counts = foreach word_groups
              generate group as word, COUNT(words);
store word_counts into 'wordcount-pig/output';
