#!/usr/bin/env bash

echo 'shutdown application begin'
    returnCode=`curl -I -s -X POST "http://127.0.0.1:${management.port}/actuator/shutdown"|head -1|cut -d" " -f2`
echo "shutdown status code : $returnCode"
    sleep 5
echo 'shutdown application end'

echo 'stop application begin'
  PID=$(ps -ef | grep ${project.name}-${project.version}.jar | grep -v grep | awk '{ print $2 }')
  if [[ -z "$PID" ]]
  then
      echo 'application is already stopped'
  else
      echo 'kill process'
      kill -9 $PID
  fi
echo 'stop application end'

echo 'bak and copy application begin'
    datestr=$(date +%Y_%m_%d_%H_%M_%S)
    mv /usr/local/share/app/${project.name}/${project.name}-${project.version}.jar /usr/local/share/app/${project.name}/${project.name}-${project.version}.jar.${datestr}
    cp ${project.name}-${project.version}.jar /usr/local/share/app/${project.name}/${project.name}-${project.version}.jar
echo 'bak and copy application end'

echo 'start application begin'
    rm -rf nohup.out
    nohup java -server -Xms1024m -Xmx1024m -XX:+UseG1GC -jar /usr/local/share/app/${project.name}/${project.name}-${project.version}.jar --spring.profiles.active=prod &
echo 'start application end'

echo 'check service begin'
    echo 'sleep 10s begin'
        sleep 10
    echo 'sleep 10s end'
    startedStatus="0";
    maxCount=40;
    for (( i = 0; i < $maxCount; i++ ));do
        returnCode=`curl -I -s "http://127.0.0.1:${management.port}/actuator/info"|head -1|cut -d" " -f2`
        if [[ "$returnCode" == "200" ]]
        then
            startedStatus="1";
            break;
        else
            echo "sleep 2s,count:$i";
            sleep 2
        fi
    done
    if [[ "$startedStatus" == "1" ]]
    then
        echo "service has started";
    else
        echo "service start fail";
        exit 1;
    fi
echo 'check service end'