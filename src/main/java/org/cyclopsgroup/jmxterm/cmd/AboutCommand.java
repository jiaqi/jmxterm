package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.Map;

import javax.management.JMException;

import org.apache.commons.collections.ExtendedProperties;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.io.ValueOutputFormat;
import org.cyclopsgroup.jmxterm.utils.ExtendedPropertiesUtils;

/**
 * Command to show about page
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "about", description = "Display about page" )
public class AboutCommand
    extends Command
{
    private boolean showDescription;

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public void execute()
        throws IOException, JMException
    {
        Session session = getSession();
        // output predefined about properties
        ExtendedProperties props =
            ExtendedPropertiesUtils.loadFromOverlappingResources( "META-INF/cyclopsgroup/jmxterm.properties",
                                                                  getClass().getClassLoader() );
        ValueOutputFormat format = new ValueOutputFormat( 2, showDescription, true );
        for ( Object entryObject : props.subset( "jmxterm.about" ).entrySet() )
        {
            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) entryObject;
            format.printExpression( session.output, entry.getKey(), entry.getValue(), null );
        }

        // output Java runtime properties
        for ( Map.Entry<Object, Object> entry : System.getProperties().entrySet() )
        {
            String keyName = entry.toString();
            if ( keyName.startsWith( "java." ) )
            {
                format.printExpression( session.output, keyName, entry.getValue(), null );
            }
        }
    }

    /**
     * @param showDescription True to show detail description
     */
    @Option( name = "s", longName = "show", description = "Show detail description" )
    public final void setShowDescription( boolean showDescription )
    {
        this.showDescription = showDescription;
    }
}
