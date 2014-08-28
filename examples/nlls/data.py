#!/usr/bin/env python
"""
Generates data for nonlinear regression example.

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
    n = 1000000
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

        # y ~ 2 * exp( 3 * t ) + N(0,0.5)
        # t ~ (-2,2)
        t = numpy.linspace( -2, 2, n )
        y = 2.0 * numpy.exp( 3.0 * t ) + numpy.random.randn(n)*0.5
        for i in range(n):
            print t[i],"\t",y[i]

    except Usage, err:
        print >>sys.stderr, err.msg
        print >>sys.stderr, "for help use --help"
        return 2

if __name__ == "__main__":
    sys.exit(main())
