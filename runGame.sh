#!/bin/bash

./clean.sh
javac MyBot.java
javac RandomBot.java
./halite -d "10 10" "java MyBot" "java RandomBot" -s 1924727266
