package org.cyclopsgroup.jmxterm.cc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.management.JMException;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.caff.token.EscapingValueTokenizer;
import org.cyclopsgroup.caff.token.TokenEvent;
import org.cyclopsgroup.caff.token.TokenEventHandler;
import org.cyclopsgroup.caff.token.ValueTokenizer;
import org.cyclopsgroup.jcli.ArgumentProcessor;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.CommandFactory;
import org.cyclopsgroup.jmxterm.JavaProcessManager;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.io.CommandInput;
import org.cyclopsgroup.jmxterm.io.CommandOutput;
import org.cyclopsgroup.jmxterm.io.RuntimeIOException;
import org.cyclopsgroup.jmxterm.io.VerboseLevel;

/**
 * Facade class where commands are maintained and executed
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class CommandCenter
{
    private static final String COMMAND_DELIMITER = "&&";

    /**
     * Argument tokenizer that parses arguments
     */
    final ValueTokenizer argTokenizer = new EscapingValueTokenizer();

    /**
     * Command factory that creates commands
     */
    final CommandFactory commandFactory;

    private final Lock lock = new ReentrantLock();

    private final JavaProcessManager processManager;

    /**
     * A handler to session
     */
    final Session session;

    /**
     * Constructor with given output {@link PrintWriter}
     *
     * @param output Message output. It can't be NULL
     * @param input Command line input
     * @throws IOException Thrown for file access failure
     */
    public CommandCenter( CommandOutput output, CommandInput input )
        throws IOException
    {
        this( output, input, new PredefinedCommandFactory() );
    }

    /**
     * This constructor is for testing purpose only
     *
     * @param output Output result
     * @param input Command input
     * @param commandFactory Given command factory
     * @throws IOException IO problem
     */
    public CommandCenter( CommandOutput output, CommandInput input, CommandFactory commandFactory )
        throws IOException
    {
        Validate.notNull( output, "Output can't be NULL" );
        Validate.notNull( commandFactory, "Command factory can't be NULL" );
        processManager = new JPMFactory().getProcessManager();
        this.session = new SessionImpl( output, input, processManager );
        this.commandFactory = commandFactory;

    }

    /**
     * Close session
     */
    public void close()
    {
        session.close();
    }

    /**
     * @param url MBeanServer location. It can be <code>AAA:###</code> or full JMX server URL
     * @param env Environment variables
     * @throws IOException Thrown when connection can't be established
     */
    public void connect( JMXServiceURL url, Map<String, Object> env )
        throws IOException
    {
        Validate.notNull( url, "URL can't be NULL" );
        session.connect( url, env );
    }

    private void doExecute( String command )
        throws JMException
    {
        command = StringUtils.trimToNull( command );
        // Ignore empty line
        if ( command == null )
        {
            return;
        }
        // Ignore line comment
        if ( command.startsWith( "#" ) )
        {
            return;
        }
        // Truncate command if there's # character
        int commandEnds = command.indexOf( '#' );
        if ( commandEnds != -1 )
        {
            command = command.substring( 0, commandEnds );
        }
        // If command includes multiple segments, call them one by one using recursive call
        if ( command.indexOf( COMMAND_DELIMITER ) != -1 )
        {
            String[] commands = StringUtils.split( command, COMMAND_DELIMITER );
            for ( String c : commands )
            {
                execute( c );
            }
            return;
        }

        // Take the first argument out since it's command name
        final List<String> args = new ArrayList<String>();
        argTokenizer.parse( command, new TokenEventHandler()
        {
            public void handleEvent( TokenEvent event )
            {
                args.add( event.getToken() );
            }
        } );
        String commandName = args.remove( 0 );
        // Leave the rest of arguments for command
        String[] commandArgs = args.toArray( ArrayUtils.EMPTY_STRING_ARRAY );
        // Call command with parsed command name and arguments
        try
        {
            doExecute( commandName, commandArgs, command );
        }
        catch ( IOException e )
        {
            throw new RuntimeIOException( "Runtime IO exception: " + e.getMessage(), e );
        }
    }

    private void doExecute( String commandName, String[] commandArgs, String originalCommand )
        throws JMException, IOException
    {
        Command cmd = commandFactory.createCommand( commandName );
        if ( cmd instanceof HelpCommand )
        {
            ( (HelpCommand) cmd ).setCommandCenter( this );
        }
        ArgumentProcessor<Command> ap = (ArgumentProcessor<Command>) ArgumentProcessor.newInstance( cmd.getClass() );

        ap.process( commandArgs, cmd );
        // Print out usage if help option is specified
        if ( cmd.isHelp() )
        {
            ap.printHelp( new PrintWriter( System.out, true ) );
            return;
        }
        cmd.setSession( session );
        // Make sure concurrency and run command
        lock.lock();
        try
        {
            cmd.execute();
        }
        finally
        {
            lock.unlock();
        }
    }

    /**
     * Execute a command. Command can be a valid full command, a comment, command followed by comment or empty
     *
     * @param command String command to execute
     * @return True if successful
     */
    public boolean execute( String command )
    {
        try
        {
            doExecute( command );
            return true;
        }
        catch ( JMException e )
        {
            session.output.printError( e );
            return false;
        }
        catch ( RuntimeException e )
        {
            session.output.printError( e );
            return false;
        }
    }

    /**
     * @return Set of command names
     */
    public Set<String> getCommandNames()
    {
        return commandFactory.getCommandTypes().keySet();
    }

    /**
     * @param name Command name
     * @return Type of command associated with given name
     */
    public Class<? extends Command> getCommandType( String name )
    {
        return commandFactory.getCommandTypes().get( name );
    }

    /**
     * @return Java process manager implementation
     */
    public final JavaProcessManager getProcessManager()
    {
        return processManager;
    }

    /**
     * @return True if command center is closed
     */
    public boolean isClosed()
    {
        return session.isClosed();
    }

    /**
     * @param verboseLevel New verbose level value
     */
    public void setVerboseLevel( VerboseLevel verboseLevel )
    {
        session.setVerboseLevel( verboseLevel );
    }
}
