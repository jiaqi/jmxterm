package org.cyclopsgroup.jmxterm.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Enumeration;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.Validate;

/**
 * Utilities for loading overlapping properties files from classpath
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public final class ConfigurationUtils {
  /**
   * @param resourcePath Path of overlapping properties files
   * @param classLoader Class loader where the resources are loaded
   * @return Configuration result
   * @throws IOException allows IO exceptions.
   */
  public static Configuration loadFromOverlappingResources(
      String resourcePath, ClassLoader classLoader) throws IOException {
    Validate.notNull(resourcePath, "Resource path can't be NULL");
    Validate.notNull(classLoader, "ClassLoader can't be NULL");
    PropertiesConfiguration props = new PropertiesConfiguration();
    props.setListDelimiterHandler(new DefaultListDelimiterHandler(','));
    Enumeration<URL> resources = classLoader.getResources(resourcePath);
    while (resources.hasMoreElements()) {
      InputStream resource = resources.nextElement().openStream();
      try (Reader reader = new InputStreamReader(resource)){
        props.read(reader);
      } catch (ConfigurationException e) {
        throw new IOException(e);
      }
    }
    return props;
  }

  private ConfigurationUtils() {}
}
