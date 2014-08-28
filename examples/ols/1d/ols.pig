data    = LOAD 'ols1d/input/data.dat' USING PigStorage('\t') 
               as (x:double, y:double);
allcols = FOREACH data generate x as x, y as y, x*x as xsquared, x*y as xy;
onerow  = GROUP allcols ALL;
sums    = FOREACH onerow GENERATE COUNT(allcols) as n, 
	   SUM(allcols.x) as sumx, SUM(allcols.y) as sumy, 
           SUM(allcols.xsquared) as sumxx, SUM(allcols.xy) as sumxy;
params  = FOREACH sums GENERATE (sumxy-sumx*sumy/n)/(sumxx-sumx*sumx/n) as m, 
                sumy/n - sumx/n*(sumxy-sumx*sumy/n)/(sumxx-sumx*sumx/n) as b;
STORE params into 'ols1d/pig-output';
