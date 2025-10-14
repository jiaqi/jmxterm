package org.cyclopsgroup.jmxterm.boot;

import java.io.File;
import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;

/**
 * Options for main class
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli(
    name = "jmxterm",
    description = "Main executable of JMX terminal CLI tool",
    note =
        "Without any option, this command opens an interactive command line based console. With a given input file, commands in file will be executed and process ends after file is processed")
public class CliMainOptions {
  /** Constant <code>stderr</code> that identifies standard error output */
  public static final String STDERR = "stderr";

  /** Constant <code>stdin</code> that identifies standard input */
  public static final String STDIN = "stdin";

  /** Constant <code>stdout</code> that identifies standard output */
  public static final String STDOUT = "stdout";

  private boolean exitOnFailure;

  private boolean help;

  private String input = STDIN;

  private boolean nonInteractive;

  private boolean appendToOutput;

  private String output = STDOUT;

  private String password;

  private String url;

  private String user;

  private String verboseLevel;

  private boolean isSecureRmiRegistry;

  /** @return #setInput(String) */
  public final String getInput() {
    return input;
  }

  /** @return #setOutput(String) */
  public final String getOutput() {
    return output;
  }

  /** @return Password for user/password authentication */
  public final String getPassword() {
    return password;
  }

  /** @return #setUrl(String) */
  public final String getUrl() {
    return url;
  }

  /** @return User name for user/password authentication */
  public final String getUser() {
    return user;
  }

  /** @return Verbose option */
  public final String getVerboseLevel() {
    return verboseLevel;
  }

  /** @return True if terminal exits on any failure */
  public final boolean isExitOnFailure() {
    return exitOnFailure;
  }

  /** @return True if terminal exits on any failure */
  public final boolean isAppendToOutput() {
    return appendToOutput;
  }

  /** @return {@link #setHelp(boolean)} */
  public final boolean isHelp() {
    return help;
  }

  /** @return True if CLI runs without user interaction, such as piped input */
  public final boolean isNonInteractive() {
    return nonInteractive;
  }

  /**
   * @return True if the server's RMI registry is protected with SSL/TLS
   *     (com.sun.management.jmxremote.registry.ssl=true)
   */
  public final boolean isSecureRmiRegistry() {
    return isSecureRmiRegistry;
  }

  /** @param exitOnFailure True if terminal exits on any failure */
  @Option(
      name = "e",
      longName = "exitonfailure",
      description = "With this flag, terminal exits for any Exception")
  public final void setExitOnFailure(boolean exitOnFailure) {
    this.exitOnFailure = exitOnFailure;
  }

  /** @param help True to turn on <code>help</code> flag */
  @Option(name = "h", longName = "help", description = "Show usage of this command line")
  public final void setHelp(boolean help) {
    this.help = help;
  }

  /** @param file Input script path or <code>stdin</code> as default value for console input */
  @Option(
      name = "i",
      longName = "input",
      description =
          "Input script file. There can only be one input file. \"stdin\" is the default value which means console input")
  public final void setInput(String file) {
    Validate.notNull(file, "Input file can't be NULL");
    Validate.isTrue(new File(file).isFile(), "File " + file + " doesn't exist");
    this.input = file;
  }

  /** @param nonInteractive True if CLI runs without user interaction, such as piped input */
  @Option(
      name = "n",
      longName = "noninteract",
      description =
          "Non interactive mode. Use this mode if input doesn't come from human or jmxterm is embedded")
  public final void setNonInteractive(boolean nonInteractive) {
    this.nonInteractive = nonInteractive;
  }

  /** @param outputFile It can be a file or {@link #STDERR} or {@link #STDERR} */
  @Option(
      name = "o",
      longName = "output",
      description = "Output file, stdout or stderr. Default value is stdout")
  public final void setOutput(String outputFile) {
    Validate.notNull(outputFile, "Output file can't be NULL");
    this.output = outputFile;
  }

  /** @param password Password for user/password authentication */
  @Option(
      name = "p",
      longName = "password",
      description = "Password for user/password authentication")
  public final void setPassword(String password) {
    Validate.notNull(password, "Password can't be NULL");
    this.password = password;
  }

  /** @param url MBean server URL */
  @Option(
      name = "l",
      longName = "url",
      description = "Location of MBean service. It can be <host>:<port> or full service URL.")
  public final void setUrl(String url) {
    Validate.notNull(url, "URL can't be NULL");
    this.url = url;
  }

  /** @param user User name for user/password authentication */
  @Option(name = "u", longName = "user", description = "User name for user/password authentication")
  public final void setUser(String user) {
    Validate.notNull(user, "User can't be NULL");
    this.user = user;
  }

  /** @param verboseLevel Verbose level */
  @Option(
      name = "v",
      longName = "verbose",
      description = "Verbose level, could be silent|brief|verbose. Default value is brief")
  public final void setVerboseLevel(String verboseLevel) {
    this.verboseLevel = verboseLevel;
  }

  /** @param appendToOutput True if outputfile is preserved */
  @Option(
      name = "a",
      longName = "appendtooutput",
      description = "With this flag, the outputfile is preserved and content is appended to it")
  public final void setAppendToOutput(boolean appendToOutput) {
    this.appendToOutput = appendToOutput;
  }

  /**
   * @param isSecureRmiRegistry Whether the server's RMI registry is protected with SSL/TLS
   *     (com.sun.management.jmxremote.registry.ssl=true)
   */
  @Option(
      name = "s",
      longName = "sslrmiregistry",
      description = "Whether the server's RMI registry is protected with SSL/TLS")
  public final void setSecureRmiRegistry(final boolean isSecureRmiRegistry) {
    this.isSecureRmiRegistry = isSecureRmiRegistry;
  }
}
