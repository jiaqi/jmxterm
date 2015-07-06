package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;

import javax.management.JMException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command to subscribe to an MBean notification
 *
 * Remove the subscription of an already subscribed notification listener.
 * Notifications will no longer be sent to the session output.
 */
@Cli( name = "unsubscribe", description = "Unsubscribe the notifications of an earlier subscribed bean", note = "Syntax is \n unsubscribe <bean>" )
public class UnsubscribeCommand
    extends Command
{
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
        NotificationListener listener = SubscribeCommand.getListeners().remove( name );
        if ( listener != null )
        {
            MBeanServerConnection con = session.getConnection().getServerConnection();
            con.removeNotificationListener( name, listener );

            session.output.printMessage("Unsubscribed from " + name);
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
