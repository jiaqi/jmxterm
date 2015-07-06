package org.cyclopsgroup.jmxterm.io;

import java.io.PrintStream;

import org.apache.commons.lang3.Validate;

/**
 * Implementation of CommandOutput where output is written in given PrintStream objects
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class PrintStreamCommandOutput
    extends CommandOutput
{
    private final PrintStream messageOutput;

    private final PrintStream resultOutput;

    /**
     * Default constructor that uses system standard output and err output
     */
    public PrintStreamCommandOutput()
    {
        this( System.out );
    }

    /**
     * Constructor with given result output and system error as message output
     * 
     * @param output Output for result
     */
    public PrintStreamCommandOutput( PrintStream output )
    {
        this( output, System.err );
    }

    /**
     * @param resultOutput PrintStream where result is written to
     * @param messageOutput PrintStream where message is written to
     */
    public PrintStreamCommandOutput( PrintStream resultOutput, PrintStream messageOutput )
    {
        Validate.notNull( resultOutput, "Result output can't be NULL" );
        Validate.notNull( messageOutput, "Message output can't be NULL" );
        this.resultOutput = resultOutput;
        this.messageOutput = messageOutput;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void print( String output )
    {
        resultOutput.print( output );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printError( Throwable e )
    {
        e.printStackTrace( messageOutput );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printMessage( String message )
    {
        messageOutput.println( message );
    }
}
