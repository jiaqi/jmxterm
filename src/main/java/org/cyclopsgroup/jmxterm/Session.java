package org.cyclopsgroup.jmxterm;

import java.io.IOException;
import java.util.Map;

import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jmxterm.io.CommandInput;
import org.cyclopsgroup.jmxterm.io.CommandOutput;
import org.cyclopsgroup.jmxterm.io.UnimplementedCommandInput;
import org.cyclopsgroup.jmxterm.io.VerboseCommandOutput;
import org.cyclopsgroup.jmxterm.io.VerboseCommandOutputConfig;
import org.cyclopsgroup.jmxterm.io.VerboseLevel;

/**
 * JMX communication context. This class exists for the whole lifecycle of a command execution. It is NOT thread safe.
 * The caller(CommandCenter) makes sure all calls are synchronized.
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public abstract class Session
    implements VerboseCommandOutputConfig
{
    private String bean;

    private boolean closed;

    private String domain;

    private final CommandInput input;

    /**
     * Public output field. TODO Reevaluate if this field should be public or exposed by a getter method
     */
    public final CommandOutput output;

    private final JavaProcessManager processManager;
    
    private VerboseLevel verboseLevel = VerboseLevel.BRIEF;

    /**
     * @param output Output destination
     * @param input Command line input
     * @param processManager Process manager
     */
    protected Session( CommandOutput output, CommandInput input, JavaProcessManager processManager )
    {
        Validate.notNull( output, "Output can't be NULL" );
        Validate.notNull( processManager, "Process manager can't be NULL" );
        this.output = new VerboseCommandOutput( output, this );
        this.input = input == null ? new UnimplementedCommandInput() : input;
        this.processManager = processManager;
    }

    /**
     * Close JMX terminal console. Supposedly, process terminates after this call
     */
    public void close()
    {
        if ( closed )
        {
            return;
        }
        closed = true;
    }

    /**
     * Connect to MBean server
     * 
     * @param url URL to connect
     * @param env Environment variables
     * @throws IOException
     */
    public abstract void connect( JMXServiceURL url, Map<String, Object> env )
        throws IOException;

    /**
     * Close JMX connector
     * 
     * @throws IOException Thrown when connection can't be closed
     */
    public abstract void disconnect()
        throws IOException;

    /**
     * @return Current selected bean
     */
    public final String getBean()
    {
        return bean;
    }

    /**
     * @return Current open JMX server connection
     */
    public abstract Connection getConnection();

    /**
     * @return Current domain
     */
    public final String getDomain()
    {
        return domain;
    }

    /**
     * @return General input of command lines, which could be interactive environment, a file or piped input
     */
    public final CommandInput getInput()
    {
        return input;
    }

    /**
     * @return Java process manager to load processes
     */
    public JavaProcessManager getProcessManager()
    {
        return processManager;
    }

    /**
     * {@inheritDoc}
     */
    public final VerboseLevel getVerboseLevel()
    {
        return verboseLevel;
    }

    /**
     * @return True if {@link #close()} has been called
     */
    public final boolean isClosed()
    {
        return closed;
    }

    /**
     * @return True if there's a open connection to JMX server
     */
    public abstract boolean isConnected();

    /**
     * Set current selected bean
     * 
     * @param bean Bean to select
     */
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    /**
     * Set current selected domain
     * 
     * @param domain Domain to select
     */
    public final void setDomain( String domain )
    {
        Validate.notNull( domain, "domain can't be NULL" );
        this.domain = domain;
    }

    /**
     * @param verboseLevel Level of verbose
     */
    public final void setVerboseLevel( VerboseLevel verboseLevel )
    {
        Validate.notNull( verboseLevel, "Verbose level can't be NULL" );
        this.verboseLevel = verboseLevel;
    }

    /**
     * Set domain and bean to be NULL
     */
    public void unsetDomain()
    {
        bean = null;
        domain = null;
    }
}
