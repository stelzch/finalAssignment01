#!/bin/zsh
cd src
rm **/*.class
javac edu/kit/informatik/**/*.java edu/kit/usxim/FinalAssignment1/{exceptions,}/*.java
jar -cfm dawngame.jar Manifest.txt **/*.class
mv dawngame.jar ..

