#!/usr/bin/env python

import sys

for line in sys.stdin:
    line = line.strip()

    words = line.split()
    x = float(words[0])
    
    print '%s\t%f' % ("min", x)
    print '%s\t%d %f' % ("mean", 1, x)
    print '%s\t%f' % ("max", x)
