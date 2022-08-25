FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} develop04/inside-test-docker.jar
ENTRYPOINT ["java","-jar","/develop04/inside-test-docker.jar"]