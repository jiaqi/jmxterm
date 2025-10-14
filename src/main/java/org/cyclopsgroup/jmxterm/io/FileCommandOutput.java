package org.cyclopsgroup.jmxterm.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.lang3.Validate;

/**
 * Output with a file
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class FileCommandOutput extends CommandOutput {
  private final PrintWriter fileWriter;

  private final WriterCommandOutput output;

  /**
   * @param file where the result is written to.
   * @param appendToOutput whether to write to output.
   * @throws IOException allows IO error.
   */
  public FileCommandOutput(File file, boolean appendToOutput) throws IOException {
    Validate.notNull(file, "File can't be NULL");
    File af = file.getAbsoluteFile();
    if (!af.getParentFile().isDirectory() && !af.getParentFile().mkdirs()) {
        throw new IOException("Couldn't make directory " + af.getParentFile());
      }

    fileWriter = new PrintWriter(new FileWriter(af, appendToOutput));
    output = new WriterCommandOutput(fileWriter, new PrintWriter(System.err, true));
  }

  @Override
  public void close() {
    fileWriter.flush();
    fileWriter.close();
  }

  @Override
  public void print(String value) {
    output.print(value);
  }

  @Override
  public void printError(Throwable e) {
    output.printError(e);
  }

  @Override
  public void printMessage(String message) {
    output.printMessage(message);
  }
}
