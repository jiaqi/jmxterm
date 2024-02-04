package org.cyclopsgroup.jmxterm.jdk9;

import static org.junit.Assert.assertFalse;

import java.util.List;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.pm.JConsoleClassLoaderFactory;
import org.junit.Test;

/**
 * Test case of {@link Jdk9JavaProcessManager}
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
public class Jdk9JavaProcessManagerTest {

  @Test
  public void testConstruction() throws Exception {
    if (!SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_9)) {
      return;
    }
    Jdk9JavaProcessManager jpm =
        new Jdk9JavaProcessManager(JConsoleClassLoaderFactory.getClassLoader());
    List<JavaProcess> ps = jpm.list();
    assertFalse(ps.isEmpty());
  }
}
