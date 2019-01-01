## Start command line session

Jmxterm tends to be self-documented at any given time. Usually -h option shows all the information you need. Please try -h to get accurate manual of the version that you have.

```
jiaqi@happycow:~$ java -jar jmxterm-1.0.0-uber.jar -h
usage: jmxterm [-e] [-h] [-i <val>] [-l <val>] [-n] [-o <val>] [-p <val>] [-u
     <val>] [-v <val>]
Main executable of JMX terminal CLI tool
-e,--exitonfailure   With this flag, terminal exits for any Exception
-h,--help            Show usage of this command line
-i,--input <val>     Input script file. There can only be one input file.
                   "stdin" is the default value which means console input
-l,--url <val>       Location of MBean service. It can be <host>:<port> or
                   full service URL.
-n,--noninteract     Non interactive mode. Use this mode if input doesn't come
                   from human or jmxterm is embedded
-o,--output <val>    Output file, stdout or stderr. Default value is stdout
-p,--password <val>  Password for user/password authentication
-u,--user <val>      User name for user/password authentication
-v,--verbose <val>   Verbose level, could be silent|brief|verbose. Default
                   value is brief
Without any option, this command opens an interactive command line based
console. With a given input file, commands in file will be executed and process
ends after file is processed
```

The executable uberjar can be used as:

* Interactive console - Without any option, it opens an interactive command line console.
* File interpreter - With -i option, it processes a given script file and exit in the end.
* Embedded - It can be called from scripting language such asSHELL or PERL. Read scripting for more details.

## Available commands

Once the console is open, all available commands and options are self explained with help command. For example:

```
jiaqi@rattlesnake:~$ java -jar jmxterm-1.0.0-uber.jar
Welcome to JMX terminal. Type "help" for available commands.
?$ help
following commands are available to use:
about    - Display about page
bean     - Display or set current selected MBean.
beans    - List available beans under a domain or all domains
bye      - Terminate console and exit
close    - Close current JMX connection
domain   - Display or set current selected domain.
domains  - List all available domain names
exit     - Terminate console and exit
get      - Get value of MBean attribute(s)
help     - Display available commands or usage of a command
info     - Display detail information about an MBean
jvms     - List all running local JVM processes
open     - Open JMX session or display current connection
option   - Set options for command session
quit     - Terminate console and exit
run      - Invoke an MBean operation
set      - Set value of an MBean attribute
?$ help get
usage: get [-b <val>] [-d <val>] [-h] [-i] [-q] [-s]
Get value of MBean attribute(s)
  -b,--bean <val>    MBean name where the attribute is. Optional if bean has
                   been set
  -d,--domain <val>  Domain of bean, optional
  -h,--help          Display usage
  -i,--info          Show detail information of each attribute
  -q,--quots         Quotation marks around value
  -s,--simple        Print simple expression of value without full expression
* stands for all attributes. eg. get Attribute1 Attribute2 or get *
```