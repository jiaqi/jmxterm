package org.cyclopsgroup.jmxterm.jdk6;

import java.util.Map;

/**
 * Static interface of sun.tools.jconsole.LocalVirtualMachine
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface StaticLocalVirtualMachine
{
    /**
     * @return Map of all virtual machines running on local
     */
    Map<Integer, Object> getAllVirtualMachines();
}
