package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.MultiValue;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.SyntaxUtils;
import org.cyclopsgroup.jmxterm.utils.ValueFormat;

/**
 * Command to set an attribute
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "set", description = "Set value of an MBean attribute" )
public class SetCommand
    extends Command
{
    private List<String> arguments = Collections.emptyList();

    private String bean;

    private String domain;

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> doSuggestArgument()
        throws IOException, JMException
    {
        Session session = getSession();
        if ( session.getBean() != null )
        {
            MBeanServerConnection conn = getSession().getConnection().getServerConnection();
            MBeanInfo info = conn.getMBeanInfo( new ObjectName( session.getBean() ) );
            MBeanAttributeInfo[] attrs = info.getAttributes();
            List<String> attributeNames = new ArrayList<String>( attrs.length );
            for ( MBeanAttributeInfo attr : attrs )
            {
                attributeNames.add( attr.getName() );
            }
            return attributeNames;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected List<String> doSuggestOption( String optionName )
        throws JMException
    {
        if ( optionName.equals( "d" ) )
        {
            return DomainsCommand.getCandidateDomains( getSession() );
        }
        else if ( optionName.equals( "b" ) )
        {
            return BeanCommand.getCandidateBeanNames( getSession() );
        }
        else
        {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
        throws JMException, IOException
    {
        Validate.isTrue( arguments.size() >= 2, "At least two arguments are required" );
        Session session = getSession();
        String attributeName = arguments.get( 0 );

        String beanName = BeanCommand.getBeanName( bean, domain, session );
        ObjectName name = new ObjectName( beanName );

        MBeanServerConnection con = session.getConnection().getServerConnection();
        MBeanInfo beanInfo = con.getMBeanInfo( new ObjectName( beanName ) );
        MBeanAttributeInfo attributeInfo = null;
        for ( MBeanAttributeInfo i : beanInfo.getAttributes() )
        {
            if ( i.getName().equals( attributeName ) )
            {
                attributeInfo = i;
                break;
            }
        }
        if ( attributeInfo == null )
        {
            throw new IllegalArgumentException( "Attribute " + attributeName + " is not specified" );
        }
        if ( !attributeInfo.isWritable() )
        {
            throw new IllegalArgumentException( "Attribute " + attributeName + " is not writable" );
        }
        String inputValue = arguments.get( 1 );
        if ( inputValue != null )
        {
            inputValue = ValueFormat.parseValue( inputValue );
        }
        Object value = SyntaxUtils.parse( inputValue, attributeInfo.getType() );
        con.setAttribute( name, new Attribute( attributeName, value ) );
        session.output.printMessage( "Value of attribute " + attributeName + " is set to " + inputValue );
    }

    /**
     * @param arguments Argument list. The first argument is attribute name
     */
    @MultiValue( listType = ArrayList.class, minValues = 2 )
    @Argument( description = "name, value, value2..." )
    public final void setArguments( List<String> arguments )
    {
        Validate.notNull( arguments, "Arguments can't be NULL" );
        this.arguments = arguments;
    }

    /**
     * @param bean Bean where the attribute is
     */
    @Option( name = "b", longName = "bean", description = "MBean name where the attribute is. Optional if bean has been set" )
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    /**
     * @param domain Domain where the bean is
     */
    @Option( name = "d", longName = "domain", description = "Domain under which the bean is" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }
}
