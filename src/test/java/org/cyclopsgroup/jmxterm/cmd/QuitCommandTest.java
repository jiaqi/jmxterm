package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringWriter;
import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link QuitCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class QuitCommandTest {
  private QuitCommand command;

  private StringWriter output;

  /** Setup objects to test */
  @BeforeEach
  void setUp() {
    command = new QuitCommand();
    output = new StringWriter();
  }

  /** @throws Exception */
  @Test
  void execute() throws Exception {
    Session session = new MockSession(output, null);
    command.setSession(session);
    command.execute();
    assertFalse(session.isConnected());
    assertTrue(session.isClosed());
  }
}
