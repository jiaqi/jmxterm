package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanFeatureInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command that displays attributes and operations of an MBean
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "info", description = "Display detail information about an MBean", note = "If -b option is not specified, current selected MBean is applied" )
public class InfoCommand
    extends Command
{
    private static final Comparator<MBeanFeatureInfo> INFO_COMPARATOR = new Comparator<MBeanFeatureInfo>()
    {
        public int compare( MBeanFeatureInfo o1, MBeanFeatureInfo o2 )
        {
            return new CompareToBuilder().append( o1.getName(), o2.getName() ).append( o1.hashCode(), o2.hashCode() ).toComparison();
        }
    };

    private static final String TEXT_ATTRIBUTES = "# attributes";

    private static final String TEXT_NOTIFICATIONS = "# notifications";

    private static final String TEXT_OPERATIONS = "# operations";

    private String bean;

    private String domain;

    private boolean showDescription;

    private String type = "aon";

    private void displayAttributes( MBeanInfo info )
    {
        Session session = getSession();
        MBeanAttributeInfo[] attrInfos = info.getAttributes();
        if ( attrInfos.length == 0 )
        {
            session.output.printMessage( "there is no attribute" );
            return;
        }
        int index = 0;
        session.output.println( TEXT_ATTRIBUTES );
        List<MBeanAttributeInfo> infos = new ArrayList<MBeanAttributeInfo>( Arrays.asList( attrInfos ) );
        Collections.sort( infos, INFO_COMPARATOR );
        for ( MBeanAttributeInfo attr : infos )
        {
            String rw = "" + ( attr.isReadable() ? "r" : "" ) + ( attr.isWritable() ? "w" : "" );
            session.output.println( String.format( "  %%%-3d - %s (%s, %s)" + ( showDescription ? ", %s" : "" ),
                                                   index++, attr.getName(), attr.getType(), rw, attr.getDescription() ) );
        }
    }

    private void displayNotifications( MBeanInfo info )
    {
        Session session = getSession();
        MBeanNotificationInfo[] notificationInfos = info.getNotifications();
        if ( notificationInfos.length == 0 )
        {
            session.output.printMessage( "there's no notifications" );
            return;
        }
        int index = 0;
        session.output.println( TEXT_NOTIFICATIONS );
        for ( MBeanNotificationInfo notification : notificationInfos )
        {
            session.output.println( String.format( "  %%%-3d - %s(%s)" + ( showDescription ? ", %s" : "" ), index++,
                                                   notification.getName(),
                                                   StringUtils.join( notification.getNotifTypes(), "," ),
                                                   notification.getDescription() ) );
        }

    }

    private void displayOperations( MBeanInfo info )
    {
        Session session = getSession();
        MBeanOperationInfo[] operationInfos = info.getOperations();
        if ( operationInfos.length == 0 )
        {
            session.output.printMessage( "there's no operations" );
            return;
        }
        List<MBeanOperationInfo> operations = new ArrayList<MBeanOperationInfo>( Arrays.asList( operationInfos ) );
        Collections.sort( operations, INFO_COMPARATOR );
        session.output.println( TEXT_OPERATIONS );
        int index = 0;
        for ( MBeanOperationInfo op : operations )
        {
            MBeanParameterInfo[] paramInfos = op.getSignature();
            List<String> paramTypes = new ArrayList<String>( paramInfos.length );
            for ( MBeanParameterInfo paramInfo : paramInfos )
            {
                paramTypes.add( paramInfo.getType() + " " + paramInfo.getName() );
            }
            session.output.println( String.format( "  %%%-3d - %s %s(%s)" + ( showDescription ? ", %s" : "" ), index++,
                                                   op.getReturnType(), op.getName(),
                                                   StringUtils.join( paramTypes, ',' ), op.getDescription() ) );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute()
        throws IOException, JMException
    {
        Session session = getSession();
        String beanName = BeanCommand.getBeanName( bean, domain, session );
        if ( beanName == null )
        {
            throw new IllegalArgumentException( "Please specify a bean using either -b option or bean command" );
        }
        ObjectName name = new ObjectName( beanName );
        MBeanServerConnection con = session.getConnection().getServerConnection();
        MBeanInfo info = con.getMBeanInfo( name );
        session.output.printMessage( "mbean = " + beanName );
        session.output.printMessage( "class name = " + info.getClassName() );
        for ( char t : type.toCharArray() )
        {
            switch ( t )
            {
                case 'a':
                    displayAttributes( info );
                    break;
                case 'o':
                    displayOperations( info );
                    break;
                case 'n':
                    displayNotifications( info );
                    break;
                default:
                    throw new IllegalArgumentException( "Unrecognizable character " + t + " in type option " + type );
            }
        }
    }

    /**
     * @param bean Bean for which information is displayed
     */
    @Option( name = "b", longName = "bean", description = "Name of MBean" )
    public final void setBean( String bean )
    {
        this.bean = bean;
    }

    /**
     * Given domain
     * 
     * @param domain Domain name
     */
    @Option( name = "d", longName = "domain", description = "Domain for bean" )
    public final void setDomain( String domain )
    {
        this.domain = domain;
    }

    /**
     * @param showDescription True to show detail description
     */
    @Option( name = "e", longName = "detail", description = "Show description" )
    public final void setShowDescription( boolean showDescription )
    {
        this.showDescription = showDescription;
    }

    /**
     * @param type Type of detail to display
     */
    @Option( name = "t", longName = "type", description = "Types(a|o|u) to display, for example aon for all attributes, operations and notifications" )
    public void setType( String type )
    {
        Validate.isTrue( StringUtils.isNotEmpty( type ), "Type can't be NULL" );
        Validate.isTrue( Pattern.matches( "^a?o?n?$", type ), "Type must be a?|o?|n?" );
        this.type = type;
    }
}
