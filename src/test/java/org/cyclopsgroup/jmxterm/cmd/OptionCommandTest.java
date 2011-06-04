package org.cyclopsgroup.jmxterm.cmd;

import java.io.StringWriter;

import org.cyclopsgroup.jcli.annotation.MalformedArgException;
import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link OptionCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class OptionCommandTest
{
    private OptionCommand command;

    private StringWriter output;

    /**
     * Set up object to test
     */
    @Before
    public void setUp()
    {
        command = new OptionCommand();
        output = new StringWriter();
    }

    /**
     * @throws Exception
     */
    @Test( expected = MalformedArgException.class )
    public void testExecuteWithInvalidVerbose()
        throws Exception
    {
        Session session = new MockSession( output, null );
        command.setVerboseLevel( "xyz" );
        command.setSession( session );
        command.execute();
    }
}
