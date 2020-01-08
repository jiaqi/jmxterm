package org.cyclopsgroup.jmxterm.io;

import java.io.IOException;

/**
 * An abstract class that provides command line input line by line
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class CommandInput {
  /** Closes and releases relevant resources. */
  public void close() throws IOException {}

  /** Reads a single line from linput. */
  public abstract String readLine() throws IOException;

  /** Reads input without echo'ing back keyboard input. */
  public abstract String readMaskedString(String prompt) throws IOException;
}
