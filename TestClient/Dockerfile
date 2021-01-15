FROM maven:3-jdk-8 as builder
WORKDIR /project
COPY TestClient .
#ADD ./target/testclient-0.0.1-SNAPSHOT.jar ./testclient.jar
RUN mvn package

FROM java:8-alpine
WORKDIR /app
COPY --from=builder /project/target/testclient-0.0.1-SNAPSHOT.jar ./testclient.jar
#ENTRYPOINT ["/bin/sh", "-c"]
CMD java -jar testclient.jar