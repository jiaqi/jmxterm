package org.cyclopsgroup.jmxterm;

import java.io.IOException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXServiceURL;

/**
 * Identifies lifecycle of a connection
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface Connection
{
    /**
     * @return Id of connector
     * @throws IOException Thrown when ID can't be retrieved
     */
    String getConnectorId()
        throws IOException;

    /**
     * @return MBean server connection
     * @throws IOException Thrown for communication problem
     */
    MBeanServerConnection getServerConnection()
        throws IOException;

    /**
     * @return JMX service URL object
     */
    JMXServiceURL getUrl();
}