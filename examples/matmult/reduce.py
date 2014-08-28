#!/usr/bin/env python

import sys

def putValue(matrix, index, value, matAvalues, matBvalues):
    if matrix == 'A':
         matAvalues[index] = value
    else:
         matBvalues[index] = value

def calcSum(matAvalues, matBvalues):
    matrixelement = 0
    for index, value in matAvalues.iteritems():
        if index in matBvalues:
            matrixelement += value * matBvalues[index]
    return matrixelement

def printSum(row, col, value):
    if value != 0:
        print "%d,%d,%d" % (row, col, value)

maxrows=100

currentRow = None
currentCol = None
matAvalues = {}
matBvalues = {}

for line in sys.stdin:
    line = line.strip()

    key, value = line.split()
    row, col = key.split('-')
    matrix, index, val = value.split('-')

    row = int(row)
    col = int(col)
    val = int(val)
    index  = int(index)

    if row != currentRow or col != currentCol:
        # new key
        if currentRow is not None:
            # calculate, print results
            matrixElement = calcSum(matAvalues, matBvalues)
            printSum(currentRow, currentCol, matrixElement)
            matAvalues = {}
            matBvalues = {}

        # new currentRow/col
        currentRow = row
        currentCol = col

    # put value in maps
    putValue(matrix, index, val, matAvalues, matBvalues)

if currentRow is not None:
    matrixElement = calcSum(matAvalues, matBvalues)
    printSum(currentRow, currentCol, matrixElement)
