package org.cyclopsgroup.jmxterm.cc;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * Test of {@link JPMFactory}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class JPMFactoryTest {
  /** Verify JPMFactory can create process manager */
  @Test
  public void testLoad() {
    assertNotNull(JPMFactory.createProcessManager());
  }
}
