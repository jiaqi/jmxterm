package org.cyclopsgroup.jmxterm.pm;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

import org.apache.commons.lang3.SystemUtils;

/**
 * Utility to get class loader that understands tools.jar and jconsole.jar
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class JConsoleClassLoaderFactory
{
    private JConsoleClassLoaderFactory()
    {
    }

    /**
     * @return ClassLoader that understands tools.jar and jconsole.jar
     */
    public static ClassLoader getClassLoader()
    {
        File javaHome = new File( SystemUtils.JAVA_HOME ).getAbsoluteFile().getParentFile();
        final File toolsJar, jconsoleJar;
        if (isBeforeJava7() && isMacOs())
        {
            toolsJar = new File( javaHome, "Classes/classes.jar" );
            jconsoleJar = new File( javaHome, "Classes/jconsole.jar" );
        }
        else
        {
            toolsJar = new File( javaHome, "lib/tools.jar" );
            jconsoleJar = new File( javaHome, "lib/jconsole.jar" );
        }
        if ( !toolsJar.isFile() )
        {
            throw new RuntimeException( "Operation requires JDK instead of JRE" );
        }
        if ( !jconsoleJar.isFile() )
        {
            throw new RuntimeException( jconsoleJar + " file is not found" );
        }
        // Parent class loader has to be bootstrap class loader instead of current one
        return AccessController.doPrivileged( new PrivilegedAction<ClassLoader>()
        {
            public ClassLoader run()
            {
                try
                {
                    return new URLClassLoader( new URL[] { toolsJar.toURI().toURL(), jconsoleJar.toURI().toURL() },
                                               String.class.getClassLoader() );
                }
                catch ( MalformedURLException e )
                {
                    throw new RuntimeException( "Couddn't convert files to URLs " + toolsJar + ", " + jconsoleJar
                        + ": " + e.getMessage(), e );
                }
            }
        } );
    }

    private static boolean isBeforeJava7() {
        return SystemUtils.IS_JAVA_1_5 || SystemUtils.IS_JAVA_1_6;
    }

    private static boolean isMacOs() {
        return SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_MAC_OSX;
    }

}