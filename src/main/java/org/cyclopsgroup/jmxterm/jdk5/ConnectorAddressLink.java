package org.cyclopsgroup.jmxterm.jdk5;

import java.io.IOException;

/**
 * Reflect <code>sun.management.ConnectorAddressLink</code>
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface ConnectorAddressLink
{
    /**
     * Name of the original class this interface reflects
     */
    String ORIGINAL_CLASS_NAME = "sun.management.ConnectorAddressLink";

    /**
     * Imports the connector address from the instrument buffer of the specified Java virtual machine.
     * 
     * @param vmid an identifier that uniquely identifies a local Java virtual machine, or <code>0</code> to indicate
     *            the current Java virtual machine.
     * @return the value of the connector address, or <code>null</code> if the target VM has not exported a connector
     *         address.
     * @throws IOException An I/O error occurred while trying to acquire the instrumentation buffer.
     */
    String importFrom( int vmid )
        throws IOException;
}
