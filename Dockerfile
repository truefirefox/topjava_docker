FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=target/topjava_docker-0.0.1-SNAPSHOT.jar
VOLUME /tmp
EXPOSE 8080
COPY ${JAR_FILE} topjavadocker.jar
ENTRYPOINT ["java", "-jar", "/topjavadocker.jar"]