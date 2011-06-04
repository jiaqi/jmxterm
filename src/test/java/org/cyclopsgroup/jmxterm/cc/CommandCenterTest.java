package org.cyclopsgroup.jmxterm.cc;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.SelfRecordingCommand;
import org.cyclopsgroup.jmxterm.cc.CommandCenter;
import org.cyclopsgroup.jmxterm.cc.TypeMapCommandFactory;
import org.cyclopsgroup.jmxterm.io.WriterCommandOutput;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case of {@link CommandCenter}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class CommandCenterTest
{
    private CommandCenter cc;

    private List<Command> executedCommands;

    private StringWriter output;

    private String getArgsFromList( int index )
    {
        SelfRecordingCommand c = (SelfRecordingCommand) executedCommands.get( index );
        return c.getArgs();
    }

    /**
     * Set up objects to test
     * 
     * @throws IOException
     */
    @Before
    public void setUp()
        throws IOException
    {
        executedCommands = new ArrayList<Command>();
        output = new StringWriter();

        Map<String, Class<? extends Command>> commandTypes = new HashMap<String, Class<? extends Command>>();
        commandTypes.put( "test", SelfRecordingCommand.class );
        cc = new CommandCenter( new WriterCommandOutput( output ), null, new TypeMapCommandFactory( commandTypes )
        {
            @Override
            public Command createCommand( String commandName )
            {
                return new SelfRecordingCommand( executedCommands );
            }
        } );
    }

    /**
     * Verify the execution
     */
    @Test
    public void testExecute()
    {
        cc.execute( "test 1" );
        cc.execute( "test 2 a b && test 3" );
        cc.execute( "# test 4" );
        cc.execute( "test 5 # test 6" );

        assertEquals( 4, executedCommands.size() );
        assertEquals( "1", getArgsFromList( 0 ) );
        assertEquals( "2 a b", getArgsFromList( 1 ) );
        assertEquals( "3", getArgsFromList( 2 ) );
        assertEquals( "5", getArgsFromList( 3 ) );
    }
}
