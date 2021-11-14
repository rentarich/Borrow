FROM maven:3.6.3-openjdk-15 AS build

# build all dependencies for offline use
#COPY ./pom.xml ./pom.xml
#COPY ./entities/pom.xml ./entities/pom.xml
#COPY ./api/pom.xml ./api/pom.xml
#COPY ./services/pom.xml ./services/pom.xml
#RUN mvn dependency:go-offline -B

COPY ./ /app
WORKDIR /app

# Downloading dependencies so they are not downloaded with every build (lower layer than clean package)
#RUN mvn dependency:go-offline -B

RUN mvn --show-version --update-snapshots --batch-mode clean package

FROM adoptopenjdk:15-jre-hotspot
RUN mkdir /app
WORKDIR /app
COPY --from=build ./app/api/target/api-1.0.0-SNAPSHOT.jar /app
EXPOSE 5556
CMD ["java", "-jar", "api-1.0.0-SNAPSHOT.jar"]