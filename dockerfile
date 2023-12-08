FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ./target/student-record-book-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]