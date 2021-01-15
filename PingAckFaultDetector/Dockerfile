FROM maven:3-jdk-8 as builder
WORKDIR /project
COPY PingAckFaultDetector .
#ADD ./target/pingackfd-0.0.1-SNAPSHOT.jar ./pingackfd.jar
RUN mvn package

FROM java:8-alpine
WORKDIR /app
COPY --from=builder /project/target/pingackfd-0.0.1-SNAPSHOT.jar ./pingackfd.jar
#ENTRYPOINT ["/bin/sh", "-c"]
CMD java -jar pingackfd.jar