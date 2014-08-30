#!/usr/bin/env python
import sys
import numpy

# read parameters from distributed cache - nbins, minmeanmax
f = open('nbins','r')
params = f.readline().strip().split()
nbins = int(params[0])
f.close()

f = open('mmm','r')
params = f.readline().strip().split()
xmin = float(params[1])
params = f.readline().strip().split()
params = f.readline().strip().split()
xmax = float(params[1])
f.close()

dx = (xmax-xmin)/nbins

def binCentre(xmin, dx, value):
    binno = int((value - xmin)/dx)
    return binno*dx + xmin + dx/2.

# process input data
for line in sys.stdin:
    line = line.strip()

    words = line.split()
    xi = float(words[0])
    bincentre = binCentre(xmin, dx, xi)

    print '%f\t%d' % (bincentre, 1)
