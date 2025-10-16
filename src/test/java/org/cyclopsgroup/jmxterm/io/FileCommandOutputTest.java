package org.cyclopsgroup.jmxterm.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link FileCommandOutput}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class FileCommandOutputTest {
  private File testFile;

  /** prepare for test output file */
  @BeforeEach
  void setUpTestFile() {
    testFile =
        new File(
            SystemUtils.JAVA_IO_TMPDIR
                + "/test-"
                + RandomStringUtils.secure().nextAlphabetic(20)
                + ".txt");
  }

  /**
   * Delete test file
   *
   * @throws IOException If file operation fails
   */
  @AfterEach
  void tearDownTestFile() throws IOException {
    FileUtils.forceDelete(testFile);
  }

  /**
   * Writes out some output and verify result
   *
   * @throws IOException If file IO fails
   */
  @Test
  void write() throws Exception {
    FileCommandOutput output = new FileCommandOutput(testFile, false);
    output.println("helloworld");
    output.printMessage("say hello");
    output.close();

    assertEquals(
        "helloworld", FileUtils.readFileToString(testFile, Charset.forName("UTF-8")).trim());
  }

  /**
   * Writes out some output and verify result
   *
   * @throws IOException If file IO fails
   */
  @Test
  void writeMultipleTimes() throws Exception {
    FileCommandOutput output = new FileCommandOutput(testFile, false);
    output.println("helloworld");
    output.printMessage("say hello");
    output.close();

    FileCommandOutput output2 = new FileCommandOutput(testFile, true);
    output2.println("helloworld2");
    output2.printMessage("say hello2");
    output2.close();

    assertEquals(
        "helloworld" + System.lineSeparator() + "helloworld2",
        FileUtils.readFileToString(testFile, Charset.forName("UTF-8")).trim());
  }
}
