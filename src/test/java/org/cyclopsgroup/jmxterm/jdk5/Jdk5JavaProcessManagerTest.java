package org.cyclopsgroup.jmxterm.jdk5;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.apache.commons.lang3.SystemUtils;
import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.pm.JConsoleClassLoaderFactory;
import org.junit.Test;

/**
 * Test case of {@link Jdk5JavaProcessManager}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class Jdk5JavaProcessManagerTest
{
    /**
     * Dummy test case
     * 
     * @throws Exception For any constrocutor exception
     */
    @Test
    public void testConstruction()
        throws Exception
    {
        if ( !SystemUtils.IS_JAVA_1_5 )
        {
            return;
        }
        Jdk5JavaProcessManager jpm = new Jdk5JavaProcessManager( JConsoleClassLoaderFactory.getClassLoader() );
        List<JavaProcess> ps = jpm.list();
        assertFalse( ps.isEmpty() );
    }
}
