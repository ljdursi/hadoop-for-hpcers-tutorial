#!/usr/bin/env python

import sys
import numpy
from StringIO import StringIO

def calculate(seg, leftGC, rightGC, data, coeff):
    if seg is not None and data is not None:
        alldata = numpy.append( numpy.append(rightGC, data), leftGC)
        return alldata[1:-1] + coeff*( alldata[0:-2] - 2.*alldata[1:-1] + alldata[2:] )
    else:
        return None

def printData(seg, data):
    if data is not None:
        datastr = numpy.array_str(data)
        datastr = datastr[1:-1]            # strip brackets
        datastr = datastr.replace('\n','') # strip newlines
        print '%d:%s' % (seg, datastr)

dx = 1.
D  = 1.
c  = 0.75
dt = c*dx*dx/(2.*D)
coeff = D * dt / (dx * dx)

currentSegment = None
leftGC = 0.
rightGC = 0.
data = None

for line in sys.stdin:
    line = line.strip()
    segment, datastr = line.split(':')

    segment = int(segment)
    if segment != currentSegment:
        # output previous data 
        if currentSegment is not None:
            updatedData = calculate(currentSegment, leftGC, rightGC, data, coeff)
            printData(currentSegment, updatedData)
 
        # init new segment
        currentSegment = segment
	leftGC = 0.
	rightGC = 0.
	data = None

    firstchar = datastr[0]
    if firstchar == 'L':
        leftGC = float(datastr[1:])
    elif firstchar == 'R':
        rightGC = float(datastr[1:])
    else:
        data = numpy.genfromtxt(StringIO(datastr))

if currentSegment is not None:
    updatedData = calculate(currentSegment, leftGC, rightGC, data, coeff)
    printData(currentSegment, updatedData)
