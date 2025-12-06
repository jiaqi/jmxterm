# Introduction

Jmxterm is an open source, command line based interactive JMX client
written in Java. It lets users access a Java MBean server from a
command line console without a graphical environment. In other words,
it's a command line alternative to `jconsole`. JMXTerm relies on the
`jconsole` library at runtime.

JMXTerm can also be embedded in non-Java programming languages such as
Perl, Shell, and Python, allowing these languages to access a Java
MBean server programmatically.

Please feel free to file issues on GitHub if you have questions.

## References

- [Maven generated reports](https://s3.us-east-1.amazonaws.com/dist.cyclopsgroup.org/projects/index.html)
- [Javadoc](https://s3.us-east-1.amazonaws.com/dist.cyclopsgroup.org/projects/jmxterm/apidocs/index.html)

## Download

Find the latest release on the right side of the GitHub page.

The uberjar file is an executable file. Run it with:

```
java -jar jmxterm-1.0.4-uber.jar
```

## 1-minute tutorial

The downloaded UBER jar runs out of the box with `java -jar`. There are
no dependencies to install, and no configuration, file structure,
permission, or log directory to set up.

Just download and run `java -jar jmxterm-<VERSION>-uber.jar` without any
options. That opens an interactive command line console where commands
are self-explained.

```
jiaqi@happycow:~$ java -jar jmxterm-1.0.4-uber.jar

Welcome to JMX terminal. Type "help" for available commands.

?$
```

Once you are in the console, the `help` command shows all available
commands. Asking for help on a specific command (for example,
`help get`), or using the `-h` option, shows usage for that command.
For example:

```
> help get
usage: get [-b <val>] [-d <val>] [-h] [-i] [-q] [-s]
Get value of MBean attribute(s)
-b,--bean <val>    MBean name where the attribute is. Optional if bean
                   has been set
-d,--domain <val>  Domain of bean, optional
-h,--help          Display usage
-i,--info          Show detail information of each attribute
-q,--quots         Quotation marks around value
-s,--simple        Print simple expression of value without full
                   expression
* stands for all attributes. e.g. get Attribute1 Attribute2 or get *
```

Jmxterm can do a lot more. Check out the
[user manual](doc/user_manual.md) for more complicated usage.
