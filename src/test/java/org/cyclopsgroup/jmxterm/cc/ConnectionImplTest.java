package org.cyclopsgroup.jmxterm.cc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;

import javax.management.remote.JMXConnector;

import org.cyclopsgroup.jmxterm.SyntaxUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

/**
 * Test case of {@link ConnectionImpl}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class ConnectionImplTest
{
    /**
     * Test the object is constructed correctly
     * 
     * @throws IOException
     */
    @Test
    public void testConstruction()
        throws IOException
    {
        Mockery context = new Mockery();
        final JMXConnector con = context.mock( JMXConnector.class );
        ConnectionImpl c = new ConnectionImpl( con, SyntaxUtils.getUrl( "localhost:9991", null ) );
        assertSame( con, c.getConnector() );

        context.checking( new Expectations()
        {
            {
                one( con ).getConnectionId();
                will( returnValue( "xyz" ) );
            }
        } );
        assertEquals( "xyz", c.getConnectorId() );
        context.assertIsSatisfied();
    }
}
