#!/bin/bash

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
export JRE_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
export CLASSPATH=.:/usr/irissys/dev/java/lib/1.8/*

echo "Building..."
cd /opt/irisapp
javac -classpath /usr/irissys/dev/java/lib/1.8/intersystems-jdbc-3.*.jar -d . java/ReadExcel.java
