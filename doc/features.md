# Features

[Instroduction](..)
| [Source](source.md)
| **Features**
| [User manual](manual.md)
| [Scripting](scripting.md)
| [F.A.Q.](faq.md)

## Ease of use

### Standalone

The only runtime dependency of Jmxterm is the JDK. Internal third
party dependencies are wrapped in a ready-to-run uberjar. Users do
not need to worry about setting up dependencies.

Wrapped as an uberjar, Jmxterm runs out of the box with
`java -jar jmxterm-<VERSION>-uber.jar`. No configuration, directories,
environment variables, or file permissions need to be set up ahead of
time.

### Platform neutral

Jmxterm is platform independent like most Java applications. However,
some features rely on JLine, which includes native bits that support
major operating systems, including Windows, macOS, and most Linux
distributions.

### Interactive

Unlike `cmdline-jmxclient`, Jmxterm runs interactively. Users can do a
series of short operations in an interactive command-line console, and
the system is aware of context.

### Self documented

Every command is self-documented via the `--help` option.

### Embeddable

Jmxterm itself does not implement flow-control commands, but it can be
embedded into scripting languages such as Perl or Shell to support more
complicated logic.

### Auto completion

The interactive console supports tab completion for commands and
options, and can also complete MBean domain names and MBean names.

### Command history

Powered by JLine, command history can be browsed with the up and down
arrow keys.

## Functionality

### Connect a local PID

Jmxterm is aware of JVM processes running on localhost that share the
same user. It can connect to these local processes without requiring
the process's MBean server to listen on a network port.

### Password authentication

Jmxterm supports password authentication.

### Reporting and active watch

Jmxterm supports a "watch" function that repeatedly prints values of
MBean attributes. With a customizable output format, the watch feature
can be extended for report generation.

### RPM distribution

With support from the Maven RPM plugin, Jmxterm can be built as an RPM
package in addition to the uberjar. DEB support is on the way.
```
