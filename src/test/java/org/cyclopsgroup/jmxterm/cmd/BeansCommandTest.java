package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link BeansCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class BeansCommandTest
{
    private BeansCommand command;

    private MBeanServerConnection conn;

    private Mockery context;

    private StringWriter output;

    /**
     * Set up testing connection
     */
    @Before
    public void setUp()
    {
        output = new StringWriter();
        context = new Mockery();
        command = new BeansCommand();
        conn = context.mock( MBeanServerConnection.class );
    }

    /**
     * Test execution and get all beans
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithAllBeans()
        throws Exception
    {
        context.checking( new Expectations()
        {
            {
                one( conn ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "a:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "a:type=1" ),
                                                                           new ObjectName( "a:type=2" ) ) ) ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "b:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "b:type=1" ) ) ) ) );
            }
        } );
        command.setSession( new MockSession( output, conn ) );
        command.execute();
        context.assertIsSatisfied();
        assertEquals( "a:type=1\na:type=2\nb:type=1\n", output.toString() );
    }

    /**
     * Test execution where domain is set in session
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithDomainInSession()
        throws Exception
    {
        context.checking( new Expectations()
        {
            {
                allowing( conn ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "b:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "b:type=1" ) ) ) ) );
            }
        } );
        MockSession session = new MockSession( output, conn );
        session.setDomain( "b" );
        command.setSession( session );
        command.execute();
        context.assertIsSatisfied();
        assertEquals( "b:type=1\n", output.toString() );
    }

    /**
     * Test execution with an domain option
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithDomainOption()
        throws Exception
    {
        command.setDomain( "b" );
        context.checking( new Expectations()
        {
            {
                allowing( conn ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "b:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "b:type=1" ) ) ) ) );
            }
        } );
        command.setSession( new MockSession( output, conn ) );
        command.execute();
        context.assertIsSatisfied();
        assertEquals( "b:type=1\n", output.toString() );
    }

    /**
     * Test execution with domain NULL
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithNullDomain()
        throws Exception
    {
        command.setDomain( "*" );
        context.checking( new Expectations()
        {
            {
                one( conn ).getDomains();
                will( returnValue( new String[] { "a", "b" } ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "a:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "a:type=1" ),
                                                                           new ObjectName( "a:type=2" ) ) ) ) );
                atLeast( 1 ).of( conn ).queryNames( new ObjectName( "b:*" ), null );
                will( returnValue( new HashSet<ObjectName>( Arrays.asList( new ObjectName( "b:type=1" ) ) ) ) );
            }
        } );
        MockSession session = new MockSession( output, conn );
        session.setDomain( "b" );
        command.setSession( session );
        command.execute();
        context.assertIsSatisfied();
        assertEquals( "a:type=1\na:type=2\nb:type=1\n", output.toString() );
    }
}
