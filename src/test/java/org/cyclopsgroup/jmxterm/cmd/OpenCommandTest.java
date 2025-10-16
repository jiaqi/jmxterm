package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringWriter;
import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case to test {@link OpenCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class OpenCommandTest {
  private OpenCommand command;

  /** Set up command to test */
  @BeforeEach
  void setUp() {
    command = new OpenCommand();
  }

  /**
   * Test execution without URL
   *
   * @throws Exception
   */
  @Test
  void executeWithoutUrl() throws Exception {
    StringWriter output = new StringWriter();
    Session session = new MockSession(output, null);
    command.setSession(session);
    command.execute();
    assertEquals(
        "id,service:jmx:rmi:///jndi/rmi://localhost:9991/jmxrmi", output.toString().trim());
  }

  /** @throws Exception */
  @Test
  void executeWithUrl() throws Exception {
    command.setUrl("xyz.cyclopsgroup.org:12345");
    Session session = new MockSession(new StringWriter(), null);
    session.disconnect();
    command.setSession(session);
    command.execute();
    assertTrue(session.isConnected());
  }
}
