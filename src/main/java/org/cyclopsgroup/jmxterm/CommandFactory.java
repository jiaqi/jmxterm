package org.cyclopsgroup.jmxterm;

import java.util.Map;

/**
 * Factory which create Command instance based on command name
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public interface CommandFactory
{
    /**
     * Create new command instance
     * 
     * @param name Command name
     * @return New instance of command object
     */
    Command createCommand( String name );

    /**
     * @return Map of command types
     */
    Map<String, Class<? extends Command>> getCommandTypes();
}
