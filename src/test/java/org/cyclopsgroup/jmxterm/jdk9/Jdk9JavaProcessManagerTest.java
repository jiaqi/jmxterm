package org.cyclopsgroup.jmxterm.jdk9;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import org.cyclopsgroup.jmxterm.JavaProcess;
import org.junit.jupiter.api.Test;

/**
 * Test case of {@link Jdk9JavaProcessManager}
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
class Jdk9JavaProcessManagerTest {

  @Test
  void construction() {
    Jdk9JavaProcessManager jpm = new Jdk9JavaProcessManager();
    List<JavaProcess> ps = jpm.list();
    assertFalse(ps.isEmpty());
  }
}
