rawdata = LOAD 'olspig1d/input/data.dat' USING PigStorage('\t') as (type:chararray, x:double, y:double);
data    = FILTER rawdata by y < 500;
allcols = FOREACH data generate type as type, x as x, y as y, x*x as xsquared, x*y as xy;
onerow  = GROUP allcols by type;
sums    = FOREACH onerow GENERATE group as type, COUNT(allcols) as n, SUM(allcols.x) as sumx, 
           SUM(allcols.y) as sumy, SUM(allcols.xsquared) as sumxx, SUM(allcols.xy) as sumxy;
params  = FOREACH sums GENERATE type as type, (sumxy-sumx*sumy/n)/(sumxx-sumx*sumx/n) as m, sumy/n - sumx/n*(sumxy-sumx*sumy/n)/(sumxx-sumx*sumx/n) as b;
STORE params into 'olspig1d/pig-output';
