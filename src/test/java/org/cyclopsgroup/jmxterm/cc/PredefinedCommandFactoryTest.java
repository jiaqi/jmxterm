package org.cyclopsgroup.jmxterm.cc;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Test case of {@link PredefinedCommandFactory}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class PredefinedCommandFactoryTest {
  /**
   * Test that object is constructed
   *
   * @throws IOException
   */
  @Test
  void construction() throws Exception {
    PredefinedCommandFactory f = new PredefinedCommandFactory();
    assertTrue(f.getCommandTypes().containsKey("help"));
    assertTrue(f.getCommandTypes().containsKey("open"));
    assertTrue(f.getCommandTypes().containsKey("close"));
    assertTrue(f.getCommandTypes().containsKey("quit"));
    assertTrue(f.getCommandTypes().containsKey("beans"));
    assertInstanceOf(HelpCommand.class, f.createCommand("help"));
  }
}
