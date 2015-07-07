package org.cyclopsgroup.jmxterm.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.io.VerboseLevel;

/**
 * Command to change/display console options
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "option", description = "Set options for command session" )
public class OptionCommand
    extends Command
{
    private static final List<String> VERBOSE_LEVEL_VALUES;
    static
    {
        List<String> verboseLevelValues = new ArrayList<String>();
        verboseLevelValues.addAll( VerboseLevel.STRING_NAMES );
        for ( String v : VerboseLevel.STRING_NAMES )
        {
            verboseLevelValues.add( v.toLowerCase() );
        }
        VERBOSE_LEVEL_VALUES = Collections.unmodifiableList( verboseLevelValues );
    }

    private String verboseLevel;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> doSuggestOption( String name )
    {
        return VERBOSE_LEVEL_VALUES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
    {
        Session session = getSession();
        if ( verboseLevel == null )
        {
            session.output.printMessage( "no change for verbose, verbose = " + session.getVerboseLevel() );
        }
        else
        {
            VerboseLevel v = VerboseLevel.valueOf( verboseLevel.toUpperCase() );
            session.setVerboseLevel( v );
            session.output.printMessage( "verbose option is turned to " + v );
        }
    }

    /**
     * @param verbose Verbose level of session
     */
    @Option( name = "v", longName = "verbose", description = "Verbose level: SILENT|BRIEF|VERBOSE" )
    public final void setVerboseLevel( String verbose )
    {
        this.verboseLevel = verbose;
    }
}
