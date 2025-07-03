#Maven Build
FROM maven:latest AS build

# Define ALL environment variables here
ARG MONGO_URI
ARG SPRING_MAIL_USERNAME
ARG SPRING_MAIL_PASSWORD
ARG SPRING_THYMELEAF_CACHE
ARG CLOUDINARY_CLOUD_NAME
ARG CLOUDINARY_API_KEY
ARG CLOUDINARY_API_SECRET

# Set environment variables
ENV MONGO_URI=$MONGO_URI \
    SPRING_MAIL_USERNAME=$SPRING_MAIL_USERNAME \
    SPRING_MAIL_PASSWORD=$SPRING_MAIL_PASSWORD \
    SPRING_THYMELEAF_CACHE=$SPRING_THYMELEAF_CACHE \
    CLOUDINARY_CLOUD_NAME=$CLOUDINARY_CLOUD_NAME \
    CLOUDINARY_API_KEY=$CLOUDINARY_API_KEY \
    CLOUDINARY_API_SECRET=$CLOUDINARY_API_SECRET

COPY src /usr/src/app/src

COPY pom.xml /usr/src/app

RUN mvn -f /usr/src/app/pom.xml clean package

FROM openjdk:17

COPY --from=build /usr/src/app/target/jeffjackson-1.1.jar /usr/app/jeffjackson-1.1.jar

EXPOSE 5151

CMD ["java", "-jar", "/usr/app/jeffjackson-1.1.jar"]