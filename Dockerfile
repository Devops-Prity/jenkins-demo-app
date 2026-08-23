FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/jenkins-demo-app-1.0.0.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-Xmx192m", "-Dserver.port=8081", "-jar", "app.jar"]
