FROM eclipse-temurin:23_37-jre-alpine

COPY target/jmxterm*-uber.jar /opt/jmxterm/jmxterm.jar

WORKDIR /opt/jmxterm

CMD ["java", "-jar", "jmxterm.jar"]
