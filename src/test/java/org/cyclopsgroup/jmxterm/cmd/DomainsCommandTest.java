package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import javax.management.MBeanServerConnection;

import org.apache.commons.lang3.SystemUtils;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case of {@link DomainsCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class DomainsCommandTest
{
    private DomainsCommand command;

    private Mockery context;

    /**
     * Set up objects to test
     */
    @Before
    public void setUp()
    {
        command = new DomainsCommand();
        context = new Mockery();
    }

    /**
     * Test normal execution
     * 
     * @throws Exception
     */
    @Test
    public void testExecution()
        throws Exception
    {
        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        StringWriter output = new StringWriter();
        context.checking( new Expectations()
        {
            {
                one( con ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
            }
        } );
        command.setSession( new MockSession( output, con ) );
        command.execute();
        context.assertIsSatisfied();
        assertEquals( "a" + SystemUtils.LINE_SEPARATOR + "b", output.toString().trim() );
    }
}
