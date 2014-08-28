#!/usr/bin/env python
import sys
import numpy

xTx = numpy.zeros((3,3))
xTy = numpy.zeros(3)
count = 0

for line in sys.stdin:
    line = line.strip()

    key, pcount, pxTy, pxTx, = line.split('\t')
    count = count + float(pcount)
    xTy   = xTy + numpy.fromstring(pxTy, sep=' ')
    xTx   = xTx + numpy.fromstring(pxTx, sep=' ').reshape(3,3)
    
key = "1" 
print '%s\t%d\t%s\t%s' % (key, count, numpy.array_str(xTy).translate(None,'[]\n'), 
                               numpy.array_str(xTx).translate(None,'[]\n'))
