package org.cyclopsgroup.jmxterm.cc;

import static org.junit.Assert.assertEquals;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang3.SystemUtils;
import org.cyclopsgroup.jmxterm.MockSession;
import org.cyclopsgroup.jmxterm.SelfRecordingCommand;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for {@link HelpCommand}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class HelpCommandTest
{
    private HelpCommand command;

    private Mockery context;

    private StringWriter output;

    /**
     * Set up objects to test
     */
    @Before
    public void setUp()
    {
        command = new HelpCommand();
        output = new StringWriter();
        context = new Mockery();
        context.setImposteriser( ClassImposteriser.INSTANCE );
    }

    /**
     * Test execution with several options
     *
     * @throws IOException
     * @throws IntrospectionException
     */
    @Test
    public void testExecuteWithOption()
        throws IOException, IntrospectionException
    {
        command.setArgNames( Arrays.asList( "a", "b" ) );
        final CommandCenter cc = context.mock( CommandCenter.class );
        command.setCommandCenter( cc );

        context.checking( new Expectations()
        {
            {
                one( cc ).getCommandType( "a" );
                will( returnValue( SelfRecordingCommand.class ) );
                one( cc ).getCommandType( "b" );
                will( returnValue( SelfRecordingCommand.class ) );
            }
        } );
        command.setSession( new MockSession( output, null ) );
        command.execute();
        context.assertIsSatisfied();
    }

    /**
     * Test execution without option
     *
     * @throws IOException
     */
    @Test
    public void testExecuteWithoutOption()
        throws IOException
    {
        final CommandCenter cc = context.mock( CommandCenter.class );
        command.setCommandCenter( cc );
        context.checking( new Expectations()
        {
            {
                one( cc ).getCommandNames();
                will( returnValue( new HashSet<String>( Arrays.asList( "a", "b" ) ) ) );
                one( cc ).getCommandType( "a" );
                will( returnValue( SelfRecordingCommand.class ) );
                one( cc ).getCommandType( "b" );
                will( returnValue( SelfRecordingCommand.class ) );
            }
        } );
        command.setSession( new MockSession( output, null ) );
        command.execute();
        assertEquals( "a        - desc" + SystemUtils.LINE_SEPARATOR + "b        - desc", output.toString().trim() );
    }
}
