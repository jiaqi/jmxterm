package org.cyclopsgroup.jmxterm.cc;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Test of {@link JPMFactory}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class JPMFactoryTest {
  /** Verify JPMFactory can create process manager */
  @Test
  void load() {
    assertNotNull(JPMFactory.createProcessManager());
  }
}
