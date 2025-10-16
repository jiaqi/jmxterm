package org.cyclopsgroup.jmxterm.cc;

import static org.cyclopsgroup.jmxterm.cc.CommandCenter.ESCAPE_CHAR_REGEX;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cyclopsgroup.jmxterm.Command;
import org.cyclopsgroup.jmxterm.SelfRecordingCommand;
import org.cyclopsgroup.jmxterm.io.WriterCommandOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test case of {@link CommandCenter}
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
class CommandCenterTest {
  private CommandCenter cc;

  private List<Command> executedCommands;

  private StringWriter output;

  private String getArgsFromList(int index) {
    return getRecordedCommand(index).getArgs();
  }

  private SelfRecordingCommand getRecordedCommand(int index) {
    return (SelfRecordingCommand) executedCommands.get(index);
  }

  private void runCommandAndVerifyArguments(String command, List<String> expectedArguments) {
    cc.execute(command);
    assertEquals(expectedArguments, getRecordedCommand(0).getArguments());
  }

  /**
   * Set up objects to test
   *
   * @throws IOException
   */
  @BeforeEach
  void setUp() {
    executedCommands = new ArrayList<>();
    output = new StringWriter();

    Map<String, Class<? extends Command>> commandTypes = new HashMap<>();
    commandTypes.put("test", SelfRecordingCommand.class);
    cc =
        new CommandCenter(
            new WriterCommandOutput(output),
            null,
            new TypeMapCommandFactory(commandTypes) {
              @Override
              public Command createCommand(String commandName) {
                return new SelfRecordingCommand(executedCommands);
              }
            });
  }

  /** Verify the execution */
  @Test
  void execute() {
    cc.execute("test 1");
    cc.execute("test 2 a b && test 3");
    cc.execute("# test 4");
    cc.execute("test 5 # test 6");

    assertEquals(4, executedCommands.size());
    assertEquals("1", getArgsFromList(0));
    assertEquals("2 a b", getArgsFromList(1));
    assertEquals("3", getArgsFromList(2));
    assertEquals("5", getArgsFromList(3));
  }

  @Test
  void multipleArguments() {
    runCommandAndVerifyArguments("test a b c d", Arrays.asList("a", "b", "c", "d"));
  }

  @Test
  void multipleEscapedArguments() {
    runCommandAndVerifyArguments("test a\\ \\ b \\-3\\ ,4", Arrays.asList("a  b", "-3 ,4"));
  }

  @Test
  void singleArgumentWithEscape() {
    runCommandAndVerifyArguments("test \\-1", Arrays.asList("-1"));
  }

  @Test
  void singleArgumentWithSpace() {
    runCommandAndVerifyArguments("test a\\ b\\ c\\ d", Arrays.asList("a b c d"));
  }

  @Test
  void singleSimpleArgument() {
    runCommandAndVerifyArguments("test 1", Arrays.asList("1"));
  }

  @Test
  void regexEscapesCorrectly() {
    final String s1 = "".split(ESCAPE_CHAR_REGEX)[0];
    final String s2 = "a b c".split(ESCAPE_CHAR_REGEX)[0];
    final String s3 = "a #b c".split(ESCAPE_CHAR_REGEX)[0];
    final String s4 = "a #b c #".split(ESCAPE_CHAR_REGEX)[0];
    final String s5 = "a \\#b c #".split(ESCAPE_CHAR_REGEX)[0];
    final String s6 = "a #b c \\# something".split(ESCAPE_CHAR_REGEX)[0];

    assertEquals("", s1);
    assertEquals("a b c", s2);
    assertEquals("a ", s3);
    assertEquals("a ", s4);
    assertEquals("a \\#b c ", s5);
    assertEquals("a ", s6);
  }
}
