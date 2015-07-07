package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command that shows list of beans
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "beans", description = "List available beans under a domain or all domains", note = "Without -d option, current select domain is applied. If there's no domain specified, all beans are listed. Example:\n beans\n beans -d java.lang" )
public class BeansCommand
    extends Command
{
    /**
     * Get list of bean names under current domain
     *
     * @param session Current JMX session
     * @param domainName Full domain name
     * @return List of bean names
     * @throws MalformedObjectNameException Input domain name is malformed
     * @throws IOException Communication error
     */
    public static List<String> getBeans( Session session, String domainName )
        throws MalformedObjectNameException, IOException
    {
        ObjectName queryName = null;
        if ( domainName != null )
        {
            queryName = new ObjectName( domainName + ":*" );
        }
        Set<ObjectName> names = session.getConnection().getServerConnection().queryNames( queryName, null );
        List<String> results = new ArrayList<String>( names.size() );
        for ( ObjectName name : names )
        {
            results.add( name.getCanonicalName() );
        }
        Collections.sort( results );
        return results;
    }

    private String domain;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> doSuggestOption( String optionName )
        throws IOException
    {
        if ( optionName.equals( "d" ) )
        {
            return DomainsCommand.getCandidateDomains( getSession() );
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
        throws MalformedObjectNameException, IOException
    {
        Session session = getSession();
        String domainName = DomainCommand.getDomainName( domain, session );
        List<String> domains = new ArrayList<String>();
        if ( domainName == null )
        {
            domains.addAll( DomainsCommand.getCandidateDomains( session ) );
        }
        else
        {
            domains.add( domainName );
        }
        for ( String d : domains )
        {
            session.output.printMessage( "domain = " + d + ":" );
            for ( String bean : getBeans( session, d ) )
            {
                session.output.println( bean );
            }
        }
    }

    /**
     * @param domain Domain under which beans are listed
     */
    @Option( name = "d", longName = "domain", displayName = "domain", description = "Name of domain under which beans are listed" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }
}
