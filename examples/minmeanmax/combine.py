#!/usr/bin/env python
import sys

xmin  =  999999.
xmax  = -999999.
count = 0
xsum  = 0.
currentKey = None

def updateResults(key, value, xmin, xmax, xsum, count):
    if key == 'min':
        newval = float(value)
        if newval < xmin:
            xmin = newval               
    if key == 'max':
        newval = float(value)
        if newval > xmax:
            xmax = newval               
    if key == 'mean':
        scount, sxsum = value.split(' ')
        newcount = int(scount)
        newxsum = float(sxsum)
        xsum += newxsum
        count += newcount

    return (xmin, xmax, xsum, count)

def printResults(key, xmin, xmax, xsum, count):
    if key == 'min':
        print '%s\t%f' % (key, xmin);
    if key == 'max':
        print '%s\t%f' % (key, xmax);
    if key == 'mean':
        print '%s\t%d %f' % (key, count, xsum);

for line in sys.stdin:
    line = line.strip()

    key, value = line.split('\t')
    if currentKey == key:
        xmin, xmax, xsum, count = updateResults(currentKey, value, 
                                          xmin, xmax, xsum, count)
    else:
        printResults(currentKey, xmin, xmax, xsum, count)
        currentKey = key
        xmin, xmax, xsum, count = updateResults(currentKey, value, 
                                          xmin, xmax, xsum, count)

printResults(currentKey, xmin, xmax, xsum, count)
