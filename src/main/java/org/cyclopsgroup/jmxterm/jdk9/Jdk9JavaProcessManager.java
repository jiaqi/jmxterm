package org.cyclopsgroup.jmxterm.jdk9;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.JavaProcessManager;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * JDK9 specific java process manager
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
public class Jdk9JavaProcessManager extends JavaProcessManager {

  /** Property for the local connector address */
  private static final String LOCAL_CONNECTOR_ADDRESS_PROP = "com.sun.management.jmxremote.localConnectorAddress";

  @Override
  public JavaProcess get(int pid) {
    return list().stream()
        .filter(process -> process.getProcessId() == pid)
        .findAny()
        .orElse(null);
  }

  @Override
  public List<JavaProcess> list() {
    List<VirtualMachineDescriptor> vmDescriptors = VirtualMachine.list();
    List<JavaProcess> javaProcesses = new ArrayList<>(vmDescriptors.size());

    for (VirtualMachineDescriptor vmd : vmDescriptors) {
      VirtualMachine vm = null;
      try {
        vm = VirtualMachine.attach(vmd);
        Properties agentProps = vm.getAgentProperties();
        String address = agentProps.getProperty(LOCAL_CONNECTOR_ADDRESS_PROP);
        javaProcesses.add(new Jdk9JavaProcess(vmd, address));
      } catch (AttachNotSupportedException | IOException e) {
        // could not attach or some other exception
        javaProcesses.add(new Jdk9JavaProcess(vmd, null));
      } finally {
        if (vm != null) {
          try {
            vm.detach();
          } catch (IOException e) {
            // Could not detach from the VM, ignoring as we cannot do anything about it
          }
        }
      }
    }

    return javaProcesses;
  }
}
