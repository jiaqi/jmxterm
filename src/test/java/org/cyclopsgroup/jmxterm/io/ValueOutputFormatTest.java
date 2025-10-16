package org.cyclopsgroup.jmxterm.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.map.ListOrderedMap;
import org.junit.jupiter.api.Test;

/**
 * Test case for {@link ValueOutputFormat}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class ValueOutputFormatTest {
  /** Print out expression and verify output */
  @Test
  void printExpression() {
    ValueOutputFormat f = new ValueOutputFormat();
    StringWriter out = new StringWriter();
    f.printExpression(new WriterCommandOutput(out), "a", "aaa", "astring");
    String s = out.toString().replaceAll("\\s", "");
    assertEquals("\"a\"=\"aaa\";(astring)", s);
  }

  /** Print out a list value and verify output */
  @Test
  void printList() {
    ValueOutputFormat f = new ValueOutputFormat();
    StringWriter out = new StringWriter();
    f.printValue(new WriterCommandOutput(out), Arrays.asList("abc", "xyz"));
    assertEquals("( \"abc\", \"xyz\" )", out.toString());
  }

  /** Print out a map and verify output */
  @Test
  void printMap() {
    ValueOutputFormat f = new ValueOutputFormat();
    StringWriter out = new StringWriter();
    Map<String, String> map = ListOrderedMap.listOrderedMap(new HashMap<String, String>());
    map.put("a", "aaa");
    map.put("b", "bbb");
    f.printValue(new WriterCommandOutput(out), map);
    String s = out.toString().replaceAll("\\s", "");
    assertEquals("{\"a\"=\"aaa\";\"b\"=\"bbb\";}", s);
  }
}
