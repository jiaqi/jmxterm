package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.SimpleType;

import org.apache.commons.lang3.RandomStringUtils;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case of {@link GetCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class GetCommandTest {
  private GetCommand command;

  private Mockery context;

  private StringWriter output;

  private void getAttributeAndVerify(
      final String domain,
      String bean,
      final String attribute,
      final String expectedBean,
      final Object expectedValue,
      final boolean singleLine,
      final String delimiter) {
    command.setDomain(domain);
    command.setBean(bean);
    command.setAttributes(Arrays.asList(attribute));
    command.setSimpleFormat(true);
    command.setSingleLine(singleLine);
    command.setDelimiter(delimiter);

    final String[] attributePath = attribute.split("\\.");

    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final MBeanAttributeInfo attributeInfo = context.mock(MBeanAttributeInfo.class);
    try {
      context.checking(
          new Expectations() {
            {
              oneOf(con).getDomains();
              will(returnValue(new String[] {domain, RandomStringUtils.secure().nextAlphabetic(5)}));
              allowing(con).getMBeanInfo(new ObjectName(expectedBean));
              will(returnValue(beanInfo));
              oneOf(beanInfo).getAttributes();
              will(returnValue(new MBeanAttributeInfo[] {attributeInfo}));
              allowing(attributeInfo).getName();
              will(returnValue(attributePath[0]));
              allowing(attributeInfo).isReadable();
              will(returnValue(true));
              oneOf(con).getAttribute(new ObjectName(expectedBean), attributePath[0]);
              will(returnValue(expectedValue));
            }
          });
      command.setSession(new MockSession(output, con));
      command.execute();
      context.assertIsSatisfied();

      Object nestedExpectedValue = expectedValue;

      if (expectedValue instanceof CompositeDataSupport support) {
        nestedExpectedValue = support.get(attributePath[1]);
      }

      assertEquals(
          nestedExpectedValue.toString() + delimiter + (singleLine ? "" : System.lineSeparator()),
          output.toString());
    } catch (JMException e) {
      throw new RuntimeException("Test failed for unexpected JMException", e);
    } catch (IOException e) {
      throw new RuntimeException("Test failed for unexpected IOException", e);
    }
  }

  /** Set up class to test */
  @BeforeEach
  void setUp() {
    command = new GetCommand();
    context = new Mockery();
    context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
    output = new StringWriter();
  }

  /** Test normal execution */
  @Test
  void executeNormally() {
    getAttributeAndVerify("a", "type=x", "a", "a:type=x", "bingo", false, "");
  }

  /** Verify non string type is formatted into string */
  @Test
  void executeWithNonStringType() {
    getAttributeAndVerify("a", "type=x", "a", "a:type=x", Integer.valueOf(10), false, "");
  }

  @Test
  void executeWithSlashInDomainName() {
    getAttributeAndVerify("a/b", "type=c", "a", "a/b:type=c", "bingo", false, "");
  }

  /**
   * Verify attribute name with dash, underline and dot is acceptable
   *
   * @throws OpenDataException
   */
  @Test
  void executeWithStrangeAttributeName() throws Exception {
    final Map<String, Object> entries = new HashMap<>();
    entries.put("d", "bingo");
    final CompositeType compositeType = context.mock(CompositeType.class);
    context.checking(
        new Expectations() {
          {
            oneOf(compositeType).keySet();
            will(returnValue(entries.keySet()));
            oneOf(compositeType).getType("d");
            will(returnValue(SimpleType.STRING));
          }
        });
    Object expectedValue = new CompositeDataSupport(compositeType, entries);
    getAttributeAndVerify("a", "type=x", "a_b-c.d", "a:type=x", expectedValue, false, "");
  }

  /** Verify unusual bean name and domain name is acceptable */
  @Test
  void executeWithUnusualDomainAndBeanName() {
    getAttributeAndVerify("a-a", "a.b-c_d=x-y.z", "a", "a-a:a.b-c_d=x-y.z", "bingo", false, "");
  }

  /** Verify that delimiters are working */
  @Test
  void executeWithDelimiters() {
    getAttributeAndVerify("a", "type=x", "a", "a:type=x", "bingo", false, ",");
  }

  /** Verify that single line output is working */
  @Test
  void executeForSingleLineOutput() {
    getAttributeAndVerify("a", "type=x", "a", "a:type=x", "bingo", true, "");
  }
}
