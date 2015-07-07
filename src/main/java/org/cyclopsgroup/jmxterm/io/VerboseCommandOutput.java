package org.cyclopsgroup.jmxterm.io;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Command output implementation where detail message can be turned on and off dynamically
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class VerboseCommandOutput
    extends CommandOutput
{
    private final VerboseCommandOutputConfig config;

    private final CommandOutput output;

    /**
     * @param output Proxy'ed output
     * @param config Dynamic config
     */
    public VerboseCommandOutput( CommandOutput output, VerboseCommandOutputConfig config )
    {
        Validate.notNull( output, "The proxy'ed output can't be NULL" );
        Validate.notNull( config, "Config can't be NULL" );
        this.output = output;
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
    {
        output.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void print( String value )
    {
        output.print( value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printError( Throwable e )
    {
        switch ( config.getVerboseLevel() )
        {
            case VERBOSE:
                output.printError( e );
                break;
            case SILENT:
                break;
            case BRIEF:
            default:
                output.printMessage( "#" + ExceptionUtils.getMessage( e ) );
                break;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printMessage( String message )
    {
        if ( config.getVerboseLevel() != VerboseLevel.SILENT )
        {
            output.printMessage( "#" + message );
        }
    }
}
