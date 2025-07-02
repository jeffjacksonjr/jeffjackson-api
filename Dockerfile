# Maven Build
FROM maven:latest AS build

COPY src /usr/src/app/src
COPY pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package

# Runtime image
FROM openjdk:17

COPY --from=build /usr/src/app/target/jeffjackson-1.1.jar /usr/app/jeffjackson-1.1.jar

EXPOSE 5151

CMD ["java", "-jar", "/usr/app/jeffjackson-1.1.jar"]
