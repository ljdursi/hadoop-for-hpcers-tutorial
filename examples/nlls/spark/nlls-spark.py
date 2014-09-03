#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

"""
Non-Linear least squres fitting, based on the Spark Logistic Regression 
example (${SPARK}/examples/src/main/python/logistic_regression.py).
"""
import sys

import numpy as np
from StringIO import StringIO
from pyspark import SparkContext

D = 2  # Number of dimensions

def readPointBatch(iterator):
    strs = list(iterator)
    matrix = np.zeros((len(strs), D))
    for i in xrange(len(strs)):
        matrix[i] = np.genfromtxt(StringIO(strs[i]))
    return [matrix]

if __name__ == "__main__":
    if len(sys.argv) != 5:
        print "Usage: logistic_regression <file> <parama> <paramb> <iterations>"
        exit(-1)
    sc = SparkContext(appName="PythonNLLS")
    points = sc.textFile(sys.argv[1]).mapPartitions(readPointBatch).cache()
    parama = float(sys.argv[2])
    paramb = float(sys.argv[3])
    iterations = int(sys.argv[4])

    w = np.array([parama, paramb])
    print "Initial w: " + str(w)

    def gradient(matrix, w):
        X = matrix[:, 0]
        Y = matrix[:, 1]

        expbt = np.exp(w[1]*X)
        f = Y - w[0]*expbt
        dfda = -expbt
        dfdb = -w[0]*X*expbt

        grad = np.array([np.sum(f*dfda), np.sum(f*dfdb)])
        JtJ  = np.array([[np.sum(dfda*dfda), np.sum(dfda*dfdb)],
                            [np.sum(dfda*dfdb), np.sum(dfdb*dfdb)]])
        return (grad, JtJ)

    def add(x, y):
        return (x[0]+y[0], x[1]+y[1])

    for i in range(iterations):
        grad,JtJ = points.map(lambda m: gradient(m,w)).reduce(add)
        dw = np.linalg.solve(JtJ, -grad)
        w += dw*0.5
        print i, w

    print "Final w: " + str(w)
