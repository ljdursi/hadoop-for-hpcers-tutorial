#!/usr/bin/env python

import sys
import numpy
from StringIO import StringIO

for line in sys.stdin:
    line = line.strip()
    segment, datastr = line.split(':')

    segment = int(segment)
    data = numpy.genfromtxt(StringIO(datastr))

    # left guardcell
    if (segment > 0):
        print '%d:L%f' % (segment-1,data[0])

    # right guardcell
    print '%d:R%f' % (segment+1,data[-1])
  
    # all data
    print line
