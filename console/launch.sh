#!/usr/bin/env bash

mvn clean package spring-boot:repackage -Dmaven.test.skip=true

# 杀掉之前的进程
cat pid.log| xargs -I {} kill {}
mv quick-media.jar media.bk.jar

mv target/quick-media.jar ./
nohup java -server -Xms128m -Xmx128m -Xmn128m -XX:-OmitStackTraceInFastThrow -jar quick-media.jar > /dev/null 2>&1 &
echo $! 1> pid.log