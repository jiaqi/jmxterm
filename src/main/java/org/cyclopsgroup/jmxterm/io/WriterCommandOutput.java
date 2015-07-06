package org.cyclopsgroup.jmxterm.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import org.apache.commons.io.output.NullWriter;
import org.apache.commons.lang3.Validate;

/**
 * A command output that writes result and message to given writers
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class WriterCommandOutput
    extends CommandOutput
{
    private final Writer messageOutput;

    private final Writer resultOutput;

    /**
     * @param output Writer for both result and message
     */
    public WriterCommandOutput( Writer output )
    {
        this( output, output );
    }

    /**
     * @param resultOutput IO Writer for result output
     * @param messageOutput IO Writer for message output
     */
    public WriterCommandOutput( Writer resultOutput, Writer messageOutput )
    {
        Validate.notNull( resultOutput, "Result output can't be NULL" );
        this.resultOutput = resultOutput;
        this.messageOutput = messageOutput == null ? new NullWriter() : messageOutput;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void print( String output )
    {
        if ( output == null )
        {
            return;
        }
        try
        {
            resultOutput.write( output );
        }
        catch ( IOException e )
        {
            throw new RuntimeIOException( "Can't print out result", e );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printError( Throwable e )
    {
        e.printStackTrace( new PrintWriter( messageOutput, true ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printMessage( String message )
    {
        try
        {
            messageOutput.write( message );
        }
        catch ( IOException e )
        {
            throw new RuntimeIOException( "Can't print out message", e );
        }
    }
}
