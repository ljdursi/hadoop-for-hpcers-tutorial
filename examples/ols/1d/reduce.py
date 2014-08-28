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
    
covxy = sumxy - (1./count)*sumx*sumy
varx  = sumxx - (1./count)*sumx*sumx
meanx = sumx/count
meany = sumy/count

m = covxy/varx
b = meany - m*meanx

print '%s\t%s' % ("out","y = " + str(m) + " x + " + str(b))

