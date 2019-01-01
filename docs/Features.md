## Standalone
The only runtime dependency of Jmxterm is JDK. The internal third party dependencies are wrapped by ready-to-run uberjar. User does not need to worry about the setup of dependencies.

Wrapped by uberjar, Jmxterm runs out of box with `java -jar jmxterm-<VERSION>-uber.jar` command. No configuration, directory, environment variable or file permissions need to be setup ahead of time.

## Platform neutral
Jmxterm implementation is platform independent like most Java applications. However some features rely on [JLine](http://jline.sourceforge.net/) which has native implementation that supports major operating systems including Windows, Mac OS and most Linux distros.

## Interactive
Unlike [cmdline-jmxclient](http://crawler.archive.org/cmdline-jmxclient/), Jmxterm runs interactively. User can do a series of short operations in an interactive command line console and system is aware of context.

## Self documented
Every command is self documented with `--help` option.

## Connect a local PID
Jmxterm is aware of JVM processes running on localhost from the same run as user. It is able to connect these local processes without requiring process's MBean server to listen to a port.

## Password authentication
Jmxterm supports password authentication.

## Embeddable
Jmxterm itself does not support flow control commands but it can be smoothly embedded into scripting languages such as PERL or SHELL for complicated logic.

## Auto completion
Jmxterm interactive command line console not only auto-complete commands, options with tab key, but also name of MBean domain and MBeans.

## Command history
Powered by [JLine](http://jline.sourceforge.net/) , command history can be browsed with up/down arrow keys.

## Reporting and active watch
Jmxterm supports "watch" function that repeatedly prints out value of MBean attributes. With customizable output format, the "watch" feature can easily be extended for report generation.

## RPM distribution
With support of [Maven RPM plugin](http://mojo.codehaus.org/rpm-maven-plugin) , Jmxterm can be built into RPM package bundle in addition to uberjar. DEB support is on its way.
