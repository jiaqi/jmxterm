package org.cyclopsgroup.jmxterm.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Test case of {@link ValueFormat}
 *
 * @author $Author$
 * @version $Revision$ in $Change$ submitted at $DateTime$
 */
class ValueFormatTest {
  /** Test parse method */
  @Test
  void parse() {
    assertNull(ValueFormat.parseValue("null"));
    assertNull(ValueFormat.parseValue(null));
    assertNull(ValueFormat.parseValue(""));
    assertEquals("", ValueFormat.parseValue("\"\""));
    assertEquals("abc", ValueFormat.parseValue("abc"));
    assertEquals("abc", ValueFormat.parseValue("\"abc\""));
    assertEquals("ab c", ValueFormat.parseValue("ab c"));
    assertEquals("ab\nc", ValueFormat.parseValue("ab\\nc"));
    assertEquals("ab\u3160c", ValueFormat.parseValue("ab\\u3160c"));
  }
}
