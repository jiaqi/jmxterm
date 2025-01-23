package org.cyclopsgroup.jmxterm.jdk9;

import java.util.List;

/**
 * Static interface of com.sun.tools.attach.VirtualMachine
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
public interface StaticVirtualMachine {
  /** @return List of all virtual machines running on local */
  List<Object> list();

  /** Attaches to a Java virtual machine. */
  Object attach(String id);
}
