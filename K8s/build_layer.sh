#! /bin/sh

cd ../LayerApp
mvn clean package
docker build -f DockerfileK8s -t layer:1.0.0 .
cd ../K8s

echo "Transferring Layer image to K8S"
docker save layer > layer.tar
microk8s ctr image import layer.tar
rm layer.tar
