package org.cyclopsgroup.jmxterm.cc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jcli.jline.CliCompletor;
import org.cyclopsgroup.jmxterm.Command;

import jline.console.completer.Completer;

/**
 * JLine completor that handles tab key
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class ConsoleCompletor
    implements Completer
{
    private static final Log LOG = LogFactory.getLog( ConsoleCompletor.class );

    private final CommandCenter commandCenter;

    private final List<String> commandNames;

    /**
     * Constructor using a command center
     *
     * @param commandCenter
     */
    public ConsoleCompletor( CommandCenter commandCenter )
    {
        Validate.notNull( commandCenter, "Command center can't be NULL" );
        this.commandCenter = commandCenter;
        List<String> commandNames = new ArrayList<String>( commandCenter.getCommandNames() );
        Collections.sort( commandNames );
        this.commandNames = Collections.unmodifiableList( commandNames );
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( { "unchecked", "rawtypes" } )
    public int complete( String buffer, int position, List candidates )
    {
        try
        {
            if ( StringUtils.isEmpty( buffer ) || buffer.indexOf( ' ' ) == -1 )
            {
                return completeCommandName( buffer, candidates );
            }
            int separatorPos = buffer.indexOf( ' ' );
            String commandName = buffer.substring( 0, separatorPos );
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Command name is [" + commandName + "]" );
            }
            String commandArguments = buffer.substring( separatorPos + 1 );
            commandArguments.replaceFirst( "^\\s*", "" );
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Analyzing commmand arguments [" + commandArguments + "]" );
            }
            Command cmd = commandCenter.commandFactory.createCommand( commandName );
            cmd.setSession( commandCenter.session );
            CliCompletor commandCompletor = new CliCompletor( cmd, commandCenter.argTokenizer );
            return commandCompletor.complete( commandArguments, position - separatorPos, candidates ) + separatorPos
                + 1;
        }
        catch ( RuntimeException e )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Couldn't complete input", e );
            }
            return position;
        }
    }

    private int completeCommandName( String buf, List<String> candidates )
    {
        if ( buf == null )
        {
            // Nothing is there
            candidates.addAll( commandNames );
        }
        else if ( buf.indexOf( ' ' ) == -1 )
        {
            // Partial one word
            List<String> matchedNames = new ArrayList<String>();
            for ( String commandName : commandNames )
            {
                if ( commandName.startsWith( buf ) )
                {
                    matchedNames.add( commandName );
                }
            }
            candidates.addAll( matchedNames );
        }
        else
        {
            throw new IllegalStateException( "Invalid state" );
        }
        return 0;
    }
}
