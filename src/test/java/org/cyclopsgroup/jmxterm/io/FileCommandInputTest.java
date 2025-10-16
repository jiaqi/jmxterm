package org.cyclopsgroup.jmxterm.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Test case of {@link FileCommandInput}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class FileCommandInputTest {
  /**
   * Read commands from given test text file and verify result
   *
   * @throws IOException If file IO is failed
   */
  @Test
  void read() throws Exception {
    File testFile = new File("src/test/testscript.jmx");
    try(FileCommandInput input = new FileCommandInput(testFile)) {
      assertEquals("beans", input.readLine());
      assertEquals("exit", input.readLine());
      assertNull(input.readLine());
    }
  }
}
