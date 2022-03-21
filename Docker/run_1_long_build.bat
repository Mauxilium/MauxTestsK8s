
cd ..\LayerApp
call mvn clean package
call docker build -f Dockerfile -t layer:1.0.0 .
cd ..\Docker

cd ..\CollectorApp
call mvn clean package
call docker build -f Dockerfile -t collector:1.0.0 .
cd ..\Docker

