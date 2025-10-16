package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringWriter;
import javax.management.MBeanServerConnection;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case of {@link DomainsCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class DomainsCommandTest {
  private DomainsCommand command;

  private Mockery context;

  /** Set up objects to test */
  @BeforeEach
  void setUp() {
    command = new DomainsCommand();
    context = new Mockery();
  }

  /**
   * Test normal execution
   *
   * @throws Exception
   */
  @Test
  void execution() throws Exception {
    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    StringWriter output = new StringWriter();
    context.checking(
        new Expectations() {
          {
            oneOf(con).getDomains();
            will(returnValue(new String[] {"a", "b"}));
          }
        });
    command.setSession(new MockSession(output, con));
    command.execute();
    context.assertIsSatisfied();
    assertEquals("a" + System.lineSeparator() + "b", output.toString().trim());
  }
}
