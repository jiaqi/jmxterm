package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanFeatureInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;

/**
 * Command that displays attributes and operations of an MBean
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli(
    name = "info",
    description = "Display detail information about an MBean",
    note = "If -b option is not specified, current selected MBean is applied")
public class InfoCommand extends Command {
  private static final Comparator<MBeanFeatureInfo> INFO_COMPARATOR = (o1, o2) -> new CompareToBuilder()
      .append(o1.getName(), o2.getName())
      .append(o1.hashCode(), o2.hashCode())
      .toComparison();

  private static final String TEXT_ATTRIBUTES = "# attributes";

  private static final String TEXT_NOTIFICATIONS = "# notifications";

  private static final String TEXT_OPERATIONS = "# operations";

  private String bean;

  private String domain;

  private boolean showDescription;

  private String type = "aon";

  private String operation;

  private void displayAttributes(MBeanInfo info) {
    Session session = getSession();
    MBeanAttributeInfo[] attrInfos = info.getAttributes();
    if (attrInfos.length == 0) {
      session.output.printMessage("there is no attribute");
      return;
    }
    int index = 0;
    session.output.println(TEXT_ATTRIBUTES);
    List<MBeanAttributeInfo> infos = Stream.of(attrInfos).sorted(INFO_COMPARATOR).toList();
    for (MBeanAttributeInfo attr : infos) {
      String rw = (attr.isReadable() ? "r" : "") + (attr.isWritable() ? "w" : "");
      session.output.println(
          String.format(
              "  %%%-3d - %s (%s, %s)" + (showDescription ? ", %s" : ""),
              index++,
              attr.getName(),
              attr.getType(),
              rw,
              attr.getDescription()));
    }
  }

  private void displayNotifications(MBeanInfo info) {
    Session session = getSession();
    MBeanNotificationInfo[] notificationInfos = info.getNotifications();
    if (notificationInfos.length == 0) {
      session.output.printMessage("there's no notifications");
      return;
    }
    int index = 0;
    session.output.println(TEXT_NOTIFICATIONS);
    for (MBeanNotificationInfo notification : notificationInfos) {
      session.output.println(
          String.format(
              "  %%%-3d - %s(%s)" + (showDescription ? ", %s" : ""),
              index++,
              notification.getName(),
              StringUtils.join(notification.getNotifTypes(), ","),
              notification.getDescription()));
    }
  }

  private void displayOperations(MBeanInfo info) {
    Session session = getSession();
    MBeanOperationInfo[] operationInfos = info.getOperations();
    if (operationInfos.length == 0) {
      session.output.printMessage("there's no operations");
      return;
    }
    List<MBeanOperationInfo> operations = Stream.of(operationInfos).sorted(INFO_COMPARATOR).toList();
    session.output.println(TEXT_OPERATIONS);
    int index = 0;
    for (MBeanOperationInfo op : operations) {
      MBeanParameterInfo[] paramInfos = op.getSignature();
      List<String> paramTypes = new ArrayList<>(paramInfos.length);
      List<String> paramDescriptions = new ArrayList<>(paramInfos.length);
      for (MBeanParameterInfo paramInfo : paramInfos) {
        paramTypes.add(paramInfo.getType() + " " + paramInfo.getName());
        paramDescriptions.add("       " + paramInfo.getName() + ": " + paramInfo.getDescription());
      }
      String parameters = StringUtils.join(paramTypes, ',');
      String parametersDesc =
          paramDescriptions.isEmpty() ? "" : '\n' + StringUtils.join(paramDescriptions, '\n');
      session.output.println(
          String.format(
              "  %%%-3d - %s %s(%s)" + (showDescription ? ", %s%s" : ""),
              index++,
              op.getReturnType(),
              op.getName(),
              parameters,
              op.getDescription(),
              parametersDesc));
    }
  }

  private void displaySingleOperation(MBeanInfo info) {
    Session session = getSession();
    MBeanOperationInfo[] operationInfos = info.getOperations();
    if (operationInfos.length == 0) {
      session.output.printMessage("there's no operations");
      return;
    }
    session.output.println(TEXT_OPERATIONS);
    int index = 0;
    boolean found = false;
    for (MBeanOperationInfo op : operationInfos) {
      String opName = op.getName();
      if (Strings.CS.equals(opName, operation)) {
        found = true;
        MBeanParameterInfo[] paramInfos = op.getSignature();
        List<String> paramTypes = new ArrayList<>(paramInfos.length);
        StringBuilder paramsDesc =
            new StringBuilder("             parameters:" + System.lineSeparator());
        for (MBeanParameterInfo paramInfo : paramInfos) {
          String parameter = paramInfo.getName();
          paramsDesc.append(String.format(
                  "                 + %-20s : %s" + System.lineSeparator(),
                  parameter,
                  paramInfo.getDescription()));
          paramTypes.add(paramInfo.getType() + " " + parameter);
        }
        session.output.println(
            "  %%%-3d - %s %s(%s), %s".formatted(
                index++,
                op.getReturnType(),
                opName,
                StringUtils.join(paramTypes, ','),
                op.getDescription()));
        session.output.println(paramsDesc.toString());
      }
    }
    if (!found) {
      session.output.printMessage(
          "The operation '%s' is not found in the bean.".formatted(operation));
    }
  }

  @Override
  public void execute() throws IOException, JMException {
    Session session = getSession();
    String beanName = BeanCommand.getBeanName(bean, domain, session);
    if (beanName == null) {
      throw new IllegalArgumentException(
          "Please specify a bean using either -b option or bean command");
    }
    ObjectName name = new ObjectName(beanName);
    MBeanServerConnection con = session.getConnection().getServerConnection();
    MBeanInfo info = con.getMBeanInfo(name);
    session.output.printMessage("mbean = " + beanName);
    session.output.printMessage("class name = " + info.getClassName());
    if (this.showDescription) {
      session.output.printMessage("description: " + info.getDescription());
    }
    if (operation == null) {
      for (char t : type.toCharArray()) {
        switch (t) {
          case 'a':
            displayAttributes(info);
            break;
          case 'o':
            displayOperations(info);
            break;
          case 'n':
            displayNotifications(info);
            break;
          default:
            throw new IllegalArgumentException(
                "Unrecognizable character " + t + " in type option " + type);
        }
      }
    } else {
      session.output.printMessage("operation = " + operation);
      displaySingleOperation(info);
    }
  }

  /** @param bean Bean for which information is displayed */
  @Option(name = "b", longName = "bean", description = "Name of MBean")
  public final void setBean(String bean) {
    this.bean = bean;
  }

  /**
   * Given domain
   *
   * @param domain Domain name
   */
  @Option(name = "d", longName = "domain", description = "Domain for bean")
  public final void setDomain(String domain) {
    this.domain = domain;
  }

  /** @param showDescription True to show detail description */
  @Option(name = "e", longName = "detail", description = "Show description")
  public final void setShowDescription(boolean showDescription) {
    this.showDescription = showDescription;
  }

  /** @param type Type of detail to display */
  @Option(
      name = "t",
      longName = "type",
      description =
          "Types(a|o|u) to display, for example aon for all attributes, operations and notifications")
  public void setType(String type) {
    Validate.isTrue(StringUtils.isNotEmpty(type), "Type can't be NULL");
    Validate.isTrue(Pattern.matches("^a?o?n?$", type), "Type must be a?|o?|n?");
    this.type = type;
  }

  @Option(
      name = "o",
      longName = "op",
      description = "Show a single operation with more details (including parameters information)")
  public void setOperation(String operation) {
    Validate.isTrue(StringUtils.isNotEmpty(operation), "Operation can't be NULL");
    this.operation = operation;
  }
}
