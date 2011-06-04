package org.cyclopsgroup.jmxterm.pm;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang.SystemUtils;
import org.cyclopsgroup.jmxterm.pm.JConsoleClassLoaderFactory;
import org.junit.Test;

/**
 * Test loading jconsole classes
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class JConsoleClassLoaderFactoryTest
{
    /**
     * Test loading a few classes
     * 
     * @throws ClassNotFoundException Negative case
     */
    @Test
    public void testLoad()
        throws ClassNotFoundException
    {
        ClassLoader cl = JConsoleClassLoaderFactory.getClassLoader();
        assertNotNull( cl );

        Class<?> clazz;
        if ( SystemUtils.IS_JAVA_1_5 )
        {
            clazz = cl.loadClass( "sun.jvmstat.monitor.MonitoredVm" );
        }
        else if ( SystemUtils.IS_JAVA_1_6 )
        {
            clazz = cl.loadClass( "sun.tools.jconsole.LocalVirtualMachine" );
        }
        else
        {
            throw new IllegalStateException( "Please build project in from a compatible JDK, 1.5 or 1.6" );
        }
        assertNotNull( clazz );
    }
}
