package org.cyclopsgroup.jmxterm.jdk5;

import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jmxterm.JavaProcess;

/**
 * JDK5 specific implementation of {@link JavaProcess}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class Jdk5JavaProcess
    implements JavaProcess
{
    private final String command;

    private final ConnectorAddressLink connectorAddressLink;

    private final int processId;

    private String url;

    /**
     * @param processId Platform PID of process
     * @param command Original command
     * @param connectorAddressLink A connector link
     */
    Jdk5JavaProcess( int processId, String command, ConnectorAddressLink connectorAddressLink )
    {
        Validate.isTrue( processId > 0, "Invalid process ID " + processId );
        Validate.notNull( command, "Command line can't be NULL" );
        Validate.notNull( connectorAddressLink, "connectorAddressLink can't be NULL" );
        this.processId = processId;
        this.command = command;
        this.connectorAddressLink = connectorAddressLink;
    }

    /**
     * {@inheritDoc}
     */
    public String getDisplayName()
    {
        return command;
    }

    /**
     * {@inheritDoc}
     */
    public int getProcessId()
    {
        return processId;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isManageable()
    {
        return url != null;
    }

    /**
     * {@inheritDoc}
     */
    public void startManagementAgent()
        throws IOException
    {
        if ( url != null )
        {
            return;
        }
        url = connectorAddressLink.importFrom( processId );
    }

    /**
     * {@inheritDoc}
     */
    public String toUrl()
    {
        return url;
    }
}
