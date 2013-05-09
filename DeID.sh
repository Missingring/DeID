#!/bin/sh

mydir=$(cd "$( dirname ${BASH_SOURCE[0]} )" && pwd )
java -Xmx512m -Xms256m -jar "$mydir"/DeID.jar
