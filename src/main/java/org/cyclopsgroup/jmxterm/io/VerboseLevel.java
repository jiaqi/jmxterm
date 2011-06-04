package org.cyclopsgroup.jmxterm.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Level of verbose option
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public enum VerboseLevel
{
    /**
     * Nothing is written out except returned values
     */
    SILENT,
    /**
     * Print out returned value of messages
     */
    BRIEF,
    /**
     * Print out returned value of detail of messages
     */
    VERBOSE;

    /**
     * List of enum names as String
     */
    public static final List<String> STRING_NAMES;
    static
    {
        VerboseLevel[] values = values();
        List<String> stringValues = new ArrayList<String>( values.length );
        for ( VerboseLevel value : values )
        {
            stringValues.add( value.name() );
        }
        STRING_NAMES = Collections.unmodifiableList( stringValues );
    }
}
