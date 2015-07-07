package org.cyclopsgroup.jmxterm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.lang3.Validate;

/**
 * Utilities for loading overlapping properties files from classpath
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class ExtendedPropertiesUtils
{
    /**
     * @param resourcePath Path of overlapping properties files
     * @param classLoader Class loader where the resources are loaded
     * @return ExtendedProperties result
     * @throws IOException
     */
    public static ExtendedProperties loadFromOverlappingResources( String resourcePath, ClassLoader classLoader )
        throws IOException
    {
        Validate.notNull( resourcePath, "Resource path can't be NULL" );
        Validate.notNull( classLoader, "ClassLoader can't be NULL" );
        ExtendedProperties props = new ExtendedProperties();
        Enumeration<URL> resources = classLoader.getResources( resourcePath );
        while ( resources.hasMoreElements() )
        {
            InputStream resource = resources.nextElement().openStream();
            try
            {
                props.load( resource );
            }
            finally
            {
                resource.close();
            }
        }
        return props;
    }

    private ExtendedPropertiesUtils()
    {
    }
}
