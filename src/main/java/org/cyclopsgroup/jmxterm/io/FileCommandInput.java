package org.cyclopsgroup.jmxterm.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

import org.apache.commons.lang3.Validate;

/**
 * Implementation of CommandInput with given File
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class FileCommandInput
    extends CommandInput
{
    private final LineNumberReader in;

    /**
     * Read input from a given file
     * 
     * @param inputFile Given input file
     * @throws FileNotFoundException Thrown when file doesn't exist
     */
    public FileCommandInput( File inputFile )
        throws FileNotFoundException
    {
        Validate.notNull( inputFile, "Input can't be NULL" );
        this.in = new LineNumberReader( new FileReader( inputFile ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close()
        throws IOException
    {
        in.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readLine()
        throws IOException
    {
        return in.readLine();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readMaskedString( String prompt )
        throws IOException
    {
        throw new UnsupportedOperationException( "Reading password from a file is not supported" );
    }
}
