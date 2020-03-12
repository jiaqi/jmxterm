FROM openjdk:8

COPY . /tmp/jmxterm

RUN curl -sSL -o /tmp/apache-maven-3.6.3-bin.tar.gz https://downloads.apache.org/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz && \
    tar zxvf /tmp/apache-maven-3.6.3-bin.tar.gz -C /tmp && \
    cd /tmp/jmxterm && \
    /tmp/apache-maven-3.6.3/bin/mvn install && \
    mkdir /opt/jmxterm && \
    cp target/jmxterm-`cat target/maven-archiver/pom.properties | grep version | cut -f 2 -d =`-uber.jar /opt/jmxterm/jmxterm.jar && \
    cd /opt/jmxterm && \
    rm -rf /tmp/apache-maven-3.6.3* /tmp/jmxterm 

WORKDIR /opt/jmxterm

CMD ["java", "-jar", "jmxterm.jar"]
