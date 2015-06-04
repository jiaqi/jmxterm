package org.cyclopsgroup.jmxterm.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.apache.commons.lang3.Validate;

/**
 * Utility that cast object into given interface(s) even though class doesn't implement interface(s)
 * 
 * @author <a href="mailto:jiaqi.guo@gmail.com">Jiaqi Guo</a>
 */
public final class WeakCastUtils
{
    /**
     * Cast object into multiple interfaces
     * 
     * @param from Object to cast
     * @param interfaces Interfaces to cast to
     * @param classLoader ClassLoader to load methods for invocation
     * @return Result that implements given interfaces
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static Object cast( final Object from, final Class<?>[] interfaces, ClassLoader classLoader )
        throws SecurityException, NoSuchMethodException
    {
        Validate.notNull( from, "Invocation target can't be NULL" );
        Validate.notNull( interfaces, "Interfaces can't be NULL" );
        Validate.notNull( classLoader, "ClassLoader can't be NULL" );
        final Map<Method, Method> methodMap = new HashMap<Method, Method>();
        for ( Class<?> interfase : interfaces )
        {
            Validate.isTrue( interfase.isInterface(), interfase + " is not an interface" );
            for ( Method fromMethod : interfase.getMethods() )
            {
                Method toMethod = from.getClass().getMethod( fromMethod.getName(), fromMethod.getParameterTypes() );
                methodMap.put( fromMethod, toMethod );
            }
        }
        return Proxy.newProxyInstance( classLoader, interfaces, new InvocationHandler()
        {
            public Object invoke( Object proxy, Method method, Object[] args )
                throws Throwable
            {
                Method toMethod = methodMap.get( method );
                if ( toMethod == null )
                {
                    throw new OperationNotSupportedException( "Method " + method + " isn't implemented in " +
                        from.getClass() );
                }
                try
                {
                    if ( ( toMethod.getModifiers() & Modifier.STATIC ) == 0 )
                    {
                        return toMethod.invoke( from, args );
                    }
                    return toMethod.invoke( null, args );
                }
                catch ( InvocationTargetException e )
                {
                    throw e.getCause();
                }
            }
        } );
    }

    /**
     * Cast object to one given interface using ClassLoader of interface
     * 
     * @param <T> Type of interface
     * @param from Object to cast
     * @param interfase Interface to cast to
     * @return Result that implements interface
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static <T> T cast( Object from, Class<T> interfase )
        throws SecurityException, NoSuchMethodException
    {
        return cast( from, interfase, interfase.getClassLoader() );
    }

    /**
     * Cast object to one given interface
     * 
     * @param <T> Type of interface
     * @param from Object to cast
     * @param interfase Interface to cast to
     * @param classLoader Class loader to load invocation methods
     * @return Result that implements interface
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings( "unchecked" )
    public static <T> T cast( Object from, Class<T> interfase, ClassLoader classLoader )
        throws SecurityException, NoSuchMethodException
    {
        Validate.notNull( interfase, "Interface can't be NULL" );
        return (T) cast( from, new Class<?>[] { interfase }, classLoader );
    }

    /**
     * Cast static methods of a class to given interfaces
     * 
     * @param from From class
     * @param interfaces To interfaces
     * @param classLoader Class loader
     * @return Casted result
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static Object staticCast( final Class<?> from, final Class<?>[] interfaces, ClassLoader classLoader )
        throws SecurityException, NoSuchMethodException
    {
        Validate.notNull( from, "Invocation target type can't be NULL" );
        Validate.notNull( interfaces, "Interfaces can't be NULL" );
        Validate.notNull( classLoader, "ClassLoader can't be NULL" );
        final Map<Method, Method> methodMap = new HashMap<Method, Method>();
        for ( Class<?> interfase : interfaces )
        {
            Validate.isTrue( interfase.isInterface(), interfase + " is not an interface" );
            for ( Method fromMethod : interfase.getMethods() )
            {
                Method toMethod = from.getMethod( fromMethod.getName(), fromMethod.getParameterTypes() );
                if ( ( toMethod.getModifiers() & Modifier.STATIC ) == 0 )
                {
                    throw new NoSuchMethodException( "Method " + toMethod + " isn't static" );
                }
                methodMap.put( fromMethod, toMethod );
            }
        }
        return Proxy.newProxyInstance( classLoader, interfaces, new InvocationHandler()
        {
            public Object invoke( Object proxy, Method method, Object[] args )
                throws Throwable
            {
                Method toMethod = methodMap.get( method );
                if ( toMethod == null )
                {
                    throw new OperationNotSupportedException( "Method " + method + " isn't implemented in " +
                        from.getClass() );
                }
                try
                {
                    return toMethod.invoke( null, args );
                }
                catch ( InvocationTargetException e )
                {
                    throw e.getCause();
                }
            }
        } );
    }

    /**
     * Cast static class to one given interface
     * 
     * @param <T> Type of interface to casts to
     * @param from Type of static class to casts from
     * @param interfase Interface to cast to
     * @return Result that implements interface
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    public static <T> T staticCast( Class<?> from, Class<T> interfase )
        throws SecurityException, NoSuchMethodException
    {
        return staticCast( from, interfase, interfase.getClassLoader() );
    }

    /**
     * Cast static class to one given interface
     * 
     * @param <T> Type of interface to casts to
     * @param from Type of static class to casts from
     * @param interfase Interface to cast to
     * @param classLoader Class loader to load result
     * @return Result that implements interface
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @SuppressWarnings( "unchecked" )
    public static <T> T staticCast( Class<?> from, Class<T> interfase, ClassLoader classLoader )
        throws SecurityException, NoSuchMethodException
    {
        return (T) staticCast( from, new Class<?>[] { interfase }, classLoader );
    }

    private WeakCastUtils()
    {
    }
}
