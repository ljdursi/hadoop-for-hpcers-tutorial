#!/usr/bin/env python
import sys

count = 0
currentKey = None

for line in sys.stdin:
    line = line.strip()
    key, pcount = line.split('\t')

    if currentKey == key:
        count = count + int(pcount)
    else:
        if currentKey is not None:
            print '%s\t%d' % (currentKey, count)
        count = int(pcount)
        currentKey = key

if currentKey is not None:
    print '%s\t%d' % (currentKey, count)
