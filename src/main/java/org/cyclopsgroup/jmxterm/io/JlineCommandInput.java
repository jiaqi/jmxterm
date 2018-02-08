package org.cyclopsgroup.jmxterm.io;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jline.reader.impl.LineReaderImpl;

import java.io.IOException;

/**
 * Implementation of input that reads command from jloin console input
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class JlineCommandInput extends CommandInput {
  private final LineReaderImpl console;

  private final String prompt;

  /**
   * @param console Jline console reader
   * @param prompt Prompt string
   */
  public JlineCommandInput(LineReaderImpl console, String prompt) {
    Validate.notNull(console, "Jline console reader can't be NULL");
    this.console = console;
    this.prompt = StringUtils.trimToEmpty(prompt);
  }

  /**
   * @return Jline console
   */
  public final LineReaderImpl getConsole() {
    return console;
  }

  @Override
  public String readLine() throws IOException {
    return console.readLine(prompt);
  }

  @Override
  public String readMaskedString(String prompt) throws IOException {
    return console.readLine(prompt, '*');
  }
}
