#!/usr/bin/env python
import sys
import numpy
import numpy.linalg

# read parameters from distributed cache
f = open('params','r')
paramline = f.readline()
params = paramline.strip().split()
a = float(params[0])
b = float(params[1])
f.close()

scale = 0.99  # scale factor to prevent overshoot

fdfda = 0
fdfdb = 0
dfda2= 0
dfdb2= 0
dfdadb= 0

for line in sys.stdin:
    line = line.strip()
    key, pfdfda, pfdfdb, pdfda2, pdfdadb, pdfdb2 = line.split('\t')

    fdfda = fdfda + float(pfdfda)
    fdfdb = fdfdb + float(pfdfdb)
    dfda2 = dfda  + float(pdfda2)
    dfdadb= dfdadb  + float(pdfdadb)
    dfdb2 = dfdb  + float(pdfdb2)

JtJ = numpy.array( [[dfda2, dfdadb],[dfdadb, dfdb2]] )
gradf = numpy.array( [fdfda, fdfdb] )

#print gradf
#print JtJ

dparams = numpy.linalg.solve(JtJ,-gradf)
if not numpy.allclose(numpy.dot(JtJ,dparams),-gradf):
    print "Error solving system"

dparams = dparams*scale
anew = a + dparams[0]
bnew = b + dparams[1]
print '%f\t%f' % (anew, bnew)
