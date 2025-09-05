FROM eclipse-temurin:24-jre-alpine

COPY target/jmxterm*-uber.jar /opt/jmxterm/jmxterm.jar

WORKDIR /opt/jmxterm

CMD ["java", "-jar", "jmxterm.jar"]
