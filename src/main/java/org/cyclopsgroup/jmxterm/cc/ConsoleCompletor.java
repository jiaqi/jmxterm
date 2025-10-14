package org.cyclopsgroup.jmxterm.cc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.cyclopsgroup.jcli.jline.CliCompletor;
import org.cyclopsgroup.jmxterm.Command;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JLine completor that handles tab key
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class ConsoleCompletor implements Completer {
  private static final Logger LOG = LoggerFactory.getLogger(ConsoleCompletor.class);

  private final CommandCenter commandCenter;

  private final List<Candidate> commandNames;

  public ConsoleCompletor(CommandCenter commandCenter) {
    Validate.notNull(commandCenter, "Command center can't be NULL");
    this.commandCenter = commandCenter;
    this.commandNames = commandCenter.getCommandNames().stream()
        .sorted()
        .map(Candidate::new)
        .toList();
  }

  @Override
  public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
    try {
      String buffer = line.line();
      if (StringUtils.isEmpty(buffer) || buffer.indexOf(' ') == -1) {
        completeCommandName(buffer, candidates);
      }
      int separatorPos = buffer.indexOf(' ');
      String commandName = buffer.substring(0, separatorPos);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Command name is [{}]", commandName);
      }
      String commandArguments = buffer.substring(separatorPos + 1);
      commandArguments.replaceFirst("^\\s*", "");
      if (LOG.isDebugEnabled()) {
        LOG.debug("Analyzing command arguments [{}]", commandArguments);
      }
      Command cmd = commandCenter.commandFactory.createCommand(commandName);
      cmd.setSession(commandCenter.session);
      CliCompletor commandCompletor = new CliCompletor(cmd, commandCenter.argTokenizer);
      int position = line.cursor();
      commandCompletor.complete(
          reader,
          new ArgumentCompleter.ArgumentLine(commandArguments, position - separatorPos),
          candidates);
    } catch (RuntimeException e) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Couldn't complete input", e);
      }
    }
  }

  private void completeCommandName(String buf, List<Candidate> candidates) {
    if (buf == null) {
      // Nothing is there
      candidates.addAll(commandNames);
    } else if (buf.indexOf(' ') == -1) {
      // Partial one word
      List<Candidate> matchedNames = new ArrayList<>();
      for (Candidate commandName : commandNames) {
        if (commandName.value().startsWith(buf)) {
          matchedNames.add(commandName);
        }
      }
      candidates.addAll(matchedNames);
    } else {
      throw new IllegalStateException("Invalid state");
    }
  }
}
