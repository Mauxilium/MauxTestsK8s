FROM openjdk:11
CMD mkdir /usr/src/myapp
WORKDIR /usr/src/myapp
COPY ./target/LayerApp-1.0-SNAPSHOT.jar /usr/src/myapp
CMD ["java", \
     "-agentlib:jdwp=transport=dt_socket,address=*:50501,suspend=n,server=y", \
     "-Dspring.profiles.active=docker", \
     "-jar", \
     "LayerApp-1.0-SNAPSHOT.jar"]