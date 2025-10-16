package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.StringWriter;
import org.cyclopsgroup.jmxterm.MockSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link CloseCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class CloseCommandTest {
  private CloseCommand command;

  private StringWriter output;

  /** Set up classes to test */
  @BeforeEach
  void setUp() {
    command = new CloseCommand();
    output = new StringWriter();
  }

  /**
   * Test execution
   *
   * @throws Exception
   */
  @Test
  void execute() throws Exception {
    MockSession session = new MockSession(output, null);
    command.setSession(session);
    command.execute();
    assertFalse(session.isConnected());
  }
}
