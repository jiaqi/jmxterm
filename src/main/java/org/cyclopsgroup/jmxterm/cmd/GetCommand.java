package org.cyclopsgroup.jmxterm.cmd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.RuntimeMBeanException;
import javax.management.openmbean.CompositeDataSupport;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jcli.annotation.Argument;
import org.cyclopsgroup.jcli.annotation.Cli;
import org.cyclopsgroup.jcli.annotation.MultiValue;
import org.cyclopsgroup.jcli.annotation.Option;
import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.Session;
import org.cyclopsgroup.jmxterm.io.ValueOutputFormat;

/**
 * Get value of MBean attribute(s)
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
@Cli(
    name = "get",
    description = "Get value of MBean attribute(s)",
    note = "* stands for all attributes. eg. get Attribute1 Attribute2 or get *")
public class GetCommand extends Command {
  private List<String> attributes = new ArrayList<>();

  private String bean;

  private String domain;

  private boolean singleLine;

  private String delimiter = "";

  private boolean showDescription;

  private boolean showQuotationMarks;

  private boolean simpleFormat;

  private boolean completeLine;

  private void displayAttributes() throws IOException, JMException {
    Session session = getSession();
    String beanName = BeanCommand.getBeanName(bean, domain, session);
    ObjectName name = new ObjectName(beanName);
    session.output.printMessage("mbean = " + beanName + ":");
    MBeanServerConnection con = session.getConnection().getServerConnection();
    MBeanAttributeInfo[] ais = con.getMBeanInfo(name).getAttributes();
    Map<String, MBeanAttributeInfo> attributeNames =
        ListOrderedMap.listOrderedMap(new HashMap<String, MBeanAttributeInfo>());
    if (attributes.contains("*")) {
      for (MBeanAttributeInfo ai : ais) {
        attributeNames.put(ai.getName(), ai);
      }
    } else {
      for (String arg : attributes) {
        String[] attributeNameElements = arg.split("\\.");

        String firstPath = attributeNameElements[0];

        for (MBeanAttributeInfo ai : ais) {
          if (ai.getName().equals(firstPath)) {
            attributeNames.put(arg, ai);
            break;
          }
        }
      }
    }
    ValueOutputFormat format = new ValueOutputFormat(2, showDescription, showQuotationMarks);
    for (Map.Entry<String, MBeanAttributeInfo> entry : attributeNames.entrySet()) {
      String attributeName = entry.getKey();
      MBeanAttributeInfo i = entry.getValue();
      if (i.isReadable()) {
        String[] attributeNameElements = attributeName.split("\\.");

        String attributeNameToRequest = attributeName;
        if (attributeNameElements.length > 1) {
          attributeNameToRequest = attributeNameElements[0];
        }

        Object result = null;

        try {
          result = con.getAttribute(name, attributeNameToRequest);
        } catch (RuntimeMBeanException e) {
          session.output.printMessage(
              "Could not get attribute " + attributeNameToRequest + ": " + e.getMessage());
        }

        if (result instanceof CompositeDataSupport support && attributeNameElements.length > 1) {
            result = support.get(attributeNameElements[1]);
        }

        if (simpleFormat) {
          format.printValue(session.output, result);
        } else if (completeLine) {
          format.printValue(
              session.output,
              "mbean = %s # %s = %s".formatted(beanName, attributeName, result));
        } else {
          format.printExpression(session.output, attributeName, result, i.getDescription());
        }
        session.output.print(delimiter);
        if (!singleLine) {
          session.output.println("");
        }
      } else {
        session.output.printMessage(i.getName() + " is not readable");
      }
    }
  }

  @Override
  public List<String> doSuggestArgument() throws IOException, JMException {
    if (getSession().getBean() != null) {
      MBeanServerConnection con = getSession().getConnection().getServerConnection();
      MBeanAttributeInfo[] ais =
          con.getMBeanInfo(new ObjectName(getSession().getBean())).getAttributes();
      List<String> results = new ArrayList<>(ais.length);
      for (MBeanAttributeInfo ai : ais) {
        results.add(ai.getName());
      }
      return results;
    }
    return null;
  }

  @Override
  protected List<String> doSuggestOption(String optionName) throws JMException {
    if ("d".equals(optionName)) {
      return DomainsCommand.getCandidateDomains(getSession());
    } else if ("b".equals(optionName)) {
      return BeanCommand.getCandidateBeanNames(getSession());
    } else {
      return null;
    }
  }

  @Override
  public void execute() throws JMException, IOException {
    if (attributes.isEmpty()) {
      throw new IllegalArgumentException("Please specify at least one attribute");
    }
    displayAttributes();
  }

  /** @param attributes List of attribute names */
  @MultiValue(listType = ArrayList.class, minValues = 1)
  @Argument(displayName = "attr", description = "Name of attributes to select")
  public final void setAttributes(List<String> attributes) {
    Validate.notNull(attributes, "Attributes can't be NULL");
    this.attributes = attributes;
  }

  /** @param bean Bean under which attribute is get */
  @Option(
      name = "b",
      longName = "bean",
      description = "MBean name where the attribute is. Optional if bean has been set")
  public final void setBean(String bean) {
    this.bean = bean;
  }

  /** @param domain Domain under which bean is selected */
  @Option(name = "d", longName = "domain", description = "Domain of bean, optional")
  public final void setDomain(String domain) {
    this.domain = domain;
  }

  /** @param showDescription True to show detail description */
  @Option(name = "i", longName = "info", description = "Show detail information of each attribute")
  public final void setShowDescription(boolean showDescription) {
    this.showDescription = showDescription;
  }

  /** @param noQuotationMarks True if value is not surrounded by quotation marsk */
  @Option(name = "q", longName = "quots", description = "Quotation marks around value")
  public final void setShowQuotationMarks(boolean noQuotationMarks) {
    this.showQuotationMarks = noQuotationMarks;
  }

  /** @param simpleFormat True if value is printed out in a simple format without full expression */
  @Option(
      name = "s",
      longName = "simple",
      description = "Print simple expression of value without full expression")
  public final void setSimpleFormat(boolean simpleFormat) {
    this.simpleFormat = simpleFormat;
  }

  /**
   * @param completeLine True if value is printed out in a complete &lt;/bean # value&gt; single
   *     line expression
   */
  @Option(
      name = "f",
      longName = "completeLine",
      description = "Print expression with bean and value in single line with '#' delimiter.")
  public final void setCompleteLine(boolean completeLine) {
    this.completeLine = completeLine;
  }

  @Option(
      name = "l",
      longName = "delimiter",
      description = "Sets an optional delimiter to be printed after the value")
  public final void setDelimiter(String delimiter) {
    this.delimiter = delimiter;
  }

  @Option(
      name = "n",
      longName = "singleLine",
      description = "Prints result without a newline - default is false")
  public final void setSingleLine(boolean singleLine) {
    this.singleLine = singleLine;
  }
}
