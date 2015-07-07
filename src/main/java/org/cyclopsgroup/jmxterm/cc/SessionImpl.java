package org.cyclopsgroup.jmxterm.cc;

import java.io.IOException;
import java.util.Map;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jmxterm.Connection;
import org.cyclopsgroup.jmxterm.JavaProcessManager;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.io.CommandInput;
import org.cyclopsgroup.jmxterm.io.CommandOutput;

/**
 * Implementation of {@link Session} which keeps a {@link ConnectionImpl}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class SessionImpl
    extends Session
{
    private ConnectionImpl connection;

    /**
     * @param output Output result
     * @param input Command line input
     * @param jpm Java process manager
     */
    SessionImpl( CommandOutput output, CommandInput input, JavaProcessManager jpm )
    {
        super( output, input, jpm );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect( JMXServiceURL url, Map<String, Object> env )
        throws IOException
    {
        Validate.notNull( url, "URL can't be NULL" );
        if ( connection != null )
        {
            throw new IllegalStateException( "Session is already opened" );
        }
        JMXConnector connector = doConnect( url, env );
        connection = new ConnectionImpl( connector, url );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect()
        throws IOException
    {
        if ( connection == null )
        {
            return;
        }
        try
        {
            connection.close();
        }
        finally
        {
            connection = null;
        }
    }

    /**
     * Connect to MBean server
     *
     * @param url MBean server URL
     * @param env A map of environment
     * @return Connector that holds connection to MBean server
     * @throws IOException Network errors
     */
    protected JMXConnector doConnect( JMXServiceURL url, Map<String, Object> env )
        throws IOException
    {
        return JMXConnectorFactory.connect( url, env );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection()
    {
        if ( connection == null )
        {
            throw new IllegalStateException( "Connection isn't open yet. Run open command to open a connection" );
        }
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected()
    {
        return connection != null;
    }
}
