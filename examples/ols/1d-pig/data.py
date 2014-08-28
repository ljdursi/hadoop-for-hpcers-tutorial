#!/usr/bin/env python
"""
Generates data for linear regression example.

Usage: data.py --npts=Npts
"""
import sys
import getopt
import random

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

        # y_A ~ 2*x + 0.1 + N(0,0.5)
        # y_B ~ 4*x - 0.1 + N(0,0.5)
        # x ~ (-1,1)
        labels  = ['A','B']
        bcoeffs = [ 2 , 4 ]
        acoeffs = [+.1,-.1]
        x = -1 + 1./n
        for i in range(n):
            which = random.randint(0,1)
            noise = random.randint(0,10000)
            if noise == 1:
                print labels[which],"\t",x,"\t",random.normalvariate(0,0.5)+ \
                                                     1000.+bcoeffs[which]*x+acoeffs[which]
            else:
                print labels[which],"\t",x,"\t",random.normalvariate(0,0.5)+ \
                                                     bcoeffs[which]*x+acoeffs[which]
            x = x + 2./n

    except Usage, err:
        print >>sys.stderr, err.msg
        print >>sys.stderr, "for help use --help"
        return 2

if __name__ == "__main__":
    sys.exit(main())
