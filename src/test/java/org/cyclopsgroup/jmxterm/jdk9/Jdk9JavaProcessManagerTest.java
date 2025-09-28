package org.cyclopsgroup.jmxterm.jdk9;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.cyclopsgroup.jmxterm.JavaProcess;
import org.junit.Test;

/**
 * Test case of {@link Jdk9JavaProcessManager}
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
public class Jdk9JavaProcessManagerTest {

  @Test
  public void testConstruction() throws Exception {
    Jdk9JavaProcessManager jpm = new Jdk9JavaProcessManager();
    List<JavaProcess> ps = jpm.list();
    assertFalse(ps.isEmpty());
  }
}
