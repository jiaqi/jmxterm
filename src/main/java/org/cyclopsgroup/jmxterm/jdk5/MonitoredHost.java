package org.cyclopsgroup.jmxterm.jdk5;

import java.util.Set;

/**
 * Interface which reflects sun.jvmstat.monitor.MonitoredHost
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface MonitoredHost
{
    /**
     * Original class this interface reflects
     */
    String ORIGINAL_CLASS_NAME = "sun.jvmstat.monitor.MonitoredHost";

    /**
     * @return Set of active VMs
     */
    Set<Integer> activeVms();
}
