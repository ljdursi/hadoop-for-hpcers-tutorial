#!/usr/bin/env python
import sys
import numpy

for line in sys.stdin:
    line = line.strip()

    words = line.split()
    x = float(words[0])
    y = float(words[1])
    z = float(words[2])

    xvec = numpy.array([1.,x,y])

    xTx  = numpy.outer(xvec,xvec)
    xTy  = z*xvec

    count = 1
    
    key = "1"
    print '%s\t%d\t%s\t%s' % (key, count, numpy.array_str(xTy).translate(None,'[]\n'), 
                                   numpy.array_str(xTx).translate(None,'[]\n'))
