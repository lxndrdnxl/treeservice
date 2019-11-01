FROM maven:3.6-jdk-8 as build
COPY src /usr/src/treeservice/src
COPY pom.xml /usr/src/treeservice
RUN mvn -f /usr/src/treeservice/pom.xml clean package -Dmaven.test.skip package

FROM gcr.io/distroless/java:8
COPY --from=build /usr/src/treeservice/target/treeservice-0.0.1-SNAPSHOT.jar /usr/treeservice/treeservice-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/usr/treeservice/treeservice-0.0.1-SNAPSHOT.jar" ]