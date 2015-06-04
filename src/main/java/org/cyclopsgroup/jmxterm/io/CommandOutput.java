package org.cyclopsgroup.jmxterm.io;

import org.apache.commons.lang3.SystemUtils;

/**
 * General abstract class to output message and values
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class CommandOutput
{
    /**
     * Close the output;
     */
    public void close()
    {
    }

    /**
     * Print out value to output without line break
     * 
     * @param output Value to print out
     */
    public abstract void print( String output );

    /**
     * @param e Error to print out
     */
    public abstract void printError( Throwable e );

    /**
     * Print out value to output as standalone line
     * 
     * @param output Value to print out
     */
    public void println( String output )
    {
        print( output );
        print( SystemUtils.LINE_SEPARATOR );
    }

    /**
     * Print message to non-standard console for human to read. New line is always appended
     * 
     * @param message Message to print out.
     */
    public abstract void printMessage( String message );
}
