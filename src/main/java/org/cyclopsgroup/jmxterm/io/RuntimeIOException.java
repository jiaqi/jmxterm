package org.cyclopsgroup.jmxterm.io;

import java.io.IOException;

/**
 * Unchecked version of IOException
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class RuntimeIOException
    extends RuntimeException
{
    private static final long serialVersionUID = -2304094504586109315L;

    /**
     * @param message Error message
     * @param e Original IOException
     */
    public RuntimeIOException( String message, IOException e )
    {
        super( message, e );
    }
}
