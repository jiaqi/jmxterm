package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertFalse;

import java.io.StringWriter;

import org.cyclopsgroup.jmxterm.MockSession;
import org.junit.Before;
import org.junit.Test;

/**
 * Test of {@link CloseCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class CloseCommandTest
{
    private CloseCommand command;

    private StringWriter output;

    /**
     * Set up classes to test
     */
    @Before
    public void setUp()
    {
        command = new CloseCommand();
        output = new StringWriter();
    }

    /**
     * Test execution
     * 
     * @throws Exception
     */
    @Test
    public void testExecute()
        throws Exception
    {
        MockSession session = new MockSession( output, null );
        command.setSession( session );
        command.execute();
        assertFalse( session.isConnected() );
    }
}
