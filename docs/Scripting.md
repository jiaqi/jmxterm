## Embed in script language

Jmxterm is not designed to hide JavaSE JMX liberary from a Java program. But with small effort, Jmxterm can be called in programmatic manner from script language such as PERL or Shell.

To wrap JMX with objects easy to call by scripting language such as Groovy or beanshell, there are other technologies designed to help such scenario. For example [Groovy JMX](http://groovy.codehaus.org/Groovy+and+JMX) provides some classes that wraps an MBean and expose nice API. JMXTERM itself doesn't provide scripting features such as variable manipulation or flow control keywords.

However since the commands in JMXTERM comes from standard input, modern script environment that handles stdin nicely, such as SHELL or PERL, can easily combine scripting feature with JMXTERM by using input pipe. Following example is a small PERL segment that connects to an MBean server and list domains.

## Example with PERL

Since PERL supports pipe programming nicely, Jmxterm easily fits in PERL programming.

```perl
# This Perl script open connection and call domains
# $jar stands for path of jmxterm jar file
open JMX, "| java -jar $jar -n";
print JMX "help \n";
my $host = "localhost";
my $port = 9991;
print JMX "open $host:$port\n";
print JMX "domains\n";
print JMX "close\n";
close JMX;
```

## Example with SHELL

Following example is to call JMXTERM from shell. This particular command is useful when you want to get the PID of JVM process that listens to a known JMX port.

```
$ echo get -s -b java.lang:type=Runtime Name | \
java -jar target/jmxterm-1.0.0-uber.jar \
    -l localhost:9991 -v silent -n
11383@happycow
```