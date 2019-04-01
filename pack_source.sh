#!/bin/zsh
zip source.zip `find src -name \*.java | grep -v "Test.java" | grep -v "Terminal.java"`
