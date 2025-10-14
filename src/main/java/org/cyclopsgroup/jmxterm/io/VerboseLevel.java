package org.cyclopsgroup.jmxterm.io;

/**
 * Level of verbose option
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public enum VerboseLevel {
  /** Nothing is written out except returned values */
  SILENT,
  /** Print out returned value of messages */
  BRIEF,
  /** Print out returned value of detail of messages */
  VERBOSE
}
