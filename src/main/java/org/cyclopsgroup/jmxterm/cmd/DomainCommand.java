package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;

/**
 * Get or set current selected domain
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "domain", description = "Display or set current selected domain. ", note = "With a parameter, parameter defined domain is selected, otherwise it displays current selected domain."
    + " eg. domain java.lang" )
public class DomainCommand
    extends Command
{
    /**
     * Get domain name from given domain expression
     *
     * @param domain Domain expression, which can be a name or NULL
     * @param session Current JMX session
     * @return String name of domain coming from given parameter or current session
     * @throws IOException
     */
    static String getDomainName( String domain, Session session )
        throws IOException
    {
        Validate.notNull( session, "Session can't be NULL" );
        Validate.isTrue( session.getConnection() != null, "Session isn't opened" );
        if ( domain == null )
        {
            return session.getDomain();
        }
        if ( SyntaxUtils.isNull( domain ) )
        {
            return null;
        }
        HashSet<String> domains = new HashSet<String>( DomainsCommand.getCandidateDomains( session ) );
        if ( !domains.contains( domain ) )
        {
            throw new IllegalArgumentException( "Domain " + domain + " doesn't exist, check your spelling" );
        }
        return domain;
    }

    private String domain;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> doSuggestArgument()
        throws IOException
    {
        return DomainsCommand.getCandidateDomains( getSession() );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
        throws IOException
    {
        Session session = getSession();
        if ( domain == null )
        {
            if ( session.getDomain() == null )
            {
                session.output.printMessage( "domain is not set" );
                session.output.println( SyntaxUtils.NULL );
            }
            else
            {
                session.output.printMessage( "domain = " + session.getDomain() );
                session.output.println( session.getDomain() );
            }
            return;
        }
        String domainName = getDomainName( domain, session );
        if ( domainName == null )
        {
            session.unsetDomain();
            session.output.printMessage( "domain is unset" );
        }
        else
        {
            session.setDomain( domainName );
            session.output.printMessage( "domain is set to " + session.getDomain() );
        }
    }

    /**
     * @param domain Domain to select
     */
    @Argument( displayName = "domain", description = "Name of domain to set" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }
}
