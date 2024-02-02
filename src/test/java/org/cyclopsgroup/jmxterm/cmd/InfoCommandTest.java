package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link InfoCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class InfoCommandTest {
  private InfoCommand command;

  private Mockery context;

  private StringWriter output;

  /** Set up objects to test */
  @Before
  public void setUp() {
    command = new InfoCommand();
    output = new StringWriter();
    context = new Mockery();
    context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
  }

  /**
   * Test how attributes are displayed
   *
   * @throws Exception
   */
  @Test
  public void testExecuteWithShowingAttributes() throws Exception {
    command.setBean("a:type=x");
    command.setType("a");
    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final MBeanAttributeInfo attributeInfo = context.mock(MBeanAttributeInfo.class);
    Session session = new MockSession(output, con);
    context.checking(
        new Expectations() {
          {
            atLeast(1).of(con).getMBeanInfo(new ObjectName("a:type=x"));
            will(returnValue(beanInfo));
            allowing(beanInfo).getClassName();
            will(returnValue("bogus class"));
            oneOf(beanInfo).getAttributes();
            will(returnValue(new MBeanAttributeInfo[] {attributeInfo}));
            atLeast(1).of(attributeInfo).isReadable();
            will(returnValue(true));
            atLeast(1).of(attributeInfo).isWritable();
            will(returnValue(false));
            atLeast(1).of(attributeInfo).getName();
            will(returnValue("b"));
            atLeast(1).of(attributeInfo).getType();
            will(returnValue("int"));
            allowing(attributeInfo).getDescription();
            will(returnValue("bingo"));
          }
        });
    command.setSession(session);
    command.execute();
    context.assertIsSatisfied();
    assertEquals(
        "# attributes" + System.lineSeparator() + "  %0   - b (int, r)", output.toString().trim());
  }

  /**
   * Test execution and show available options
   *
   * @throws Exception
   */
  @Test
  public void testExecuteWithShowingOperations() throws Exception {
    command.setBean("a:type=x");
    command.setType("o");
    MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    MBeanOperationInfo opInfo = context.mock(MBeanOperationInfo.class);
    MBeanParameterInfo paramInfo = context.mock(MBeanParameterInfo.class);
    Session session = new MockSession(output, con);
    context.checking(
        new Expectations() {
          {
            atLeast(1).of(con).getMBeanInfo(new ObjectName("a:type=x"));
            will(returnValue(beanInfo));
            allowing(beanInfo).getClassName();
            will(returnValue("bogus class"));
            oneOf(beanInfo).getOperations();
            will(returnValue(new MBeanOperationInfo[] {opInfo}));
            allowing(opInfo).getDescription();
            will(returnValue("bingo"));
            oneOf(opInfo).getSignature();
            will(returnValue(new MBeanParameterInfo[] {paramInfo}));
            oneOf(paramInfo).getType();
            will(returnValue(String.class.getName()));
            atLeast(1).of(paramInfo).getName();
            will(returnValue("a"));
            oneOf(paramInfo).getDescription();
            will(returnValue("a-desc"));
            oneOf(opInfo).getReturnType();
            will(returnValue("int"));
            atLeast(1).of(opInfo).getName();
            will(returnValue("x"));
          }
        });
    command.setSession(session);
    command.execute();
    context.assertIsSatisfied();
    assertEquals(
        "# operations" + System.lineSeparator() + "  %0   - int x(java.lang.String a)",
        output.toString().trim());
  }

  /**
   * Test execution and show available options
   *
   * @throws Exception
   */
  @Test
  public void testExecuteWithShowingSpecificOperation() throws Exception {
    command.setBean("a:type=x");
    command.setOperation("x");
    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final MBeanOperationInfo opInfo = context.mock(MBeanOperationInfo.class);
    final MBeanParameterInfo paramInfo = context.mock(MBeanParameterInfo.class);
    Session session = new MockSession(output, con);
    context.checking(
        new Expectations() {

          {
            atLeast(1).of(con).getMBeanInfo(new ObjectName("a:type=x"));
            will(returnValue(beanInfo));
            allowing(beanInfo).getClassName();
            will(returnValue("bogus class"));
            oneOf(beanInfo).getOperations();
            will(returnValue(new MBeanOperationInfo[] {opInfo}));
            allowing(opInfo).getDescription();
            will(returnValue("bingo"));
            oneOf(opInfo).getSignature();
            will(returnValue(new MBeanParameterInfo[] {paramInfo}));
            oneOf(paramInfo).getType();
            will(returnValue(String.class.getName()));
            oneOf(paramInfo).getName();
            will(returnValue("myfakeparameter"));
            oneOf(paramInfo).getDescription();
            will(returnValue("My param description"));
            oneOf(opInfo).getReturnType();
            will(returnValue("int"));
            atLeast(1).of(opInfo).getName();
            will(returnValue("x"));
          }
        });
    command.setSession(session);
    command.execute();
    context.assertIsSatisfied();
    StringBuilder result = new StringBuilder("# operations").append(System.lineSeparator());
    result
        .append("  %0   - int x(java.lang.String myfakeparameter), bingo")
        .append(System.lineSeparator());
    result.append("             parameters:").append(System.lineSeparator());
    result.append("                 + myfakeparameter      : My param description");
    assertEquals(result.toString(), output.toString().trim());
  }

  /**
   * Test execution and show available options
   *
   * @throws Exception
   */
  @Test
  public void testExecuteWithShowingNonExistingOperation() throws Exception {
    command.setBean("a:type=x");
    command.setOperation("y");
    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final MBeanOperationInfo opInfo = context.mock(MBeanOperationInfo.class);
    Session session = new MockSession(output, con);
    context.checking(
        new Expectations() {

          {
            atLeast(1).of(con).getMBeanInfo(new ObjectName("a:type=x"));
            will(returnValue(beanInfo));
            allowing(beanInfo).getClassName();
            will(returnValue("bogus class"));
            oneOf(beanInfo).getOperations();
            will(returnValue(new MBeanOperationInfo[] {opInfo}));
            atLeast(1).of(opInfo).getName();
            will(returnValue("x"));
          }
        });
    command.setSession(session);
    command.execute();
    context.assertIsSatisfied();
    assertEquals("# operations", output.toString().trim());
  }

  /**
   * Test execution and show available options
   *
   * @throws Exception
   */
  @Test
  public void testExecuteWithShowingMultipleMatchingOperations() throws Exception {
    command.setBean("a:type=x");
    command.setOperation("x");
    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final MBeanOperationInfo opInfo1 = context.mock(MBeanOperationInfo.class);
    final MBeanOperationInfo opInfo2 = context.mock(MBeanOperationInfo.class, "mockOpInfo2");
    final MBeanParameterInfo paramInfo1 = context.mock(MBeanParameterInfo.class);
    final MBeanParameterInfo paramInfo2 = context.mock(MBeanParameterInfo.class, "mockParamInfo2");
    Session session = new MockSession(output, con);
    context.checking(
        new Expectations() {

          {
            atLeast(1).of(con).getMBeanInfo(new ObjectName("a:type=x"));
            will(returnValue(beanInfo));
            allowing(beanInfo).getClassName();
            will(returnValue("bogus class"));
            oneOf(beanInfo).getOperations();
            will(returnValue(new MBeanOperationInfo[] {opInfo1, opInfo2}));
            allowing(opInfo1).getDescription();
            will(returnValue("bingo"));
            oneOf(opInfo1).getSignature();
            will(returnValue(new MBeanParameterInfo[] {paramInfo1}));
            oneOf(paramInfo1).getType();
            will(returnValue(String.class.getName()));
            oneOf(paramInfo1).getName();
            will(returnValue("a"));
            oneOf(paramInfo1).getDescription();
            will(returnValue("My param description"));
            oneOf(opInfo1).getReturnType();
            will(returnValue("int"));
            atLeast(1).of(opInfo1).getName();
            will(returnValue("x"));

            allowing(opInfo2).getDescription();
            will(returnValue("pilou"));
            oneOf(opInfo2).getSignature();
            will(returnValue(new MBeanParameterInfo[] {paramInfo2}));
            oneOf(paramInfo2).getType();
            will(returnValue(Double.TYPE.getName()));
            oneOf(paramInfo2).getName();
            will(returnValue("b"));
            oneOf(paramInfo2).getDescription();
            will(returnValue("My param 2 description"));
            oneOf(opInfo2).getReturnType();
            will(returnValue("void"));
            atLeast(1).of(opInfo2).getName();
            will(returnValue("x"));
          }
        });
    command.setSession(session);
    command.execute();
    context.assertIsSatisfied();
    StringBuilder result = new StringBuilder("# operations").append(System.lineSeparator());
    result.append("  %0   - int x(java.lang.String a), bingo").append(System.lineSeparator());
    result.append("             parameters:").append(System.lineSeparator());
    result
        .append("                 + a                    : My param description")
        .append(System.lineSeparator())
        .append(System.lineSeparator());
    result.append("  %1   - void x(double b), pilou").append(System.lineSeparator());
    result.append("             parameters:").append(System.lineSeparator());
    result.append("                 + b                    : My param 2 description");
    assertEquals(result.toString(), output.toString().trim());
  }
}
