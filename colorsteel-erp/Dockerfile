FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG JAR_FILE=target/colorsteel-erp-1.0.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
