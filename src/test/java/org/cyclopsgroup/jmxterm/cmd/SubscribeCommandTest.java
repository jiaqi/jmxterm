package org.cyclopsgroup.jmxterm.cmd;

import org.apache.commons.lang3.SystemUtils;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.management.*;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test case for {@link org.cyclopsgroup.jmxterm.cmd.RunCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class SubscribeCommandTest
{
    private SubscribeCommand command;

    private Mockery context;

    private StringWriter output;

    /**
     * Setup objects to test
     */
    @Before
    public void setUp()
    {
        context = new Mockery();
        context.setImposteriser( ClassImposteriser.INSTANCE );
        command = new SubscribeCommand();
        output = new StringWriter();
    }

    @After
    public void tearDown() {
        SubscribeCommand.getListeners().clear();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExecuteOneNotification()
        throws Exception
    {
        command.setBean("a:type=x");

        final MBeanServerConnection con =
            context.mock( MBeanServerConnection.class );
        final MBeanInfo beanInfo = context.mock( MBeanInfo.class );
        final Notification notification = context.mock(Notification.class);

        final ObjectName objectName = new ObjectName("a:type=x");
        context.checking(new Expectations() {
            {
                atLeast(1).of(con).getMBeanInfo(objectName);
                will(returnValue(beanInfo));

                one(con).addNotificationListener(
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
        assertEquals( 1, SubscribeCommand.getListeners().size() );

        NotificationListener notificationListener = SubscribeCommand.getListeners().get( objectName );
        assertNotNull( notificationListener );

        notificationListener.handleNotification( notification, null );
        assertEquals( "notification received: timestamp=123,class=" + notification.getClass().getName() + ",source=xyz,type=azerty,message=qwerty", output.toString().trim() );

        context.assertIsSatisfied();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExecuteTwoNotifications()
            throws Exception
    {
        command.setBean("a:type=x");

        final MBeanServerConnection con =
                context.mock( MBeanServerConnection.class );
        final MBeanInfo beanInfo = context.mock( MBeanInfo.class );
        final Notification notification = context.mock(Notification.class);

        final ObjectName objectName = new ObjectName("a:type=x");
        context.checking(new Expectations() {
            {
                atLeast(1).of(con).getMBeanInfo(objectName);
                will(returnValue(beanInfo));

                one(con).addNotificationListener(
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
        assertEquals( 1, SubscribeCommand.getListeners().size() );

        NotificationListener notificationListener = SubscribeCommand.getListeners().get( objectName );
        assertNotNull( notificationListener );

        notificationListener.handleNotification( notification, null );
        notificationListener.handleNotification( notification, null );

        String expected = "notification received: timestamp=123,class=" + notification.getClass().getName() + ",source=xyz,type=azerty,message=qwerty"
                + SystemUtils.LINE_SEPARATOR
                + "notification received: timestamp=123,class=" + notification.getClass().getName() + ",source=xyz,type=azerty,message=qwerty";

        assertEquals( expected, output.toString().trim() );

        context.assertIsSatisfied();
    }

}
