package org.cyclopsgroup.jmxterm;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXServiceURL;

/**
 * Mock Connection implementation for testing purpose
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class MockConnection
    implements Connection
{
    private final MBeanServerConnection con;

    private final JMXServiceURL url;

    /**
     * @param url Service URL
     * @param con Server connection
     */
    public MockConnection( JMXServiceURL url, MBeanServerConnection con )
    {
        this.url = url;
        this.con = con;
    }

    /**
     * {@inheritDoc}
     */
    public final String getConnectorId()
        throws IOException
    {
        return "id";
    }

    /**
     * {@inheritDoc}
     */
    public final MBeanServerConnection getServerConnection()
        throws IOException
    {
        return con;
    }

    /**
     * {@inheritDoc}
     */
    public final JMXServiceURL getUrl()
    {
        return url;
    }
}
