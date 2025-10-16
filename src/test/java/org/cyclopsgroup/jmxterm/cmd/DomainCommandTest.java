package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.StringWriter;

import javax.management.JMException;
import javax.management.MBeanServerConnection;

import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link DomainCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class DomainCommandTest {
  private DomainCommand command;

  private StringWriter output;

  private void setDomainAndVerify(String domainName, final String[] knownDomains)
      throws IOException {
    Mockery context = new Mockery();
    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    command.setDomain(domainName);
    MockSession session = new MockSession(output, con);
    context.checking(
        new Expectations() {
          {
            oneOf(con).getDomains();
            will(returnValue(knownDomains));
          }
        });
    command.setSession(session);
    command.execute();
    assertEquals(domainName, session.getDomain());
    context.assertIsSatisfied();
  }

  /** Set up command to test */
  @BeforeEach
  void setUp() {
    command = new DomainCommand();
    output = new StringWriter();
  }

  /**
   * Test execution and get empty result
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX errors
   */
  @Test
  void executeWithGettingNull() throws Exception {
    command.setSession(new MockSession(output, null));
    command.execute();
    assertEquals("null", output.toString().trim());
  }

  /**
   * Test execution and get valid result
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX errors
   */
  @Test
  void executeWithGettingSomething() throws Exception {
    MockSession session = new MockSession(output, null);
    session.setDomain("something");
    command.setSession(session);
    command.execute();
    assertEquals("something", output.toString().trim());
  }

  /**
   * Test the case where invalid value is declined
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX errors
   */
  @Test
  void settingWithInvalidDomain() throws Exception {
    assertThrows(IllegalArgumentException.class, () ->
      setDomainAndVerify("invalid", new String[]{"something"}));
  }

  /**
   * Test execution and set value with special characters
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX errors
   */
  @Test
  void settingWithSpecialCharacters() throws Exception {
    setDomainAndVerify("my_domain.1-1", new String[] {"my_domain.1-1", "something"});
  }

  /**
   * Test execution and set valid value
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX errors
   */
  @Test
  void settingWithValidDomain() throws Exception {
    setDomainAndVerify("something", new String[] {"something"});
  }
}
