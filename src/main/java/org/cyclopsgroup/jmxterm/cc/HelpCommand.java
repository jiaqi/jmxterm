package org.cyclopsgroup.jmxterm.cc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jcli.ArgumentProcessor;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.MultiValue;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.io.RuntimeIOException;

/**
 * Command that display a help message
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "help", description = "Display available commands or usage of a command", note = "Run \"help [command1] [command2] ...\" to display usage or certain command(s). Help without argument shows list of available commands" )
public class HelpCommand
    extends Command
{
    private List<String> argNames = Collections.emptyList();

    private CommandCenter commandCenter = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        Validate.notNull( commandCenter, "Command center hasn't been set yet" );
        if ( argNames.isEmpty() )
        {
            List<String> commandNames = new ArrayList<String>( commandCenter.getCommandNames() );
            Collections.sort( commandNames );
            getSession().output.printMessage( "following commands are available to use:" );
            for ( String commandName : commandNames )
            {
                Class<? extends Command> commandType = commandCenter.getCommandType( commandName );
                org.cyclopsgroup.jcli.spi.Cli cli =
                    ArgumentProcessor.newInstance( commandType ).createParsingContext().cli();
                getSession().output.println( String.format( "%-8s - %s", commandName, cli.getDescription() ) );
            }
        }
        else
        {
            for ( String argName : argNames )
            {
                Class<? extends Command> commandType = commandCenter.getCommandType( argName );
                if ( commandType == null )
                {
                    throw new IllegalArgumentException( "Command " + argName + " is not found" );
                }
                ArgumentProcessor<Command> ap = ArgumentProcessor.newInstance( commandType );
                try
                {
                    ap.printHelp( new PrintWriter( System.out, true ) );
                }
                catch ( IOException e )
                {
                    throw new RuntimeIOException( "Can't print help message", e );
                }
            }
        }
    }

    /**
     * @param argNames Array of arguments
     */
    @MultiValue( listType = ArrayList.class )
    @Argument
    public final void setArgNames( List<String> argNames )
    {
        Validate.notNull( argNames, "argNames can't be NULL" );
        this.argNames = argNames;
    }

    /**
     * @param commandCenter CommandCenter object that calls this help command
     */
    final void setCommandCenter( CommandCenter commandCenter )
    {
        Validate.notNull( commandCenter, "commandCenter can't be NULL" );
        this.commandCenter = commandCenter;
    }
}
