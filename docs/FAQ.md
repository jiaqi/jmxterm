### Q. Error of .jmxterm_history
The latest Jmxterm code in git head did a major dependency update, especially an upgrade to JLine 3. If you see Jmxterm fails to start because of .jmxterm_history, remove your previous history file using (Unix)

```
rm ~/.jmxterm_history
```

or (Windows)

```
del %HOMEPATH%\.jmxterm_history
```

### Q. Why jvms command doesn't work? What is it?
A. "jvm" is a command that lists all running JVM processes on host under same run as user. This feature is not official exposed by JavaSE therefore the implementation is specific to the version of JDK and OS. It depends on JDK library which is not available in JRE. Please don't be surprised if it doesn't work on your OS or JDK version, but do let me know when it fails. Sorry for inconvenience.

### Q. Something is failing but I don't see the details, how to turn on debugging?
A. In Jmxterm, run command option -v verbose to turn on debugging.

### Q. How to query a bean where name contains white space?
A. Use back slash to escape any white space or special characters in command. For example

```
$> domain java.lang
#domain is set to java.lang
$> bean name=PS\ Survivor\ Space,type=MemoryPool
#bean is set to java.lang:name=PS Survivor Space,type=MemoryPool
```

### Q. How to pass a null as a value?

A. Special keyword "null" is reserved to stand for null pointer. If you do want to pass a string "null", wrap value with double quots.

```
$>set anAttribute null # Sets anAttribute to be null pointer
$>set anAttribute "null" # Sets anAttribute to be string "null"
```

### Q. Build Java application on top of Jmxterm
A: This topic has been discussed for several times. It's not Jmxterm's intention to hide JMX API in JavaSE. If you programmatically call Jmxterm in Java in order to make calls to MBean server you'd find many important features missing. To interact MBean server in Java programming, the original JMX API in JavaSE is still the best choice.