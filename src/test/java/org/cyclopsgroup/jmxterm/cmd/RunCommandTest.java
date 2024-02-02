package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.Arrays;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link RunCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class RunCommandTest {
  private RunCommand command;

  private Mockery context;

  private StringWriter output;

  /** Setup objects to test */
  @Before
  public void setUp() {
    context = new Mockery();
    context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
    command = new RunCommand();
    output = new StringWriter();
  }

  /** @throws Exception */
  @Test
  public void testExecuteNormally() throws Exception {
    command.setBean("a:type=x");
    command.setParameters(Arrays.asList("exe", "33"));

    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final MBeanOperationInfo opInfo = context.mock(MBeanOperationInfo.class);
    final MBeanParameterInfo paramInfo = context.mock(MBeanParameterInfo.class);
    context.checking(
        new Expectations() {
          {
            atLeast(1).of(con).getMBeanInfo(new ObjectName("a:type=x"));
            will(returnValue(beanInfo));
            oneOf(beanInfo).getOperations();
            will(returnValue(new MBeanOperationInfo[] {opInfo}));
            atLeast(1).of(opInfo).getName();
            will(returnValue("exe"));
            atLeast(1).of(opInfo).getSignature();
            will(returnValue(new MBeanParameterInfo[] {paramInfo}));
            atLeast(1).of(paramInfo).getType();
            will(returnValue("int"));
            oneOf(con)
                .invoke(new ObjectName("a:type=x"), "exe", new Object[] {33}, new String[] {"int"});
            will(returnValue("bingo"));
          }
        });
    command.setSession(new MockSession(output, con));
    command.execute();
    context.assertIsSatisfied();
    assertEquals("bingo", output.toString().trim());
  }

  /** @throws Exception */
  @Test
  public void testExecuteOverloadedMethod() throws Exception {
    command.setBean("a:type=x");
    command.setTypes("java.lang.String");
    command.setParameters(Arrays.asList("exe", "33"));

    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final MBeanOperationInfo opInfo1 = context.mock(MBeanOperationInfo.class, "opInfo1");
    final MBeanParameterInfo paramInfoInt = context.mock(MBeanParameterInfo.class, "paramInfoInt");
    final MBeanOperationInfo opInfo2 = context.mock(MBeanOperationInfo.class, "opInfo2");
    final MBeanParameterInfo paramInfoString =
        context.mock(MBeanParameterInfo.class, "paramInfoString");
    context.checking(
        new Expectations() {
          {
            atLeast(1).of(con).getMBeanInfo(new ObjectName("a:type=x"));
            will(returnValue(beanInfo));
            oneOf(beanInfo).getOperations();
            will(returnValue(new MBeanOperationInfo[] {opInfo1, opInfo2}));
            // exe <int>
            atLeast(1).of(opInfo1).getName();
            will(returnValue("exe"));
            atLeast(1).of(opInfo1).getSignature();
            will(returnValue(new MBeanParameterInfo[] {paramInfoInt}));
            atLeast(1).of(paramInfoInt).getType();
            will(returnValue("int"));
            never(con)
                .invoke(new ObjectName("a:type=x"), "exe", new Object[] {33}, new String[] {"int"});
            will(returnValue("bingo-int"));
            // exe <java.lang.String>
            atLeast(1).of(opInfo2).getName();
            will(returnValue("exe"));
            atLeast(1).of(opInfo2).getSignature();
            will(returnValue(new MBeanParameterInfo[] {paramInfoString}));
            atLeast(1).of(paramInfoString).getType();
            will(returnValue("java.lang.String"));
            oneOf(con)
                .invoke(
                    new ObjectName("a:type=x"),
                    "exe",
                    new Object[] {"33"},
                    new String[] {"java.lang.String"});
            will(returnValue("bingo-string"));
          }
        });
    command.setSession(new MockSession(output, con));
    command.execute();
    context.assertIsSatisfied();
    assertEquals("bingo-string", output.toString().trim());
  }
}
