package org.cyclopsgroup.jmxterm.io;

import java.io.Closeable;
import java.io.IOException;

/**
 * An abstract class that provides command line input line by line
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class CommandInput implements Closeable {
  @Override
  public void close() throws IOException {}

  /**
   * Reads a single line from input.
   *
   * @return The line it reads.
   * @throws IOException allows any communication error.
   */
  public abstract String readLine() throws IOException;

  /**
   * Reads input without echoing back keyboard input.
   *
   * @param prompt The full or partial input that user types.
   * @return The string it reads.
   * @throws IOException allows any communication error.
   */
  public abstract String readMaskedString(String prompt) throws IOException;
}
