package org.cyclopsgroup.jmxterm.io;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

/**
 * Unit test case for class {@link InputStreamCommandInput}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class InputStreamCommandInputTest
{
    /**
     * Read from string line by line and verify result
     * 
     * @throws IOException If stream operation fails
     */
    @Test
    public void testRead()
        throws IOException
    {
        String input = "aaaa\nbbbb";
        InputStreamCommandInput in = new InputStreamCommandInput( new ByteArrayInputStream( input.getBytes() ) );
        assertEquals( "aaaa", in.readLine() );
        assertEquals( "bbbb", in.readLine() );
    }
}
