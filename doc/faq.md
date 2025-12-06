# Frequently asked questions

[Instroduction](../readme.md)
| [Source](source.md)
| [Features](features.md)
| [User manual](manual.md)
| [Scripting](scripting.md)
| **F.A.Q.**

## Q. Error of .jmxterm_history

The latest Jmxterm code in git head included a major dependency
update, especially an upgrade to JLine 3. If Jmxterm fails to start
because of `.jmxterm_history`, remove your previous history file using
(Unix):

`rm ~/.jmxterm_history`

or (Windows):

`del %HOMEPATH%\.jmxterm_history`

## Q. Why jvms command doesn't work? What is it?

The `jvms` command lists all running JVM processes under the same user.
This feature is not officially exposed by JavaSE, so the implementation
is specific to the JDK version and OS. It depends on a JDK library that
is not available in JRE. Please don't be surprised if it doesn't work
on your OS or JDK version, but do let me know when it fails. Sorry for
the inconvenience.

## Q. Something is failing but I don't see the details, how to turn on
debugging?

In Jmxterm, run the command `option -v verbose` to turn on debugging.

## Q. How to query a bean where name contains white space?

Use a backslash to escape any white space or special characters in the
command. For example:

```
$> domain java.lang
#domain is set to java.lang
$> bean name=PS\ Survivor\ Space,type=MemoryPool
#bean is set to java.lang:name=PS Survivor Space,type=MemoryPool
```

## Q. How to pass a null as a value?

The special keyword "null" is reserved to represent a null pointer. If
you do want to pass a string "null", wrap the value with double quotes.

```
$>set anAttribute null # Sets anAttribute to be null pointer
$>set anAttribute "null" # Sets anAttribute to be string "null"
```

## Q. Build Java application on top of Jmxterm

This topic has been discussed several times. Jmxterm does not intend to
hide the JMX API from JavaSE. If you programmatically call Jmxterm in
Java to make calls to an MBean server, you will find many important
features missing. To interact with an MBean server in Java programming,
the original JMX API in JavaSE is still the best choice.
