#!/usr/bin/env python
import sys

count = 0
sumx  = 0
sumy  = 0
sumxy = 0
sumxx = 0
count = 0

for line in sys.stdin:
    line = line.strip()

    key, pcount, psumx, psumy, psumxy, psumxx = line.split()
    count = count + float(pcount)
    sumx  = sumx  + float(psumx)
    sumy  = sumy  + float(psumy)
    sumxy = sumxy + float(psumxy)
    sumxx = sumxx + float(psumxx)
    
print '%s\t%f\t%f\t%f\t%f\t%f' % (key, count, sumx, sumy, sumxy, sumxx)
