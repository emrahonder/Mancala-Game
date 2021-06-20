## Build stage
# jdk 11 image
FROM maven:3.6-jdk-11 as builder


COPY src /tmp/mancala-app/src
COPY pom.xml /tmp/mancala-app/pom.xml
WORKDIR /tmp/mancala-app/

# Build a release artifact.
RUN mvn clean install

## Package stage
FROM adoptopenjdk/openjdk11:alpine-slim

# Copy the jar to the production image from the builder stage.
COPY --from=builder /tmp/mancala-app/target/mancala*.jar /mancala.jar

EXPOSE 8080

# Run the web service on container startup.
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/mancala.jar"]

