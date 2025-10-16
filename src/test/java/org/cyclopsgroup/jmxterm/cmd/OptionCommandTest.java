package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringWriter;
import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test for {@link OptionCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class OptionCommandTest {
  private OptionCommand command;

  private StringWriter output;

  /** Set up object to test */
  @BeforeEach
  void setUp() {
    command = new OptionCommand();
    output = new StringWriter();
  }

  /** @throws Exception */
  @Test
  void executeWithInvalidVerbose() throws Exception {
    Session session = new MockSession(output, null);
    command.setVerboseLevel("xyz");
    command.setSession(session);
    assertThrows(IllegalArgumentException.class, () ->
      command.execute());
  }
}
