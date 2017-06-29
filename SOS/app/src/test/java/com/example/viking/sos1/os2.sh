#!/bin/bash

a=$1

b=$2

set -- `ls -l $1`

x=$1

set -- `ls -l $b`

y=$1

if [ $x = $y ] ; then

echo "equal"

echo $x

echo $y

else

echo "vik"

echo "$a $x"

echo "$b $y"

fi