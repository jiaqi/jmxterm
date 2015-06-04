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
import static org.junit.Assert.assertTrue;

/**
 * Test case for {@link RunCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class UnsubscribeCommandTest
{
    private SubscribeCommand subscribeCommand;
    private UnsubscribeCommand unsubscribeCommand;

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
        subscribeCommand = new SubscribeCommand();
        unsubscribeCommand = new UnsubscribeCommand();
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
    public void testExecuteNormally()
        throws Exception
    {
        subscribeCommand.setBean("a:type=x");
        unsubscribeCommand.setBean("a:type=x");

        final MBeanServerConnection con =
            context.mock( MBeanServerConnection.class );
        final MBeanInfo beanInfo = context.mock( MBeanInfo.class );
        final ObjectName objectName = new ObjectName("a:type=x");

        context.checking(new Expectations() {
            {
                atLeast(2).of(con).getMBeanInfo(objectName);
                will(returnValue(beanInfo));

                one(con).addNotificationListener(
                        with(equal(objectName)),
                        with(any(NotificationListener.class)),
                        with(aNull(NotificationFilter.class)),
                        with(aNull(Object.class)));

                one(con).removeNotificationListener(
                        with(equal(objectName)),
                        with(any(NotificationListener.class)));
            }
        });

        MockSession session = new MockSession(output, con);
        subscribeCommand.setSession( session );
        subscribeCommand.execute();
        assertEquals( 1, SubscribeCommand.getListeners().size() );

        unsubscribeCommand.setSession( session );
        unsubscribeCommand.execute();
        assertTrue( SubscribeCommand.getListeners().isEmpty() );

        context.assertIsSatisfied();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExecuteTwoNotifications()
            throws Exception
    {
        subscribeCommand.setBean("a:type=x");

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
        subscribeCommand.setSession(new MockSession(output, con));
        subscribeCommand.execute();
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
