package org.cyclopsgroup.jmxterm.boot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.management.remote.JMXConnector;

import org.apache.commons.lang3.StringUtils;
import org.cyclopsgroup.jcli.ArgumentProcessor;
import org.cyclopsgroup.jcli.GnuParser;
import org.cyclopsgroup.jmxterm.SyntaxUtils;
import org.cyclopsgroup.jmxterm.cc.CommandCenter;
import org.cyclopsgroup.jmxterm.cc.ConsoleCompletor;
import org.cyclopsgroup.jmxterm.io.CommandInput;
import org.cyclopsgroup.jmxterm.io.CommandOutput;
import org.cyclopsgroup.jmxterm.io.FileCommandInput;
import org.cyclopsgroup.jmxterm.io.FileCommandOutput;
import org.cyclopsgroup.jmxterm.io.InputStreamCommandInput;
import org.cyclopsgroup.jmxterm.io.JlineCommandInput;
import org.cyclopsgroup.jmxterm.io.PrintStreamCommandOutput;
import org.cyclopsgroup.jmxterm.io.VerboseLevel;

import jline.console.ConsoleReader;
import jline.console.history.FileHistory;

/**
 * Main class invoked directly from command line
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class CliMain
{
    private static final PrintWriter STDOUT_WRITER = new PrintWriter( System.out, true );

    private static final String COMMAND_PROMPT = "$> ";

    /**
     * Main entry
     *
     * @param args Main command
     * @throws Exception
     */
    public static final void main( String[] args )
        throws Exception
    {
        System.exit( new CliMain().execute( args ) );
    }

    /**
     * Execute main class
     *
     * @param args Command line arguments
     * @return Exit code
     * @throws Exception Allow any exceptions
     */
    int execute( String[] args )
        throws Exception
    {
        ArgumentProcessor<CliMainOptions> ap = ArgumentProcessor.newInstance( CliMainOptions.class, new GnuParser() );
        CliMainOptions options = new CliMainOptions();
        ap.process( args, options );
        if ( options.isHelp() )
        {
            ap.printHelp( STDOUT_WRITER );
            return 0;
        }

        VerboseLevel verboseLevel;
        if ( options.getVerboseLevel() != null )
        {
            verboseLevel = VerboseLevel.valueOf( options.getVerboseLevel().toUpperCase() );
        }
        else
        {
            verboseLevel = null;
        }

        CommandOutput output;
        if ( StringUtils.equals( options.getOutput(), CliMainOptions.STDOUT ) )
        {
            output = new PrintStreamCommandOutput( System.out, System.err );
        }
        else
        {
            File outputFile = new File( options.getOutput() );
            output = new FileCommandOutput( outputFile );
        }
        try
        {
            CommandInput input;
            if ( options.getInput().equals( CliMainOptions.STDIN ) )
            {
                if ( options.isNonInteractive() )
                {
                    input = new InputStreamCommandInput( System.in );
                }
                else
                {
                    ConsoleReader consoleReader = new ConsoleReader( System.in, System.err );
                    final FileHistory history = new FileHistory(
                        new File(System.getProperty("user.home"), ".jmxterm_history"));
                    consoleReader.setHistory(history);
                    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                history.flush();
                            }
                            catch (IOException e)
                            {
                                System.err.println("Failed to flush command history! " + e);
                            }
                        }
                    }));
                    input = new JlineCommandInput( consoleReader, COMMAND_PROMPT );
                }
            }
            else
            {
                File inputFile = new File( options.getInput() );
                if ( !inputFile.isFile() )
                {
                    throw new FileNotFoundException( "File " + inputFile + " is not a valid file" );
                }
                input = new FileCommandInput( new File( options.getInput() ) );
            }
            try
            {
                CommandCenter commandCenter = new CommandCenter( output, input );
                if ( input instanceof JlineCommandInput )
                {
                    ( (JlineCommandInput) input ).getConsole().addCompleter(new ConsoleCompletor(commandCenter));
                }
                if ( options.getUrl() != null )
                {
                    Map<String, Object> env;
                    if ( options.getUser() != null )
                    {
                        env = new HashMap<String, Object>( 1 );
                        String password = options.getPassword();
                        if ( password == null )
                        {
                            password = input.readMaskedString( "Authentication password: " );
                        }
                        String[] credentials = { options.getUser(), password };
                        env.put( JMXConnector.CREDENTIALS, credentials );
                    }
                    else
                    {
                        env = null;
                    }
                    commandCenter.connect( SyntaxUtils.getUrl( options.getUrl(), commandCenter.getProcessManager() ),
                                           env );
                }
                if ( verboseLevel != null )
                {
                    commandCenter.setVerboseLevel( verboseLevel );
                }
                if ( verboseLevel != VerboseLevel.SILENT )
                {
                    output.printMessage( "Welcome to JMX terminal. Type \"help\" for available commands." );
                }
                String line;
                int exitCode = 0;
                int lineNumber = 0;
                while ( ( line = input.readLine() ) != null )
                {
                    lineNumber++;
                    if ( !commandCenter.execute( line ) && options.isExitOnFailure() )
                    {
                        exitCode = -lineNumber;
                        break;
                    }
                    if ( commandCenter.isClosed() )
                    {
                        break;
                    }
                }
                commandCenter.close();
                return exitCode;
            }
            finally
            {
                input.close();
            }
        }
        finally
        {
            output.close();
        }
    }
}
