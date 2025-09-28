package org.cyclopsgroup.jmxterm.cc;

import org.cyclopsgroup.jmxterm.JavaProcessManager;
import org.cyclopsgroup.jmxterm.jdk9.Jdk9JavaProcessManager;

/**
 * Internal factory class to create JPM instance
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public final class JPMFactory {

  /** Default constructor that figures out an implementation of JPM */
  private JPMFactory() {
    throw new UnsupportedOperationException("Not instantiable");
  }

  /** @return Java process manager instance */
  static JavaProcessManager createProcessManager() {
    return new Jdk9JavaProcessManager();
  }
}
