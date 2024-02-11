#!/bin/zsh

doforward() {
  service=$1
  port=$2
  remotePort=$3
  echo $service $port
  microk8s kubectl port-forward -n default service/$service ${port}:${remotePort} &
  echo $! >port-forward-${service}.pid
}

doforward 'kafka-broker' 9092 9092
doforward 'kafka-zk' 2181 2181
doforward 'cr-mysql' 3306 3306
doforward 'uaa' 8080 8080
exit 0
