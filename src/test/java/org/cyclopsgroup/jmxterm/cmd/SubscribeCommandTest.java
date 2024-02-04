package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringWriter;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link org.cyclopsgroup.jmxterm.cmd.RunCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class SubscribeCommandTest {
  private SubscribeCommand command;

  private Mockery context;

  private StringWriter output;

  /** Setup objects to test */
  @Before
  public void setUp() {
    context = new Mockery();
    context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
    command = new SubscribeCommand();
    output = new StringWriter();
  }

  @After
  public void tearDown() {
    SubscribeCommand.getListeners().clear();
  }

  /** @throws Exception */
  @Test
  public void testExecuteOneNotification() throws Exception {
    command.setBean("a:type=x");

    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final Notification notification = context.mock(Notification.class);

    final ObjectName objectName = new ObjectName("a:type=x");
    context.checking(
        new Expectations() {
          {
            atLeast(1).of(con).getMBeanInfo(objectName);
            will(returnValue(beanInfo));

            oneOf(con)
                .addNotificationListener(
                    with(equal(objectName)),
                    with(any(NotificationListener.class)),
                    with(aNull(NotificationFilter.class)),
                    with(aNull(Object.class)));

            atLeast(1).of(notification).getTimeStamp();
            will(returnValue(123L));

            atLeast(1).of(notification).getSource();
            will(returnValue("xyz"));

            atLeast(1).of(notification).getType();
            will(returnValue("azerty"));

            atLeast(1).of(notification).getMessage();
            will(returnValue("qwerty"));
          }
        });
    command.setSession(new MockSession(output, con));
    command.execute();
    assertEquals(1, SubscribeCommand.getListeners().size());

    NotificationListener notificationListener = SubscribeCommand.getListeners().get(objectName);
    assertNotNull(notificationListener);

    notificationListener.handleNotification(notification, null);
    assertEquals(
        "notification received: timestamp=123,class="
            + notification.getClass().getName()
            + ",source=xyz,type=azerty,message=qwerty",
        output.toString().trim());

    context.assertIsSatisfied();
  }

  /** @throws Exception */
  @Test
  public void testExecuteTwoNotifications() throws Exception {
    command.setBean("a:type=x");

    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final Notification notification = context.mock(Notification.class);

    final ObjectName objectName = new ObjectName("a:type=x");
    context.checking(
        new Expectations() {
          {
            atLeast(1).of(con).getMBeanInfo(objectName);
            will(returnValue(beanInfo));

            oneOf(con)
                .addNotificationListener(
                    with(equal(objectName)),
                    with(any(NotificationListener.class)),
                    with(aNull(NotificationFilter.class)),
                    with(aNull(Object.class)));

            atLeast(1).of(notification).getTimeStamp();
            will(returnValue(123L));

            atLeast(1).of(notification).getSource();
            will(returnValue("xyz"));

            atLeast(1).of(notification).getType();
            will(returnValue("azerty"));

            atLeast(1).of(notification).getMessage();
            will(returnValue("qwerty"));
          }
        });
    command.setSession(new MockSession(output, con));
    command.execute();
    assertEquals(1, SubscribeCommand.getListeners().size());

    NotificationListener notificationListener = SubscribeCommand.getListeners().get(objectName);
    assertNotNull(notificationListener);

    notificationListener.handleNotification(notification, null);
    notificationListener.handleNotification(notification, null);

    String expected =
        "notification received: timestamp=123,class="
            + notification.getClass().getName()
            + ",source=xyz,type=azerty,message=qwerty"
            + System.lineSeparator()
            + "notification received: timestamp=123,class="
            + notification.getClass().getName()
            + ",source=xyz,type=azerty,message=qwerty";

    assertEquals(expected, output.toString().trim());

    context.assertIsSatisfied();
  }
}
