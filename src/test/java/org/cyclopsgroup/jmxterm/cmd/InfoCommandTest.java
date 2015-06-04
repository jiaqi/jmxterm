package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.lang3.SystemUtils;
import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.Session;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link InfoCommand}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class InfoCommandTest
{
    private InfoCommand command;

    private Mockery context;

    private StringWriter output;

    /**
     * Set up objects to test
     */
    @Before
    public void setUp()
    {
        command = new InfoCommand();
        output = new StringWriter();
        context = new Mockery();
        context.setImposteriser( ClassImposteriser.INSTANCE );
    }

    /**
     * Test how attributes are displayed
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithShowingAttributes()
        throws Exception
    {
        command.setBean( "a:type=x" );
        command.setType( "a" );
        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        final MBeanInfo beanInfo = context.mock( MBeanInfo.class );
        final MBeanAttributeInfo attributeInfo = context.mock( MBeanAttributeInfo.class );
        Session session = new MockSession( output, con );
        context.checking( new Expectations()
        {
            {
                atLeast( 1 ).of( con ).getMBeanInfo( new ObjectName( "a:type=x" ) );
                will( returnValue( beanInfo ) );
                allowing( beanInfo ).getClassName();
                will( returnValue( "bogus class" ) );
                one( beanInfo ).getAttributes();
                will( returnValue( new MBeanAttributeInfo[] { attributeInfo } ) );
                atLeast( 1 ).of( attributeInfo ).isReadable();
                will( returnValue( true ) );
                atLeast( 1 ).of( attributeInfo ).isWritable();
                will( returnValue( false ) );
                atLeast( 1 ).of( attributeInfo ).getName();
                will( returnValue( "b" ) );
                atLeast( 1 ).of( attributeInfo ).getType();
                will( returnValue( "int" ) );
                allowing( attributeInfo ).getDescription();
                will( returnValue( "bingo" ) );
            }
        } );
        command.setSession( session );
        command.execute();
        context.assertIsSatisfied();
        assertEquals( "# attributes" + SystemUtils.LINE_SEPARATOR + "  %0   - b (int, r)", output.toString().trim() );
    }

    /**
     * Test execution and show available options
     * 
     * @throws Exception
     */
    @Test
    public void testExecuteWithShowingOperations()
        throws Exception
    {
        command.setBean( "a:type=x" );
        command.setType( "o" );
        final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
        final MBeanInfo beanInfo = context.mock( MBeanInfo.class );
        final MBeanOperationInfo opInfo = context.mock( MBeanOperationInfo.class );
        final MBeanParameterInfo paramInfo = context.mock( MBeanParameterInfo.class );
        Session session = new MockSession( output, con );
        context.checking( new Expectations()
        {
            {
                atLeast( 1 ).of( con ).getMBeanInfo( new ObjectName( "a:type=x" ) );
                will( returnValue( beanInfo ) );
                allowing( beanInfo ).getClassName();
                will( returnValue( "bogus class" ) );
                one( beanInfo ).getOperations();
                will( returnValue( new MBeanOperationInfo[] { opInfo } ) );
                allowing( opInfo ).getDescription();
                will( returnValue( "bingo" ) );
                one( opInfo ).getSignature();
                will( returnValue( new MBeanParameterInfo[] { paramInfo } ) );
                one( paramInfo ).getType();
                will( returnValue( String.class.getName() ) );
                one( paramInfo ).getName();
                will( returnValue( "a" ) );
                one( opInfo ).getReturnType();
                will( returnValue( "int" ) );
                atLeast( 1 ).of( opInfo ).getName();
                will( returnValue( "x" ) );
            }
        } );
        command.setSession( session );
        command.execute();
        context.assertIsSatisfied();
        assertEquals( "# operations" + SystemUtils.LINE_SEPARATOR + "  %0   - int x(java.lang.String a)",
                      output.toString().trim() );
    }
}
