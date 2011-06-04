package org.cyclopsgroup.jmxterm;

import java.util.List;

/**
 * Java process manager
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class JavaProcessManager
{
    /**
     * Get JVM process
     * 
     * @param pid Process ID
     * @return Process or NULL
     */
    public abstract JavaProcess get( int pid );

    /**
     * List all running Java processes
     * 
     * @return List of running processes
     */
    public abstract List<JavaProcess> list();
}
