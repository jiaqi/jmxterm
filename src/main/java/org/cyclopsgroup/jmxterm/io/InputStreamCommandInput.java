package org.cyclopsgroup.jmxterm.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.commons.lang3.Validate;

/**
 * Implementation of {@link CommandInput} with an input stream
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class InputStreamCommandInput
    extends CommandInput
{
    private final LineNumberReader reader;

    /**
     * @param in Given input stream
     */
    public InputStreamCommandInput( InputStream in )
    {
        Validate.notNull( in, "Input stream can't be NULL" );
        reader = new LineNumberReader( new InputStreamReader( in ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readLine()
        throws IOException
    {
        return reader.readLine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readMaskedString( String prompt )
        throws IOException
    {
        throw new UnsupportedOperationException( "Reading password from stream is not supported" );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
        throws IOException
    {
        reader.close();
    }
}
