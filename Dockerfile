FROM openjdk:8-jdk-alpine
MAINTAINER baeldung.com
COPY target/pocket-puppy-school-jobs-0.0.1-SNAPSHOT.jar pocket-puppy-school-jobs-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/pocket-puppy-school-jobs-0.0.1-SNAPSHOT.jar"]