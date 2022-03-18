#! /bin/sh

cd ../LayerApp
mvn clean package
docker build -f DockerfileK8s -t layer:1.0.0 .

cd ../CollectorApp
mvn clean package
docker build -f DockerfileK8s -t collector:1.0.0 .


cd ../K8s
echo "Transferring Layer image to K8S"
docker save layer > layer.tar
microk8s ctr image import layer.tar
rm layer.tar

echo "Transferring Collector image to K8S"
docker save collector > collector.tar
microk8s ctr image import collector.tar
rm collector.tar

