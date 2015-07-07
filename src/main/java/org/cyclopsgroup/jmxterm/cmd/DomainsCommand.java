package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.io.RuntimeIOException;

/**
 * List domains for JMX connection
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "domains", description = "List all available domain names" )
public class DomainsCommand
    extends Command
{
    /**
     * Get list of domains for current JMX connection
     *
     * @param session Current session
     * @return Sorted list of domain names
     */
    static List<String> getCandidateDomains( Session session )
    {
        String[] domains;
        try
        {
            domains = session.getConnection().getServerConnection().getDomains();
        }
        catch ( IOException e )
        {
            throw new RuntimeIOException( "Couldn't get candate domains", e );
        }
        List<String> result = new ArrayList<String>( Arrays.asList( domains ) );
        Collections.sort( result );
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
        throws IOException
    {
        Session session = getSession();

        session.output.printMessage( "following domains are available" );
        for ( String domain : getCandidateDomains( session ) )
        {
            session.output.println( domain );
        }
    }
}