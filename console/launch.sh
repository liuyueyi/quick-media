#!/usr/bin/env bash

mvn clean package spring-boot:repackage -Dmaven.test.skip=true

cd target
nohup java -server -Xms128m -Xmx128m -Xmn128m -XX:-OmitStackTraceInFastThrow -jar quick-media.jar > /dev/null 2>&1 &
echo $! 1> pid.log