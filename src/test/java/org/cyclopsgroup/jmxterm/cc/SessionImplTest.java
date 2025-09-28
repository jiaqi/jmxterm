package org.cyclopsgroup.jmxterm.cc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.io.output.NullWriter;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.SyntaxUtils;
import org.cyclopsgroup.jmxterm.io.WriterCommandOutput;
import org.cyclopsgroup.jmxterm.jdk9.Jdk9JavaProcessManager;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case of {@link ConnectionImpl}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class SessionImplTest {
  private JMXConnector con;

  private Mockery context;

  private SessionImpl session;

  /** Set up objects to test */
  @Before
  public void setUp() {
    context = new Mockery();
    con = context.mock(JMXConnector.class);
    session =
        new SessionImpl(
            new WriterCommandOutput(new NullWriter()),
            null,
            new Jdk9JavaProcessManager()) {
          @Override
          protected JMXConnector doConnect(JMXServiceURL url, Map<String, Object> env)
              throws IOException {
            return con;
          }
        };
  }

  /**
   * Verify connect() runs correctly
   *
   * @throws IOException
   */
  @Test
  public void testConnect() throws IOException {
    session.connect(SyntaxUtils.getUrl("localhost:9991", null), null);
    Connection con = session.getConnection();
    assertEquals("service:jmx:rmi:///jndi/rmi://localhost:9991/jmxrmi", con.getUrl().toString());
  }
}
