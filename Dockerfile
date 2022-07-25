FROM openjdk:11-jdk
ARG JAR_FILE=https://github.com/Sim-mi-gyeong/dnd-7th-9-backend.git/build/libs/*.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]