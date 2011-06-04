#!/bin/sh -e
#
# Invoke Jmxterm interactive command line from a SHELL
#

uberjar="/usr/share/jmxterm/jmxterm-uber.jar"

if [ ! -e $uberjar ]
then
  echo "Uberjar $uberjar doesn't exist! Exit"
  exit 1
fi

for candidate in "$JAVA_HOME/bin/java" $(which java)
do
  if [ -x $candidate ]
  then
    java_cmd=$candidate
    break
  fi
done

if [ -z $java_cmd ]
then
  echo "JAVA_HOME environment variable is not defined"
  echo "JAVA command isn't in PATH either"
  echo "Without an executable Java, Jmxterm can not continue"
  exit 1
fi

$java_cmd -jar $uberjar $@