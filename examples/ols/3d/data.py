#!/usr/bin/env python
"""
Generates data for linear regression example.

Usage: data.py --npts=Npts
"""
import sys
import getopt
import numpy
import numpy.random

class Usage(Exception):
    def __init__(self, msg):
        self.msg = msg

def main(argv=None):
    n = 500
    if argv is None:
        argv = sys.argv
    try:
        try:
            opts, args = getopt.getopt(argv[1:], "hn:", ["help","npts="])
        except getopt.error, msg:
            raise Usage(msg)
        for o, a in opts:
            if o in ("-h","--help"):
                print "Usage: data.py --npts=Npts"
                return 2
            if o in ("-n","--npts"):
                n = int(a)
            else:
                raise Usage()

        # z ~ 1.0 + 3*x * 4*y + N(0,0.5)
        # x,y ~ (-1,1)
        x = numpy.linspace(0,2,n)-1
        y = numpy.linspace(0,2,n)-1
        xv, yv = numpy.meshgrid(x, y, indexing='ij')
        z = 1.0 + 3*xv + 4*yv + numpy.random.randn(n,n)*0.5 
        for i in range(n):
            for j in range(n):
                print x[i],"\t",y[j],"\t",z[i,j]

    except Usage, err:
        print >>sys.stderr, err.msg
        print >>sys.stderr, "for help use --help"
        return 2

if __name__ == "__main__":
    sys.exit(main())
