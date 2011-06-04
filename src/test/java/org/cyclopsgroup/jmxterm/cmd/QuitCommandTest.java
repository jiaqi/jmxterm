package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link QuitCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class QuitCommandTest
{
    private QuitCommand command;

    private StringWriter output;

    /**
     * Setup objects to test
     */
    @Before
    public void setUp()
    {
        command = new QuitCommand();
        output = new StringWriter();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExecute()
        throws Exception
    {
        Session session = new MockSession( output, null );
        command.setSession( session );
        command.execute();
        assertFalse( session.isConnected() );
        assertTrue( session.isClosed() );
    }
}
