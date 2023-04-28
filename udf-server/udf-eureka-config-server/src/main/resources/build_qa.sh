#!/usr/bin/env bash

base_path="udf-server"

echo 'shutdown application begin'
returnCode=`curl -I -s -X POST "http://127.0.0.1:${management.port}/shutdown"|head -1|cut -d" " -f2`
echo "shutdown status code : $returnCode"
sleep 5
echo 'shutdown application end'

echo 'stop application begin'
  PID=$(ps -ef | grep ${project.name}-${project.version}.jar | grep -v grep | awk '{ print $2 }')
  if [ -z "$PID" ]
  then
      echo 'application is already stopped'
  else
      echo kill $PID
      kill -9 $PID
  fi
  rm -rf /${base_path}/${project.name}-${project.version}.jar
echo 'stop application end'

echo 'clean copy application begin'
rm -rf /${base_path}/${project.name}-${project.version}.jar
cp ../${project.name}-${project.version}.jar /${base_path}/${project.name}-${project.version}.jar
echo 'copy application end'

echo 'start application begin'
nohup java -server -Xms256m -Xmx256m -XX:+UseG1GC -jar /${base_path}/${project.name}-${project.version}.jar --spring.profiles.active=qa &
echo 'start application end'

echo 'sleep 60ms begin'
sleep 60
echo 'sleep 60ms end'

echo 'check service begin'
returnCode=`curl -I -s "http://127.0.0.1:${management.port}/info"|head -1|cut -d" " -f2`
if [ "$returnCode" == "200" ]
then
	echo "service has started";
else
	echo "service has error or start time out of 60ms";
	exit 1;
fi
echo 'check service end'