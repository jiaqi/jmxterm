package org.cyclopsgroup.jmxterm;

import java.io.IOException;

/**
 * Identifies a running JVM process
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface JavaProcess
{
    /**
     * @return Display name of process
     */
    String getDisplayName();

    /**
     * @return System process ID
     */
    int getProcessId();

    /**
     * @return True if process is JMX manageable
     */
    boolean isManageable();

    /**
     * Start management agent
     * 
     * @throws IOException Thrown when management agent couldn't be started
     */
    void startManagementAgent()
        throws IOException;

    /**
     * @return Get connector URL
     */
    String toUrl();
}
