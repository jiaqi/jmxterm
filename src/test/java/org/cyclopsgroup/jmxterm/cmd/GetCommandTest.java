package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.lang.RandomStringUtils;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case of {@link GetCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class GetCommandTest
{
    private GetCommand command;

    private Mockery context;

    private StringWriter output;

    private void getAttributeAndVerify( final String domain, String bean, final String attribute,
                                        final String expectedBean, final Object expectedValue )
    {
        command.setDomain( domain );
        command.setBean( bean );
        command.setAttributes( Arrays.asList( attribute ) );
        command.setSimpleFormat( true );

        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        final MBeanInfo beanInfo = context.mock( MBeanInfo.class );
        final MBeanAttributeInfo attributeInfo = context.mock( MBeanAttributeInfo.class );
        try
        {
            context.checking( new Expectations()
            {
                {
                    one( con ).getDomains();
                    will( returnValue( new String[] { domain, RandomStringUtils.randomAlphabetic( 5 ) } ) );
                    one( con ).getMBeanInfo( new ObjectName( expectedBean ) );
                    will( returnValue( beanInfo ) );
                    one( beanInfo ).getAttributes();
                    will( returnValue( new MBeanAttributeInfo[] { attributeInfo } ) );
                    allowing( attributeInfo ).getName();
                    will( returnValue( attribute ) );
                    allowing( attributeInfo ).isReadable();
                    will( returnValue( true ) );
                    one( con ).getAttribute( new ObjectName( expectedBean ), attribute );
                    will( returnValue( expectedValue ) );
                }
            } );
            command.setSession( new MockSession( output, con ) );
            command.execute();
            context.assertIsSatisfied();
            assertEquals( expectedValue.toString(), output.toString().trim() );
        }
        catch ( JMException e )
        {
            throw new RuntimeException( "Test failed for unexpected JMException", e );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Test failed for unexpected IOException", e );
        }
    }

    /**
     * Set up class to test
     */
    @Before
    public void setUp()
    {
        command = new GetCommand();
        context = new Mockery();
        context.setImposteriser( ClassImposteriser.INSTANCE );
        output = new StringWriter();
    }

    /**
     * Test normal execution
     */
    @Test
    public void testExecuteNormally()
    {
        getAttributeAndVerify( "a", "type=x", "a", "a:type=x", "bingo" );
    }

    /**
     * Verify non string type is formatted into string
     */
    @Test
    public void testExecuteWithNonStringType()
    {
        getAttributeAndVerify( "a", "type=x", "a", "a:type=x", new Integer( 10 ) );
    }

    /**
     * Verify attribute name with dash, underline and dot is acceptable
     */
    @Test
    public void testExecuteWithStrangeAttributeName()
    {
        getAttributeAndVerify( "a", "type=x", "a_b-c.d", "a:type=x", "bingo" );
    }

    /**
     * Verify unusual bean name and domain name is acceptable
     */
    @Test
    public void testExecuteWithUnusualDomainAndBeanName()
    {
        getAttributeAndVerify( "a-a", "a.b-c_d=x-y.z", "a", "a-a:a.b-c_d=x-y.z", "bingo" );
    }
}
