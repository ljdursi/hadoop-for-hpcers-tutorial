#!/usr/bin/env python

import sys
maxrows=100

for line in sys.stdin:
    line = line.strip()

    matrix, row, col, val = line.split()
    row = int(row)
    col = int(col)
    val = int(val)

    if matrix == 'A':	
        value = matrix+'-'+str(col)+'-'+str(val)
        for j in xrange(maxrows):
            key = str(row)+'-'+str(j)
            print '%s\t%s' % ( key, value )
    else:		
        value = matrix+'-'+str(row)+'-'+str(val)
        for i in xrange(maxrows):
            key = str(i)+'-'+str(col)
            print '%s\t%s' % ( key, value )
