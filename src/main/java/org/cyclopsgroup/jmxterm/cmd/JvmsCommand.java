package org.cyclopsgroup.jmxterm.cmd;

import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.Session;

import javax.management.JMException;
import java.io.IOException;
import java.util.List;

/**
 * Command to list all running local JVM processes
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli( name = "jvms", description = "List all running local JVM processes" )
public class JvmsCommand
    extends Command
{
    private boolean pidOnly;

    @Override
    public void execute()
        throws IOException, JMException
    {
        Session session = getSession();
        List<JavaProcess> processList = session.getProcessManager().list();
        for ( JavaProcess p : processList )
        {
            if ( pidOnly )
            {
                session.output.println( String.valueOf( p.getProcessId() ) );
            }
            else
            {

                session.output.println( String.format( "%-8d (%s) - %s", p.getProcessId(),
                                                       p.isManageable() ? "m" : " ", p.getDisplayName() ) );
            }
        }
    }

    /**
     * @param pidOnly Flag to notify command to only print out PID instead of more details
     */
    @Option( name = "p", longName = "pidonly", description = "Only print out PID" )
    public final void setPidOnly( boolean pidOnly )
    {
        this.pidOnly = pidOnly;
    }
}
