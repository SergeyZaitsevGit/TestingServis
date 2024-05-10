FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
RUN apk add maven
WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn clean package
RUN cp target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080