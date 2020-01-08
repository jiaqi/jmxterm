package org.cyclopsgroup.jmxterm.io;

/**
 * General abstract class to output message and values
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class CommandOutput {
  /** Close the output; */
  public void close() {}

  /**
   * Print out value to output without line break
   *
   * @param output Value to print out
   */
  public abstract void print(String output);

  /** @param e Error to print out */
  public abstract void printError(Throwable e);

  /**
   * Print out value to output as standalone line
   *
   * @param output Value to print out
   */
  public void println(String output) {
    print(output);
    print(System.lineSeparator());
  }

  /**
   * Print message to non-standard console for human to read. New line is always appended
   *
   * @param message Message to print out.
   */
  public abstract void printMessage(String message);
}
