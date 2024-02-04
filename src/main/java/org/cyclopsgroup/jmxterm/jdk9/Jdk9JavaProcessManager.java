package org.cyclopsgroup.jmxterm.jdk9;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jmxterm.JavaProcess;
import org.cyclopsgroup.jmxterm.JavaProcessManager;
import org.cyclopsgroup.jmxterm.utils.WeakCastUtils;

/**
 * JDK9 specific java process manager
 *
 * @author <a href="https://github.com/nyg">nyg</a>
 */
public class Jdk9JavaProcessManager extends JavaProcessManager {
  private final StaticVirtualMachine staticVirtualMachine;
  private final Class<?> originalVirtualMachine;

  public Jdk9JavaProcessManager(ClassLoader classLoader)
      throws SecurityException, NoSuchMethodException, ClassNotFoundException {
    Validate.notNull(classLoader, "ClassLoader can't be NULL");
    originalVirtualMachine = classLoader.loadClass(VirtualMachine.ORIGINAL_CLASS_NAME);
    staticVirtualMachine =
        WeakCastUtils.staticCast(originalVirtualMachine, StaticVirtualMachine.class);
  }

  @Override
  public JavaProcess get(int pid) {
    return list().stream().filter(process -> process.getProcessId() == pid).findAny().orElse(null);
  }

  @Override
  public List<JavaProcess> list() {
    List<Object> vmDescriptors = staticVirtualMachine.list();
    List<JavaProcess> result = new ArrayList<>(vmDescriptors.size());

    for (Object vmd : vmDescriptors) {
      VirtualMachineDescriptor vmdProxy = null;
      VirtualMachine vmProxy = null;

      try {
        vmdProxy = WeakCastUtils.cast(vmd, VirtualMachineDescriptor.class);
        Object vm = staticVirtualMachine.attach(vmdProxy.id());
        vmProxy = WeakCastUtils.cast(originalVirtualMachine, vm, VirtualMachine.class);

        Properties agentProps = vmProxy.getAgentProperties();
        String address = (String) agentProps.get(VirtualMachine.LOCAL_CONNECTOR_ADDRESS_PROP);
        result.add(new Jdk9JavaProcess(staticVirtualMachine, vmdProxy, address));
      } catch (SecurityException | NoSuchMethodException e) {
        throw new RuntimeException("Error casting object", e);
      } catch (Exception e) {
        // could not attach or some other exception
        result.add(new Jdk9JavaProcess(staticVirtualMachine, vmdProxy, null));
      } finally {
        if (vmProxy != null) {
          vmProxy.detach();
        }
      }
    }
    return result;
  }
}
