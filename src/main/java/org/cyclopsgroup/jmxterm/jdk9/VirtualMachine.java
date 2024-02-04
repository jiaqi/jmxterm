package org.cyclopsgroup.jmxterm.jdk9;

import java.util.Properties;

/**
 * Reflect class com.sun.tools.attach.VirtualMachine
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
public interface VirtualMachine {
  /** Name of original class this interface reflects */
  String ORIGINAL_CLASS_NAME = "com.sun.tools.attach.VirtualMachine";

  /** Property for the local connector address */
  String LOCAL_CONNECTOR_ADDRESS_PROP = "com.sun.management.jmxremote.localConnectorAddress";

  /** Detach from the virtual machine. */
  void detach();

  /** @return The current agent properties in the target virtual machine. */
  Properties getAgentProperties();

  /** Starts the local JMX management agent in the target virtual machine. */
  void startLocalManagementAgent();
}
