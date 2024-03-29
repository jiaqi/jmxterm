package org.cyclopsgroup.jmxterm.jdk9;

/**
 * Reflect class com.sun.tools.attach.VirtualMachineDescriptor
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
public interface VirtualMachineDescriptor {
  /** Name of original class this interface reflects */
  String ORIGINAL_CLASS_NAME = "com.sun.tools.attach.VirtualMachineDescriptor";

  /** @return The display name component of this descriptor */
  String displayName();

  /** @return The identifier component of this descriptor */
  String id();
}
