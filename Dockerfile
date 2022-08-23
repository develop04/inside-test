FROM openjdk:17
COPY target/inside-test-0.0.1-SNAPSHOT.jar inside-test-docker.jar
ENTRYPOINT ["java","-jar","inside-test-docker.jar"]