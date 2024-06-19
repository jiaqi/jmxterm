FROM eclipse-temurin:21.0.3_9-jre-alpine

COPY target/jmxterm*-uber.jar /opt/jmxterm/jmxterm.jar

WORKDIR /opt/jmxterm

CMD ["java", "-jar", "jmxterm.jar"]
