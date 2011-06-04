package org.cyclopsgroup.jmxterm.utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Test case of {@link WeakCastUtils}
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class WeakCastUtilsTest
{
    /**
     * Test casing a {@link SizeAware} interface
     * 
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    public void testCast()
        throws SecurityException, NoSuchMethodException
    {
        SizeAware s = WeakCastUtils.cast( new ArrayList<Integer>( Arrays.asList( 1, 2, 3 ) ), SizeAware.class );
        assertEquals( 3, s.size() );
    }
}
