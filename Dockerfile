FROM eclipse-temurin:24-jre-alpine

RUN apk upgrade --no-cache

COPY target/jmxterm*-uber.jar /opt/jmxterm/jmxterm.jar

WORKDIR /opt/jmxterm

CMD ["java", "-jar", "jmxterm.jar"]
