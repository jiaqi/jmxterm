package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.jupiter.api.Assertions.*;

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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link RunCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class UnsubscribeCommandTest {
  private SubscribeCommand subscribeCommand;
  private UnsubscribeCommand unsubscribeCommand;

  private Mockery context;

  private StringWriter output;

  /** Setup objects to test */
  @BeforeEach
  void setUp() {
    context = new Mockery();
    context.setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
    subscribeCommand = new SubscribeCommand();
    unsubscribeCommand = new UnsubscribeCommand();
    output = new StringWriter();
  }

  @AfterEach
  void tearDown() {
    SubscribeCommand.getListeners().clear();
  }

  /** @throws Exception */
  @Test
  void executeNormally() throws Exception {
    subscribeCommand.setBean("a:type=x");
    unsubscribeCommand.setBean("a:type=x");

    final MBeanServerConnection con = context.mock(MBeanServerConnection.class);
    final MBeanInfo beanInfo = context.mock(MBeanInfo.class);
    final ObjectName objectName = new ObjectName("a:type=x");

    context.checking(
        new Expectations() {
          {
            atLeast(2).of(con).getMBeanInfo(objectName);
            will(returnValue(beanInfo));

            oneOf(con)
                .addNotificationListener(
                    with(equal(objectName)),
                    with(any(NotificationListener.class)),
                    with(aNull(NotificationFilter.class)),
                    with(aNull(Object.class)));

            oneOf(con)
                .removeNotificationListener(
                    with(equal(objectName)), with(any(NotificationListener.class)));
          }
        });

    MockSession session = new MockSession(output, con);
    subscribeCommand.setSession(session);
    subscribeCommand.execute();
    assertEquals(1, SubscribeCommand.getListeners().size());

    unsubscribeCommand.setSession(session);
    unsubscribeCommand.execute();
    assertTrue(SubscribeCommand.getListeners().isEmpty());

    context.assertIsSatisfied();
  }

  /** @throws Exception */
  @Test
  void executeTwoNotifications() throws Exception {
    subscribeCommand.setBean("a:type=x");

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
    subscribeCommand.setSession(new MockSession(output, con));
    subscribeCommand.execute();
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
