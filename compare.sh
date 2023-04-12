#! /bin/bash

for BOT1 in $(cat bots.txt)
do
    for BOT2 in $(cat bots.txt)
    do 
        java -Xmx512m Tournament $BOT1 $BOT2 10000 >> results.txt
    done
done