package org.cyclopsgroup.jmxterm.jdk9;

import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jmxterm.JavaProcess;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

/**
 * JDK9 specific implementation of {@link JavaProcess}
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
public class Jdk9JavaProcess implements JavaProcess {

  private final VirtualMachineDescriptor vmd;
  private final String address;

  /**
   * @param vmd Local VM
   * @param address Connector address, if any
   */
  Jdk9JavaProcess(VirtualMachineDescriptor vmd, String address) {
    Validate.notNull(vmd, "VirtualMachineDescriptor can't be NULL");
    this.vmd = vmd;
    this.address = address;
  }

  @Override
  public String getDisplayName() {
    return vmd.displayName();
  }

  @Override
  public int getProcessId() {
    return Integer.parseInt(vmd.id());
  }

  @Override
  public boolean isManageable() {
    return address != null;
  }

  @Override
  public void startManagementAgent() throws IOException {
    try {
      VirtualMachine.attach(vmd).startLocalManagementAgent();
    } catch (SecurityException | AttachNotSupportedException e) {
      throw new IllegalStateException("Cannot start management agent on VM with pid " + vmd.id(), e);
    }
  }

  @Override
  public String toUrl() {
    return address;
  }
}
