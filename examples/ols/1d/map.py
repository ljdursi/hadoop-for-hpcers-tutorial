#!/usr/bin/env python

import sys

for line in sys.stdin:
    line = line.strip()

    words = line.split()
    x = float(words[0])
    y = float(words[1])
    
    count = 1 
    sumx  = x
    sumy  = y
    sumxy = x*y
    sumxx = x*x

    key = "1"
    print '%s\t%f\t%f\t%f\t%f\t%f' % (key, count, sumx, sumy, sumxy, sumxx)
