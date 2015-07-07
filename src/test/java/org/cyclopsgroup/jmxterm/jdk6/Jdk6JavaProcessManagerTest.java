package org.cyclopsgroup.jmxterm.jdk6;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.pm.JConsoleClassLoaderFactory;
import org.junit.Test;

/**
 * Test case of Jdk6JavaProcessManager
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Jdk6JavaProcessManagerTest
{
    /**
     * Test to list processes
     * 
     * @throws Exception
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    @Test
    public void testList()
        throws Exception
    {
        if ( !SystemUtils.IS_JAVA_1_6 )
        {
            return;
        }
        Jdk6JavaProcessManager m = new Jdk6JavaProcessManager( JConsoleClassLoaderFactory.getClassLoader() );
        List<JavaProcess> ps = m.list();
        assertFalse( ps.isEmpty() );
    }
}
