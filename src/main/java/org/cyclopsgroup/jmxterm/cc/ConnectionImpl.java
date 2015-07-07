package org.cyclopsgroup.jmxterm.cc;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jmxterm.Connection;

/**
 * Identifies a JMX connection
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class ConnectionImpl
    implements Connection
{
    private final JMXConnector connector;

    private final JMXServiceURL url;

    /**
     * @param connector JMX connector
     * @param url JMX service URL object
     */
    ConnectionImpl( JMXConnector connector, JMXServiceURL url )
    {
        Validate.notNull( connector, "JMX connector can't be NULL" );
        Validate.notNull( url, "JMX service URL can't be NULL" );
        this.connector = connector;
        this.url = url;
    }

    /**
     * Close current connection
     *
     * @throws IOException Communication error
     */
    void close()
        throws IOException
    {
        connector.close();
    }

    /**
     * @return JMX connector
     */
    public final JMXConnector getConnector()
    {
        return connector;
    }

     /**
      * {@inheritDoc}
     */
    @Override
    public String getConnectorId()
        throws IOException
    {
        return connector.getConnectionId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MBeanServerConnection getServerConnection()
        throws IOException
    {
        return connector.getMBeanServerConnection();
    }

     /**
      * {@inheritDoc}
     */
    @Override
    public final JMXServiceURL getUrl()
    {
        return url;
    }
}
