FROM openjdk:21

VOLUME /tmp

COPY target/*.jar ChatService-0.0.1-SNAPSHOT.jar

EXPOSE 8877
ENTRYPOINT ["java","-jar","/ChatService-0.0.1-SNAPSHOT.jar"]