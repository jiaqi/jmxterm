package org.cyclopsgroup.jmxterm.io;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.Test;

/**
 * Test case of {@link PrintStreamCommandOutput}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class PrintStreamCommandOutputTest
{
    /**
     * Write something to output and verify what's written
     */
    @Test
    public void testPrint()
    {
        ByteArrayOutputStream w1 = new ByteArrayOutputStream();
        ByteArrayOutputStream w2 = new ByteArrayOutputStream();
        
        PrintStreamCommandOutput output = new PrintStreamCommandOutput(new PrintStream(w1), new PrintStream(w2));
        output.println( "hello world" );
        output.printMessage( "yeeha" );
        
        assertEquals("hello world", new String(w1.toByteArray()).trim());
        assertEquals("yeeha", new String(w2.toByteArray()).trim());
    }
}
