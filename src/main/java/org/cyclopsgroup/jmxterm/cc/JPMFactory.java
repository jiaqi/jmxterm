package org.cyclopsgroup.jmxterm.cc;

import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.cyclopsgroup.jmxterm.JavaProcessManager;
import org.cyclopsgroup.jmxterm.jdk5.Jdk5JavaProcessManager;
import org.cyclopsgroup.jmxterm.jdk6.Jdk6JavaProcessManager;
import org.cyclopsgroup.jmxterm.pm.JConsoleClassLoaderFactory;
import org.cyclopsgroup.jmxterm.pm.UnsupportedJavaProcessManager;

/**
 * Internal factory class to create JPM instance
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class JPMFactory
{
    private final JavaProcessManager jpm;

    /**
     * Default constructor that figures out an implementation of JPM
     */
    public JPMFactory()
    {
        if ( !SystemUtils.isJavaVersionAtLeast( JavaVersion.JAVA_1_5 ) )
        {
            jpm =
                new UnsupportedJavaProcessManager( "JDK version " + SystemUtils.JAVA_RUNTIME_VERSION
                    + " doesn't support this command" );
            return;
        }
        JavaProcessManager j;
        try
        {
            ClassLoader cl = JConsoleClassLoaderFactory.getClassLoader();
            if ( SystemUtils.IS_JAVA_1_5 )
            {
                j = new Jdk5JavaProcessManager( cl );
            }
            else
            {
                j = new Jdk6JavaProcessManager( cl );
            }
        }
        catch ( ClassNotFoundException e )
        {
            j =
                new UnsupportedJavaProcessManager( e.getMessage() + ", operation on this JDK("
                    + SystemUtils.JAVA_RUNTIME_VERSION + ") isn't fully supported", e );
        }
        catch ( Exception e )
        {
            j = new UnsupportedJavaProcessManager( e );
        }
        jpm = j;
    }

    /**
     * @return Java process manager instance
     */
    final JavaProcessManager getProcessManager()
    {
        return jpm;
    }
}
