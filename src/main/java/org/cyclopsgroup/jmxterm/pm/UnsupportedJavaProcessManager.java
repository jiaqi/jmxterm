package org.cyclopsgroup.jmxterm.pm;

import java.util.List;

import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.JavaProcessManager;

/**
 * Implementation with nothing but {@link UnsupportedOperationException}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class UnsupportedJavaProcessManager
    extends JavaProcessManager
{
    private final Throwable cause;

    private final String message;

    /**
     * @param message Error message to display
     */
    public UnsupportedJavaProcessManager( String message )
    {
        this.message = message;
        this.cause = null;
    }
    
    /**
     * @param message Message to show
     * @param cause Root cause
     */
    public UnsupportedJavaProcessManager(String message, Throwable cause)
    {
        this.message = message;
        this.cause = cause;
    }

    /**
     * @param cause Root cause of original error
     */
    public UnsupportedJavaProcessManager( Throwable cause )
    {
        this.cause = cause;
        this.message = cause.getMessage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JavaProcess get( int pid )
    {
        throw new UnsupportedOperationException( message, cause );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<JavaProcess> list()
    {
        throw new UnsupportedOperationException( message, cause );
    }
}
