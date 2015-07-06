package org.cyclopsgroup.jmxterm.io;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import jline.console.ConsoleReader;

/**
 * Implementation of input that reads command from jloin console input
 *
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class JlineCommandInput
    extends CommandInput
{
    private final ConsoleReader console;

    private final String prompt;

    /**
     * @param console Jline console reader
     * @param prompt Prompt string
     */
    public JlineCommandInput( ConsoleReader console, String prompt )
    {
        Validate.notNull( console, "Jline console reader can't be NULL" );
        this.console = console;
        this.prompt = StringUtils.trimToEmpty( prompt );
    }

    /**
     * @return Jline console
     */
    public final ConsoleReader getConsole()
    {
        return console;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readLine()
        throws IOException
    {
        return console.readLine( prompt );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readMaskedString( String prompt )
        throws IOException
    {
        return console.readLine( prompt, '*' );
    }

}
