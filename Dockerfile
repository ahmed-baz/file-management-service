FROM openjdk:17-alpine
LABEL maintainer="ahmed.baz@fawry.com"
WORKDIR /usr/local/bin/
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} file-system-1.0.jar
EXPOSE 9999
CMD ["java","-jar","file-system.jar"]
ENTRYPOINT ["java","-jar","file-system-1.0.jar"]
