package org.cyclopsgroup.jmxterm.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

/**
 * Test case of {@link FileCommandInput}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class FileCommandInputTest {
  /**
   * Read commands from given test text file and verify result
   *
   * @throws IOException If file IO is failed
   */
  @Test
  public void testRead() throws IOException {
    File testFile = new File("src/test/testscript.jmx");
    try(FileCommandInput input = new FileCommandInput(testFile)) {
      assertEquals("beans", input.readLine());
      assertEquals("exit", input.readLine());
      assertNull(input.readLine());
    }
  }
}
