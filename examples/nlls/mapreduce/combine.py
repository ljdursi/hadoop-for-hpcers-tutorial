#!/usr/bin/env python
import sys

count = 0
dfda = 0
dfdb = 0
fdfda = 0
fdfdb = 0
dfda2= 0
dfdb2= 0
dfdadb= 0
f = 0

for line in sys.stdin:
    line = line.strip()
    key, pcount, pf, pdfda, pdfdb, pfdfda, pfdfdb, pdfda2, pdfdadb, pdfdb2 = line.split('\t')

    count = count + float(pcount)
    f = f + float(pf)
    dfda  = dfda  + float(pdfda)
    dfdb  = dfdb  + float(pdfdb)
    fdfda = fdfda + float(pfdfda)
    fdfdb = fdfdb + float(pfdfdb)
    dfda2 = dfda  + float(pdfda2)
    dfdadb= dfdadb  + float(pdfdadb)
    dfdb2 = dfdb  + float(pdfdb2)

key = "1" 
print '%s\t%d\t%f\t%f\t%f\t%f\t%f\t%f\t%f\t%f' % (key, count, f, dfda, dfdb, fdfda, fdfdb, dfda2, dfdadb, dfdb2)
