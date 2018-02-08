# JMXTerm

An open source command line based interactive JMX client written in Java. It allows user to access a Java MBean server
in command line console without graphical environment. In another word, it's a command line based jconsole.
At runtime, JMXTerm implementation relies on JDK jconsole library while it doesn't require graphical environment 
(such as X in Linux).

JMXTerm can also be used to integrate with non-Java programming language such as PERL or SHELL and allow these languages
to access Java MBean server programmatically. However as this page will point out later, there's no point of using
JMXTerm to access MBean server programmatically in Java, as Java already provides good API.

Read more at http://wiki.cyclopsgroup.org/jmxterm

## Building

Use Maven to build the code, test it and create a runnable JAR or installable Debian package

    mvn package

## Running

An uber JAR containing every dependency is automatically created and can be run
directly:

    java -jar target/jmxterm-2.0.0-uber.jar

## Installation

Installable packages will be created for Debian based operating systems, other
systems require a manual installation.

### Debian / Ubuntu

    sudo dpkg -i target/jmxterm_2.0.0_all.deb

### Other Linux operating systems

    sudo mkdir /usr/share/jmxterm
    sudo cp target/jmxterm-2.0.0-uber.jar /usr/share/jmxterm/jmxterm-uber.jar
    sudo cp src/main/script/jmxterm.sh /usr/bin/jmxterm
    sudo chmod +x /usr/bin/jmxterm

## Release Notes

### Version 2.0.0

We did a major dependency update in version 2.0.0, especially an upgrade to JLine 3. With version 2.0.0 you need to
remove your previous history file using (Unix)

    rm ~/.jmxterm_history
    
or (Windows)

    del %HOMEPATH%\.jmxterm_history

