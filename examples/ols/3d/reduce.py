#!/usr/bin/env python
import sys
import numpy
import numpy.linalg

xTx = numpy.zeros((3,3))
xTy = numpy.zeros(3)
count = 0

for line in sys.stdin:
    line = line.strip()

    key, pcount, pxTy, pxTx, = line.split('\t')
    count = count + float(pcount)
    xTy   = xTy + numpy.fromstring(pxTy, sep=' ')
    xTx   = xTx + numpy.fromstring(pxTx, sep=' ').reshape(3,3)
    
params = numpy.linalg.solve(xTx,xTy)
print '%s\t%s' % ("out",numpy.array_str(params))

