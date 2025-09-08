package org.cyclopsgroup.jmxterm.jdk9;

import java.io.IOException;
import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.utils.WeakCastUtils;

/**
 * JDK9 specific implementation of {@link JavaProcess}
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
public class Jdk9JavaProcess implements JavaProcess {

  private final StaticVirtualMachine staticVirtualMachine;
  private final VirtualMachineDescriptor vmd;
  private final String address;

  /**
   * @param staticVm Static VirtualMachine proxy
   * @param vmd Local VM
   * @param address Connector address, if any
   */
  Jdk9JavaProcess(StaticVirtualMachine staticVm, VirtualMachineDescriptor vmd, String address) {
    Validate.notNull(vmd, "StaticVirtualMachine can't be NULL");
    Validate.notNull(vmd, "VirtualMachineDescriptor can't be NULL");
    this.staticVirtualMachine = staticVm;
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
    Object vm = staticVirtualMachine.attach(vmd.id());
    try {
      Class<?> originalVirtualMachine = Class.forName(VirtualMachine.ORIGINAL_CLASS_NAME);
      VirtualMachine vmProxy = WeakCastUtils.cast(originalVirtualMachine, vm, VirtualMachine.class);
      vmProxy.startLocalManagementAgent();
    } catch (ClassNotFoundException | SecurityException | NoSuchMethodException e) {
      throw new RuntimeException("Can't cast " + vm + " to VirtualMachineDescriptor", e);
    }
  }

  @Override
  public String toUrl() {
    return address;
  }
}
