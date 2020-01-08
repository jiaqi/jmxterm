package org.cyclopsgroup.jmxterm.io;

import java.io.IOException;

/**
 * Unimplemented version of command input
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class UnimplementedCommandInput extends CommandInput {
  @Override
  public String readLine() throws IOException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String readMaskedString(String prompt) throws IOException {
    throw new UnsupportedOperationException();
  }
}
