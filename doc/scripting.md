# Embed in script

[Instroduction](..)
| [Source](source.md)
| [Features](features.md)
| [User manual](manual.md)
| **Scripting**
| [F.A.Q.](faq.md)

Jmxterm is not designed to hide JavaSE JMX library from a Java
program. However, with small effort, Jmxterm can be called
programmatically from scripting languages such as Perl or Shell.

To wrap JMX with objects easy to call from scripting languages such as
Groovy or Beanshell, there are other technologies designed to help in
this scenario. For example, Groovy JMX provides classes that wrap a
MBean and expose a nice API. Jmxterm itself does not provide scripting
features such as variable manipulation or flow-control keywords.

However, since Jmxterm commands come from standard input, modern
scripting environments that handle stdin nicely (such as Shell or
Perl) can easily combine scripting features with Jmxterm by using
input pipes. The following example is a small Perl segment that
connects to an MBean server and lists domains.

# Examples

## PERL

Since Perl supports pipe programming nicely, Jmxterm easily fits in
Perl programming.

```
# This Perl script opens a connection and calls domains
# $jar is the path of jmxterm uber jar file
open JMX, "| java -jar $jar -n";
print JMX "help \n";
my $host = "localhost";
my $port = 9991;
print JMX "open $host:$port\n";
print JMX "domains\n";
print JMX "close\n";
close JMX;
```

## SHELL

The following example calls Jmxterm from shell. This command is useful
when you want to get the PID of a JVM process that listens on a known
JMX port.

```
$ echo get -s -b java.lang:type=Runtime Name | \
java -jar target/jmxterm-1.0.2-uber.jar \
    -l localhost:9991 -v silent -n
11383@happycow
```
