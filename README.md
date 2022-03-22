# MauxTestsK8s
Java Spring project showing how to works with Kubernetes

This project implements a simple multi layer microservices network used to test kubernetes load balancer and Pods communications.

In this project exists two kind of microservices:
1) <b>Layer</b>: receives a rest communication and propagate it to the next layer.
2) <b>Collector</b>: receives a rest communication and replay a message to the caller.

With theese two basic elements it is possible to compose a multi layer network, like the following schemes:

Two Layers:

    Caller -> Layer1 -> Collector

Three Layers:

    Caller -> Layer1 -> Layer2 -> Collector

Four Layers:

    Caller -> Layer1 -> Layer2 -> Layer3 -> Collector

Each Layer and Collector can be executed in multiple instances, the related kubernetes service redirects the load to each of them.
By the way of Zipkin server it is possible to show the resulting path of each external call.

<h2>System startup</h2>
Into the Docker and K8s directories you will find the scrips used to run the system using the related platform.
In details:
1) <b>run_1_\*_build.\*</b> are used to create the jars, the docker images, and push them into the K8s repository. The "fast" version of them do not execute the unit tests.
2) <b>run_2_startup_\*_levels.\*</b> are used to execute one of the schemes explained before.
3) <b>run_3_stop_\*_levels.\*</b> are used to terminate one of the schemes explained before.
4) <b>*.yml</b> are used to declare all the Docker/K8s resources required to run the related schema.

<b>Note</b>:
The file <b><i>docker_compose_2_levels.yml</i></b> exposes two ports (50501 and 50502) available to attach a remote debugger. Using this schema you could perform a debugging session of Layer connected to Collector.


<h2>System test</h2>
In order to test the system you can use one of the following:

* Use a browser load the Swagger page through the page: <b>localhost:80</b> (or <b>localhost:8081</b> if you are running the Docker version).
* Execute a Post call to: <b>localhost:80/api/v1/sample</b>
with a simple json body like: <b>{ "sampleValue": "Mauxilium test" }</b>
* Use the Postman script (.json) stored into Tests directory to perform a simple single post.
* Use the JMeter script (.jmx) stored into Tests directory. To perform a group of calls by the way of treads and cicles. 



<h2>Other info</h2>
You will find a more complete description of this project and the related results into the "<i>Programming</i>" section of my personal site:
www.mauxilium.it
