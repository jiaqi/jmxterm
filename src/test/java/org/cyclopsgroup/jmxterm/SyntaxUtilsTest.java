package org.cyclopsgroup.jmxterm;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Test case of {@link SyntaxUtils}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class SyntaxUtilsTest {
  /**
   * Test how getUrl() figure out MBeanServer URL based on various pattern of input
   *
   * @throws IOException Thrown when syntax is invalid
   */
  @Test
  void getUrl() throws Exception {
    assertEquals(
        "/jndi/rmi://xyz-host.cyclopsgroup.org:12345/jmxrmi",
        SyntaxUtils.getUrl("xyz-host.cyclopsgroup.org:12345", null).getURLPath());
    assertEquals(
        "/jndi/rmi://xyz-host.cyclopsgroup.org:12345/jmxrmi",
        SyntaxUtils.getUrl(
                "service:jmx:rmi:///jndi/rmi://xyz-host.cyclopsgroup.org:12345/jmxrmi", null)
            .getURLPath());
  }

  /** Verify string expression of type is correctly parsed */
  @Test
  void parseNormally() {
    assertEquals("x", SyntaxUtils.parse("x", "java.lang.String"));
    assertEquals(3, SyntaxUtils.parse("3", "int"));
    assertEquals(3L, SyntaxUtils.parse("3", "long"));
    assertEquals("", SyntaxUtils.parse("", "java.lang.String"));
    assertNull(SyntaxUtils.parse("", "java.util.Date"));
    assertNull(SyntaxUtils.parse("null", "java.lang.String"));
  }

  /** Verify that Exception is thrown when type is wrong */
  @Test
  void parseWithWrongType() {
    assertThrows(IllegalArgumentException.class, () ->
      SyntaxUtils.parse("x", "x"));
  }
}
