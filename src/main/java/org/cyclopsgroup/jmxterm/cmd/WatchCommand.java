package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.MultiValue;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.io.CommandOutput;
import org.cyclopsgroup.jmxterm.io.JlineCommandInput;

import jline.console.ConsoleReader;

/**
 * Command to watch an MBean attribute TODO Consider the use case for CSV file backend generation
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "watch", description = "Watch the value of one MBean attribute constantly", note = "DO NOT call this command in a script and expect decent output" )
public class WatchCommand
    extends Command
{
    private static class ConsoleOutput
        extends Output
    {
        private final ConsoleReader console;

        private ConsoleOutput( Session session )
        {
            if ( !( session.getInput() instanceof JlineCommandInput ) )
            {
                throw new IllegalStateException( "Under current context, watch command can't execute." );
            }
            this.console = ( (JlineCommandInput) session.getInput() ).getConsole();
        }

        protected void printLine( String line )
            throws IOException
        {
            console.redrawLine();
            console.print( line );
        }
    }

    private static abstract class Output
    {
        protected abstract void printLine( String line )
            throws IOException;
    }

    private static class ReportOutput
        extends Output
    {
        private final CommandOutput out;

        private ReportOutput( Session session )
        {
            this.out = session.output;
        }

        @Override
        protected void printLine( String line )
        {
            out.println( line );
        }

    }

    private static final String BUILDING_ATTRIBUTE_NOW = "%now";

    private static final int DEFAULT_REFRESH_INTERVAL = 1;

    private List<String> attributes = new ArrayList<String>();

    private String outputFormat;

    private int refreshInterval = DEFAULT_REFRESH_INTERVAL;

    private boolean report;

    private int stopAfter;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> doSuggestArgument()
        throws IOException, JMException
    {
        if ( getSession().getBean() != null )
        {
            MBeanServerConnection con = getSession().getConnection().getServerConnection();
            MBeanAttributeInfo[] ais = con.getMBeanInfo( new ObjectName( getSession().getBean() ) ).getAttributes();
            List<String> results = new ArrayList<String>( ais.length );
            for ( MBeanAttributeInfo ai : ais )
            {
                results.add( ai.getName() );
            }
            results.add( BUILDING_ATTRIBUTE_NOW );
            return results;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
        throws IOException, JMException
    {
        if ( report && stopAfter == 0 )
        {
            throw new IllegalArgumentException( "When --report is sepcified, --stopafter(-s) must be specificed" );
        }
        Session session = getSession();
        String domain = DomainCommand.getDomainName( null, session );
        if ( domain == null )
        {
            throw new IllegalStateException( "Please specify a domain using domain command first." );
        }
        String beanName = BeanCommand.getBeanName( null, domain, session );
        if ( beanName == null )
        {
            throw new IllegalStateException( "Please specify a bean using bean command first." );
        }

        final ObjectName name = new ObjectName( beanName );
        final MBeanServerConnection con = session.getConnection().getServerConnection();
        final Output output;
        if ( report )
        {
            output = new ReportOutput( session );
        }
        else
        {
            output = new ConsoleOutput( session );
            getSession().output.printMessage( "press any key to stop. DO NOT press Ctrl+C !!!" );
        }

        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleWithFixedDelay( new Runnable()
        {
            public void run()
            {
                try
                {
                    printValues( name, con, output );
                }
                catch ( IOException e )
                {
                    getSession().output.printError( e );
                }
            }
        }, 0, refreshInterval, TimeUnit.SECONDS );
        if ( stopAfter > 0 )
        {
            executor.schedule( new Runnable()
            {
                public void run()
                {
                    executor.shutdownNow();
                }
            }, stopAfter, TimeUnit.SECONDS );
        }
        if ( !report )
        {
            System.in.read();
            System.out.println();
            executor.shutdownNow();
        }

        session.output.println( "" );
    }

    private Object getAttributeValue( ObjectName beanName, String attributeName, MBeanServerConnection connection )
        throws IOException
    {
        // $now is a reserved keyword for current java.util.Date
        if ( attributeName.equals( BUILDING_ATTRIBUTE_NOW ) )
        {
            return new Date();
        }
        try
        {
            return connection.getAttribute( beanName, attributeName );
        }
        catch ( JMException e )
        {
            return e.getClass().getSimpleName();
        }
    }

    private void printValues( ObjectName beanName, MBeanServerConnection connection, Output output )
        throws IOException
    {
        StringBuffer result = new StringBuffer();
        if ( outputFormat == null )
        {
            boolean first = true;
            for ( String attributeName : attributes )
            {
                if ( first )
                {
                    first = false;
                }
                else
                {
                    result.append( ", " );
                }
                result.append( getAttributeValue( beanName, attributeName, connection ) );
            }
        }
        else
        {
            Object[] values = new Object[attributes.size()];
            int i = 0;
            for ( String attributeNamne : attributes )
            {
                values[i++] = getAttributeValue( beanName, attributeNamne, connection );
            }
            MessageFormat format = new MessageFormat( outputFormat );
            format.format( values, result, new FieldPosition( 0 ) );
        }
        output.printLine( result.toString() );
    }

    /**
     * @param attributes Name of attributes to watch
     */
    @MultiValue( listType = ArrayList.class, minValues = 1 )
    @Argument( displayName = "attr", description = "Name of attributes to watch" )
    public final void setAttributes( List<String> attributes )
    {
        this.attributes = attributes;
    }

    /**
     * @param outputFormat Pattern used in {@link MessageFormat}
     */
    @Option( name = "f", longName = "format", displayName = "expr", description = "Java pattern(java.text.MessageFormat) to print attribute values" )
    public final void setOutputFormat( String outputFormat )
    {
        this.outputFormat = outputFormat;
    }

    /**
     * @param refreshInterval Refreshing interval in seconds
     */
    @Option( name = "i", longName = "interval", displayName = "sec", description = "Optional number of seconds between consecutive poll, default is 1 second", defaultValue = "1" )
    public final void setRefreshInterval( int refreshInterval )
    {
        Validate.isTrue( refreshInterval > 0, "Invalid interval value " + refreshInterval );
        this.refreshInterval = refreshInterval;
    }

    /**
     * @param report True to output result line by line as report
     */
    @Option( name = "r", longName = "report", description = "Output result line by line as report" )
    public final void setReport( boolean report )
    {
        this.report = report;
    }

    /**
     * @param stopAfter After this number of seconds, stop watching
     */
    @Option( name = "s", longName = "stopafter", displayName = "sec", description = "Stop after watching a number of seconds" )
    public final void setStopAfter( int stopAfter )
    {
        Validate.isTrue( stopAfter >= 0, "Invalid stop after argument " + stopAfter );
        this.stopAfter = stopAfter;
    }
}
