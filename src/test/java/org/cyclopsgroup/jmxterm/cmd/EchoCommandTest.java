package org.cyclopsgroup.jmxterm.cmd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.management.JMException;
import javax.management.MBeanServerConnection;

import org.apache.commons.lang.SystemUtils;
import org.cyclopsgroup.jmxterm.MockSession;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;


public class EchoCommandTest {
	
	private EchoCommand command;
	
	private Mockery context;

    private StringWriter output;
    
    private void echoText( final List<String> texts, final boolean echoTimestamp, final String timestampFormat, final boolean singleLine, final String delimiter,
    		final String expectedValue )
    {	
    	command.setTexts( texts );
    	command.setEchoTimestamp( echoTimestamp );
    	if ( timestampFormat != null ) 
    	{
    		command.setTimestampFormat(timestampFormat);
    	}
    	command.setSingleLine( singleLine );
    	command.setDelimiter( delimiter );
    	
    	final MBeanServerConnection con = context.mock( MBeanServerConnection.class );
    	
    	try {
			command.setSession( new MockSession( output, con ) );
			command.execute();
			context.assertIsSatisfied();
			assertEquals( expectedValue, output.toString() );
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
        command = new EchoCommand();
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
        echoText( Arrays.asList("a", "b"), false, "", false, "", "ab" + SystemUtils.LINE_SEPARATOR );
    }
    
    /**
     * Test echo timestamp
     */
    @Test
    public void testExecuteWithTimestamp()
    {
    	String format = "dd.MM.yyyy HH:mm:ss";
    	DateFormat df = new SimpleDateFormat(format);
        echoText( new ArrayList<String>(), true, null, false, "", df.format(new Date()) + SystemUtils.LINE_SEPARATOR );
    }
    
    /**
     * Test echo timestamp in custom format
     */
    @Test
    public void testExecuteWithTimestampAndCustomFormat()
    {
    	String format = "dd.MM.yyyy";
    	DateFormat df = new SimpleDateFormat(format);
    	echoText( new ArrayList<String>(), true, format, false, "", df.format(new Date()) + SystemUtils.LINE_SEPARATOR );
    }
    
    /**
     * Test with delimiter set
     */
    @Test
    public void testExecuteWithDelimiter()
    {
    	echoText( Arrays.asList("a", "b"), false, "", false, ",", "a,b" + SystemUtils.LINE_SEPARATOR );
    }
    
    /**
     * Test in singleLine
     */
    @Test
    public void testExecuteWithSingleLineOutput()
    {
    	echoText( Arrays.asList("a", "b"), false, "", true, "", "ab" );
    }

}
