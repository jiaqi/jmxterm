The downloaded UBER jar runs out of box with `java -jar` command. Yes, there's no dependency, installation, user configuration, file structure, permission or log directory to setup.

Just Download And Run `java -jar jmxterm-<VERSION>-uber.jar` without any option opens an interactive command line console where commands are self-explained. You will never get lost.

```
jiaqi@happycow:~$ java -jar jmxterm-1.0.0-uber.jar
Welcome to JMX terminal. Type "help" for available commands.
?$
Once you are in the console, help command shows all available commands and help with command name or command with -h option shows the usage of each command. For example

> help get
usage: get [-b <val>] [-d <val>] [-h] [-i] [-q] [-s]
Get value of MBean attribute(s)
-b,--bean <val> MBean name where the attribute is. Optional if bean has
been set
-d,--domain <val> Domain of bean, optional
-h,--help Display usage
-i,--info Show detail information of each attribute
-q,--quots Quotation marks around value
-s,--simple Print simple expression of value without full expression
* stands for all attributes. eg. get Attribute1 Attribute2 or get *
```

Jmxterm can do a lot more. Check out [[UserManual]] for more complicated usage.