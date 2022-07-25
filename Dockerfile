FROM openjdk:11-jdk
ARG JAR_FILE=dnd-7th-9-backend/build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]