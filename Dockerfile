FROM openjdk:17-alpine
MAINTAINER nrpndr
WORKDIR /app/review-service
EXPOSE 9014
ARG JAR_FILE=target/review-service.jar
COPY ${JAR_FILE} review-service.jar
ENTRYPOINT ["java","-jar","review-service.jar"]