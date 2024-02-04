package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Invocation;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.jmock.lib.action.CustomAction;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case of {@link SetCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class SetCommandTest {
  private SetCommand command;

  private Mockery context;

  private StringWriter output;

  /** Set up objects to test */
  @Before
  public void setUp() {
    command = new SetCommand();
    output = new StringWriter();
    context = new Mockery();
    context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
  }

  private void setValueAndVerify(String expr, final String type, final Object expected) {
    command.setBean("a:type=x");
    command.setArguments(Arrays.asList("var", expr));

    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final MBeanAttributeInfo attributeInfo = context.mock(MBeanAttributeInfo.class);
    final AtomicReference<Attribute> setAttribute = new AtomicReference<Attribute>();
    try {
      context.checking(
          new Expectations() {
            {
              atLeast(1).of(con).getMBeanInfo(new ObjectName("a:type=x"));
              will(returnValue(beanInfo));
              atLeast(1).of(beanInfo).getAttributes();
              will(returnValue(new MBeanAttributeInfo[] {attributeInfo}));
              atLeast(1).of(attributeInfo).getName();
              will(returnValue("var"));
              atLeast(1).of(attributeInfo).getType();
              will(returnValue(type));
              atLeast(1).of(attributeInfo).isWritable();
              will(returnValue(true));
              oneOf(con)
                  .setAttribute(
                      with(equal(new ObjectName("a:type=x"))),
                      (Attribute) with(aNonNull(Attribute.class)));
              will(
                  doAll(
                      new CustomAction("SetAttribute") {

                        public Object invoke(Invocation invocation) throws Throwable {
                          setAttribute.set((Attribute) invocation.getParameter(1));
                          return null;
                        }
                      }));
            }
          });

      command.setSession(new MockSession(output, con));
      command.execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (JMException e) {
      throw new RuntimeException(e);
    }
    context.assertIsSatisfied();

    assertNotNull(setAttribute.get());
    assertEquals("var", setAttribute.get().getName());
    assertEquals(expected, setAttribute.get().getValue());
  }

  /** Test setting an integer */
  @Test
  public void testExecuteNormally() {
    setValueAndVerify("33", "int", 33);
  }

  /** Test setting an empty string */
  @Test
  public void testExecuteWithAnEmptyString() {
    setValueAndVerify("\"\"", String.class.getName(), "");
  }

  /** Test setting string with control character */
  @Test
  public void testExecuteWithControlCharacter() {
    setValueAndVerify("hello\\n", String.class.getName(), "hello\n");
  }

  /** Test with negative number */
  @Test
  public void testExecuteWithNegativeNumber() {
    setValueAndVerify("-2", "int", -2);
  }

  /** Test setting NULL string */
  @Test
  public void testExecuteWithNullString() {
    setValueAndVerify("null", String.class.getName(), null);
  }

  /** Test with quoted negative number */
  @Test
  public void testExecuteWithQuotedNegativeNumber() {
    setValueAndVerify("\"-2\"", "int", -2);
  }
}
