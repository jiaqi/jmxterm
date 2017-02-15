package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.JMException;

import org.apache.commons.lang.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.MultiValue;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command to echo text
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "echo", description = "Prints predefined text or timestamps" )
public class EchoCommand
    extends Command
{

    private List<String> texts = new ArrayList<String>();

    private boolean echoTimestamp;

    private String timestampFormat = "dd.MM.yyyy HH:mm:ss";

    private boolean singleLine = false;

    private String delimiter = "";

    /**
     * @inheritDoc
     */
    @Override
    public void execute()
        throws IOException, JMException
    {
        Session session = getSession();

        for ( int i = 0 ; i < texts.size() ; i++ )
        {
            session.output.print( texts.get(i) );

            if ( i < texts.size() - 1 )
            {
                session.output.print( delimiter );
            }
        }

        if ( echoTimestamp )
        {
            if ( !texts.isEmpty() )
            {
                session.output.print( delimiter );
            }
            DateFormat df = new SimpleDateFormat( timestampFormat );
            session.output.print( df.format( new Date() ) );
            if ( texts.isEmpty() )
            {
                session.output.print( delimiter );
            }
        }

        if ( !singleLine )
        {
            session.output.println( "" );
        }
    }

    /**
     * @param echoTimestamp True to echo the current system timestamp
     */
    @Option( name = "t", longName = "timestamp", description = "Echo the current system timestamp" )
    public final void setEchoTimestamp( boolean echoTimestamp )
    {
        this.echoTimestamp = echoTimestamp;
    }

    /**
     * @param echoTimestamp True to echo the current system timestamp
     */
    @Option( name = "f", longName = "format", description = "The format to be used for the timestamp" )
    public final void setTimestampFormat( String timestampFormat )
    {
        this.timestampFormat = timestampFormat;
    }

    @Option( name = "l", longName = "delimiter", description = "Sets an optional delimiter to be printed after the value" )
    public final void setDelimiter( String delimiter )
    {
        this.delimiter = delimiter;
    }

    @Option( name = "n", longName = "singleLine", description = "Prints result without a newline - default is false" )
    public final void setSingleLine( boolean singleLine )
    {
        this.singleLine = singleLine;
    }

    /**
     * @param attributes List of attribute names
     */
    @MultiValue( listType = ArrayList.class, minValues = 0 )
    @Argument( displayName = "texts", description = "Texts to print" )
    public final void setTexts( List<String> texts )
    {
        Validate.notNull( texts, "Texts can't be NULL" );
        this.texts = texts;
    }

}
