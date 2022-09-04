FROM arm64v8/openjdk:8
COPY target/pocketpuppyschooljobs-0.0.1-SNAPSHOT.jar /usr/bin/pocketpuppyschooljobs.jar
WORKDIR /usr/bin
EXPOSE 8080/tcp
CMD ["java", "-jar", "pocketpuppyschooljobs.jar"]