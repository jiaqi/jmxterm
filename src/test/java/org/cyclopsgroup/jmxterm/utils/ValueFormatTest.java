package org.cyclopsgroup.jmxterm.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test case of {@link ValueFormat}
 *
 * @author $Author$
 * @version $Revision$ in $Change$ submitted at $DateTime$
 */
public class ValueFormatTest
{
    /**
     * Test parse method
     */
    @Test
    public void testParse()
    {
        assertEquals(null, ValueFormat.parseValue("null"));
        assertEquals(null, ValueFormat.parseValue(null));
        assertEquals(null, ValueFormat.parseValue(""));
        assertEquals("", ValueFormat.parseValue("\"\""));
        assertEquals("abc", ValueFormat.parseValue("abc"));
        assertEquals("abc", ValueFormat.parseValue("\"abc\""));
        assertEquals("ab c", ValueFormat.parseValue("ab c"));
        assertEquals("ab\nc", ValueFormat.parseValue("ab\\nc"));
        assertEquals("ab\u3160c", ValueFormat.parseValue("ab\\u3160c"));
    }
}
