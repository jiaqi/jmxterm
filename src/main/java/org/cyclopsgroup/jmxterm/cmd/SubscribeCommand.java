package org.cyclopsgroup.jmxterm.cmd;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

import javax.management.*;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Command to subscribe to an MBean notification
 *
 * All notifications will be printed to the output in the form of notification received: timestamp=xxx,class=xxx,source=xxx,type=xxx,message=xxx
 * To remove the subscription call the unsubscribe in the terminal.
 */
@Cli( name = "subscribe", description = "Subscribe to the notifications of a bean", note = "Syntax is \n subscribe <bean>" )
public class SubscribeCommand
    extends Command
{
    private static Map<ObjectName, NotificationListener> listeners = new ConcurrentHashMap<ObjectName, NotificationListener>();

    public static Map<ObjectName, NotificationListener> getListeners() {
        return listeners;
    }

    public class BeanNotificationListener implements NotificationListener {

        @Override
        public void handleNotification(Notification notification, Object handback) {

            Session session = getSession();

            StringBuilder sb = new StringBuilder("notification received: ");
            sb.append("timestamp=").append(notification.getTimeStamp());
            sb.append(",class=").append(notification.getClass().getName());
            sb.append(",source=").append(notification.getSource());
            sb.append(",type=").append(notification.getType());
            sb.append(",message=").append(notification.getMessage());

            session.output.println(sb.toString());
        }
    }

    private String bean;

    private String domain;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
        throws MalformedObjectNameException, IOException, JMException
    {
        Session session = getSession();
        String beanName = BeanCommand.getBeanName( bean, domain, session );
        if ( beanName == null )
        {
            throw new IllegalArgumentException( "Please specify MBean to invoke either using -b option or bean command" );
        }

        ObjectName name = new ObjectName( beanName );
        if ( !listeners.containsKey( name ) )
        {
            MBeanServerConnection con = session.getConnection().getServerConnection();

            NotificationListener listener = new BeanNotificationListener();
            con.addNotificationListener( name, listener , null, null );
            listeners.put( name, listener );

            session.output.printMessage("Subscribed to " + name);
        }
    }

    /**
     * @param bean Bean under which the operation is
     */
    @Option( name = "b", longName = "bean", description = "MBean to invoke" )
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    /**
     * @param domain Domain under which is bean is
     */
    @Option( name = "d", longName = "domain", description = "Domain of MBean to invoke" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }
}
