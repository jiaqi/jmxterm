package org.cyclopsgroup.jmxterm;

import java.io.IOException;
import java.util.List;

import javax.management.JMException;

import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cyclopsgroup.jcli.AutoCompletable;
import org.cyclopsgroup.jcli.annotation.Option;

/**
 * Base class of all commands. Command is executed in single thread. Extending classes don't need to worry about
 * concurrency. Command is transient, every command in console creates a new instance of Command object which is
 * disposed after execution finishes.
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class Command
    implements AutoCompletable
{
    private static final Log LOG = LogFactory.getLog( Command.class );

    private boolean help;

    private Session session;

    /**
     * Provide a list of possible arguments for auto completion. This method returns list of arguments(not option) and
     * is called when user presses tab key.
     *
     * @return List of possible arguments used by auto completion or NULL
     * @throws IOException IO errors
     * @throws JMException JMX problemo
     */
    protected List<String> doSuggestArgument()
        throws IOException, JMException
    {
        return null;
    }

    /**
     * Provide a list of possible option values for auto completion
     *
     * @param optionName Name of option
     * @return List of possible arguments used by auto completion or NULL
     * @throws IOException Network communication errors
     * @throws JMException JMX errors
     */
    protected List<String> doSuggestOption( String optionName )
        throws IOException, JMException
    {
        return null;
    }

    /**
     * Execute command
     *
     * @throws IOException IO errors
     * @throws JMException JMX errors
     */
    public abstract void execute()
        throws IOException, JMException;

    /**
     * @return Session where command runs
     */
    public final Session getSession()
    {
        return session;
    }

    /**
     * @return True if help option is on
     */
    public final boolean isHelp()
    {
        return help;
    }

    /**
     * @param help True to display usage
     */
    @Option( name = "h", longName = "help", description = "Display usage" )
    public final void setHelp( boolean help )
    {
        this.help = help;
    }

    /**
     * @param session Session where command runs
     */
    public final void setSession( Session session )
    {
        Validate.notNull( session, "Session can't be NULL" );
        this.session = session;
    }

    /**
     * {@inheritDoc}
     */
    public final List<String> suggestArgument( String partialArg )
    {
        if ( partialArg != null )
        {
            return null;
        }
        try
        {
            return doSuggestArgument();
        }
        catch ( IOException e )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Couldn't suggest option", e );
            }
            return null;
        }
        catch ( JMException e )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Couldn't suggest option", e );
            }
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public final List<String> suggestOption( String name, String partialValue )
    {
        if ( partialValue != null )
        {
            return null;
        }
        try
        {
            return doSuggestOption( name );
        }
        catch ( IOException e )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Couldn't suggest option", e );
            }
            return null;
        }
        catch ( JMException e )
        {
            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "Couldn't suggest option", e );
            }
            return null;
        }
    }
}
