package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringWriter;
import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link BeanCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class BeanCommandTest {
  private final BeanCommand command = new BeanCommand();

  private final StringWriter output = new StringWriter();

  private void setBeanAndVerify(String beanName, String domainName, final String expectedBean)
      throws IOException, JMException {
    Mockery context = new Mockery();
    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    command.setBean(beanName);
    MockSession s = new MockSession(output, con);
    if (domainName != null) {
      s.setDomain(domainName);
    }
    context.checking(
        new Expectations() {
          {
            atLeast(1).of(con).getMBeanInfo(new ObjectName(expectedBean));
          }
        });
    command.setSession(s);
    command.execute();
    assertEquals(expectedBean, s.getBean());
  }

  /**
   * Test execution with NULL result
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX exceptions
   */
  @Test
  void executeWithGettingNull() throws Exception {
    command.setSession(new MockSession(output, null));
    command.execute();
    assertEquals("null", output.toString().trim());
  }

  /**
   * Test execution with some result
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX exceptions
   */
  @Test
  void executeWithGettingSomething() throws Exception {
    MockSession s = new MockSession(output, null);
    s.setBean("something");
    command.setSession(s);
    command.execute();
    assertEquals("something", output.toString().trim());
  }

  /**
   * Test the case where an illegal bean is requested
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX exceptions
   */
  @Test
  void executeWithInvalidBean() throws Exception {
    command.setBean("blablabla");
    command.setSession(new MockSession(output, null));
    assertThrows(IllegalArgumentException.class, () ->
      command.execute());
  }

  /**
   * Test the case where NULL is get
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX exceptions
   */
  @Test
  void executeWithSettingNull() throws Exception {
    command.setBean("null");
    MockSession s = new MockSession(output, null);
    s.setBean("something");
    command.setSession(s);
    command.execute();
    assertNull(s.getBean());
  }

  /**
   * Test setting names with special character such as dot, dash and underline, without setting a
   * domain first
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX exceptions
   */
  @Test
  void settingSpecialCharactersWithoutDomain() throws Exception {
    setBeanAndVerify(
        "domain_name.with-dash:attr.name_1-1=a.b", null, "domain_name.with-dash:attr.name_1-1=a.b");
  }

  /**
   * Test the case where a domain is set
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX exceptions
   */
  @Test
  void settingWithDomain() throws Exception {
    setBeanAndVerify("type=x", "something", "something:type=x");
  }

  /**
   * Test the case where domain is set
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX exceptions
   */
  @Test
  void settingWithoutDomain() throws Exception {
    setBeanAndVerify("something:type=x", null, "something:type=x");
  }

  /**
   * Test setting names with special character such as dot, dash and underline
   *
   * @throws IOException Allows network IO errors
   * @throws JMException Allows JMX exceptions
   */
  @Test
  void settingWithSpecialCharacters() throws Exception {
    setBeanAndVerify(
        "attr.name_1-1=a.b", "domain_name.with-dash", "domain_name.with-dash:attr.name_1-1=a.b");
  }
}
