#! /bin/sh

cd ../LayerApp
mvn clean package -DskipTests
docker build -f Dockerfile -t layer:1.0.0 .
cd ../Docker

cd ../CollectorApp
mvn clean package -DskipTests
docker build -f Dockerfile -t collector:1.0.0 .
cd ../Docker

