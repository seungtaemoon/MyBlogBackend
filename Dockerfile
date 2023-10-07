#Docker File

#jdk11 Image Start
FROM openjdk:17-jdk
#LABEL authors="Administrator"
#인자 정리 - jar
ARG JAR_FILE=build/libs/*.jar
# jar File Copy
COPY ${JAR_FILE} MyBlogBackend-0.0.1-SNAPSHOT.jar
#build/libs/*.jar MyBlogBackend-0.0.1-SNAPSHOT.jar
#실행 명령어?
ENTRYPOINT ["java", "-jar", "/MyBlogBackend-0.0.1-SNAPSHOT.jar"]