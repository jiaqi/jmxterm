package org.cyclopsgroup.jmxterm.cc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import javax.management.remote.JMXConnector;
import org.cyclopsgroup.jmxterm.SyntaxUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;

/**
 * Test case of {@link ConnectionImpl}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class ConnectionImplTest {
  /**
   * Test the object is constructed correctly
   *
   * @throws IOException
   */
  @Test
  void construction() throws Exception {
    Mockery context = new Mockery();
    final JMXConnector con = context.mock(JMXConnector.class);
    ConnectionImpl c = new ConnectionImpl(con, SyntaxUtils.getUrl("localhost:9991", null));
    assertSame(con, c.getConnector());

    context.checking(
        new Expectations() {
          {
            oneOf(con).getConnectionId();
            will(returnValue("xyz"));
          }
        });
    assertEquals("xyz", c.getConnectorId());
    context.assertIsSatisfied();
  }
}
