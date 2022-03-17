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

Each Layer and Collector applications can be executed in multiple instance, the related kubernetes service redirects the load to each of them.
By the way of Zipkin server it is possible to show the resulting path of each external call.

##System startup
Into the K8s directory you will find all the required scrips. In details:
1) <b>build_*.sh</b> used to create jar, docker containers, and push them into K8s repository.
2) <b>start_*_level_system.sh</b> used to execute one of the schemes explained before.
3) <b>stop_*_level_system.sh</b> used to terminate one of the schemes explained before.
4) <b>*.yml</b> used to declare all the K8s resources required to run the related schema.


##System test
In order to test the system you can use one of the following:

* Execute a Post call to: <b>localhost:80/v1/sample</b>
with a simple json body like: <b>{ "sampleValue": "Mauxilium test" }</b>
* Use the Postman script (.json) stored into Tests directory to perform a simple single post.
* Use the JMeter script (.jmx) stored into Tests directory. To perform a group of calls by the way of treads and cicles. 



##Other info
You will find a more complete description of this project and the related results into the "<i>Programming</i>" section of my personal site:
www.mauxilium.it