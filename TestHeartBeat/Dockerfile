FROM maven:3-jdk-8 as builder
WORKDIR /project
COPY TestHeartBeat .
#ADD ./target/testheartbeat-0.0.1-SNAPSHOT.jar ./testheartbeat.jar
RUN mvn package

FROM java:8-alpine
WORKDIR /app
COPY --from=builder /project/target/testheartbeat-0.0.1-SNAPSHOT.jar ./testheartbeat.jar
#ENTRYPOINT ["/bin/sh", "-c"]
CMD java -jar testheartbeat.jar