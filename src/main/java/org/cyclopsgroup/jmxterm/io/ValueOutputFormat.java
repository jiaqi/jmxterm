package org.cyclopsgroup.jmxterm.io;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

import javax.management.openmbean.CompositeData;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * A utility to print out object values in particular format.
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public class ValueOutputFormat
{
    private final int indentSize;

    private final boolean showDescription;

    private final boolean showQuotationMarks;

    /**
     * Default constructor with indentiation = 2, showDescription and showQuotationMarks are both true
     */
    public ValueOutputFormat()
    {
        this( 2, true, true );
    }

    /**
     * @param indentSize Size of indentation
     * @param showDescription True if value description is printed
     * @param showQuotationMarks True if quotation mark is printed
     */
    public ValueOutputFormat( int indentSize, boolean showDescription, boolean showQuotationMarks )
    {
        Validate.isTrue( indentSize >= 0, "Invalid indent size value " + indentSize );
        this.indentSize = indentSize;
        this.showDescription = showDescription;
        this.showQuotationMarks = showQuotationMarks;
    }

    /**
     * Print out equal expression of an variable with description
     * 
     * @param output Output to print to
     * @param name Name of variable
     * @param value Value of variable
     * @param description Description of variable
     */
    public void printExpression( CommandOutput output, Object name, Object value, String description )
    {
        printExpression( output, name, value, description, 0 );
    }

    /**
     * Print out equal expression of an variable with description
     * 
     * @param output Output to print to
     * @param name Name of variable
     * @param value Value of variable
     * @param description Description of variable
     * @param indent Starting indent position
     */
    private void printExpression( CommandOutput output, Object name, Object value, String description, int indent )
    {
        output.print( StringUtils.repeat( " ", indent ) );
        printValue( output, name, indent );
        output.print( " = " );
        printValue( output, value, indent );
        output.print( ";" );
        if ( showDescription && description != null )
        {
            output.print( " (" + description + ")" );
        }
        output.println( "" );
    }

    /**
     * @param output Output writer where value is printed to
     * @param value Value to print
     */
    public void printValue( CommandOutput output, Object value )
    {
        printValue( output, value, 0 );
    }

    /**
     * Print out expression of given value considering various possible types of value
     * 
     * @param output Output writer where value is printed
     * @param value Object value which can be anything
     * @param indent Starting indentation length
     */
    private void printValue( CommandOutput output, Object value, int indent )
    {
        if ( value == null )
        {
            output.print( "null" );
        }
        else if ( value.getClass().isArray() )
        {
            int length = Array.getLength( value );
            output.print( "[ " );
            for ( int i = 0; i < length; i++ )
            {
                if ( i != 0 )
                {
                    output.print( ", " );
                }
                printValue( output, Array.get( value, i ), indent );
            }
            output.print( " ]" );
        }
        else if ( Collection.class.isAssignableFrom( value.getClass() ) )
        {
            boolean start = true;
            output.print( "( " );
            for ( Object obj : ( (Collection<?>) value ) )
            {
                if ( !start )
                {
                    output.print( ", " );
                }
                start = false;
                printValue( output, obj, indent );
            }
            output.print( " )" );
        }
        else if ( Map.class.isAssignableFrom( value.getClass() ) )
        {
            output.println( "{ " );
            for ( Map.Entry<?, ?> entry : ( (Map<?, ?>) value ).entrySet() )
            {
                printExpression( output, entry.getKey(), entry.getValue(), null, indent + indentSize );
            }
            output.print( StringUtils.repeat( " ", indent ) + " }" );
        }
        else if ( CompositeData.class.isAssignableFrom( value.getClass() ) )
        {
            output.println( "{ " );
            CompositeData data = (CompositeData) value;
            for ( Object key : data.getCompositeType().keySet() )
            {
                Object v = data.get( (String) key );
                printExpression( output, key, v, data.getCompositeType().getDescription( (String) key ), indent
                    + indentSize );
            }
            output.print( StringUtils.repeat( " ", indent ) + " }" );
        }
        else if ( value instanceof String && showQuotationMarks )
        {
            output.print( "\"" + value + "\"" );
        }
        else
        {
            output.print( value.toString() );
        }
    }
}
