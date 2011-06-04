package org.cyclopsgroup.jmxterm.jdk6;

/**
 * Reflect class sun.tools.jconsole.LocalVirtualMachine
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface LocalVirtualMachine
{
    /**
     * Name of original class this interface reflects
     */
    String ORIGINAL_CLASS_NAME = "sun.tools.jconsole.LocalVirtualMachine";

    /**
     * @return Address of JMX connector
     */
    String connectorAddress();

    /**
     * @return Display name of process
     */
    String displayName();

    /**
     * @return True if process is JMX attacheable
     */
    boolean isAttachable();

    /**
     * @return True if process is JMX manageable
     */
    boolean isManageable();

    /**
     * Start management agent if it's not manageable
     */
    void startManagementAgent();

    /**
     * @return PID of process
     */
    int vmid();
}
