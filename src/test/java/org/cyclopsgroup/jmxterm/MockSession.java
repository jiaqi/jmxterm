package org.cyclopsgroup.jmxterm;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXServiceURL;

import org.cyclopsgroup.jmxterm.io.WriterCommandOutput;
import org.cyclopsgroup.jmxterm.pm.UnsupportedJavaProcessManager;

/**
 * Mocked version of {@link Session} implementation for testing purpose only
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class MockSession
    extends Session
{
    private boolean connected = true;

    private MockConnection connection;

    /**
     * @param output Output writer
     * @param con MBean service connection
     * @throws IOException
     */
    public MockSession( Writer output, MBeanServerConnection con )
        throws IOException
    {
        super( new WriterCommandOutput( output, null ), null, new UnsupportedJavaProcessManager( "testing" ) );
        connection = new MockConnection( SyntaxUtils.getUrl( "localhost:9991", null ), con );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect( JMXServiceURL url, Map<String, Object> env )
        throws IOException
    {
        connected = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect()
        throws IOException
    {
        connected = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection()
    {
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected()
    {
        return connected;
    }
}
