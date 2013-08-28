package gryphon.domain;

import java.lang.reflect.*;
import java.util.Date;

import gryphon.*;

/**
 * @author Evgueni Tiourine
 */

public class SimpleEntity implements Entity
{
	private static final long serialVersionUID = 1L;
	
	private Object id;

    public SimpleEntity()
    {
    }

    public Object getAttribute(String name) throws Exception
    {
        try
        {
            Method method = getClass().getMethod("get" + name, null);
            return method.invoke(this, null);
        }
        catch (NoSuchMethodException e)
        {
            throw new NoSuchMethodException(getClass().getName() + ".get"
                    + name + "()");
        }
    }

    public void setAttribute(String name, Object value) throws Exception
    {
        Class[] parameterTypes = new Class[1];
        if (value instanceof Date)
        	parameterTypes[0] = Date.class;
        else if (value!=null)
        	parameterTypes[0] = value.getClass();
        else
        {//value==null
        	char[] cName = name.toCharArray();
        	cName[0] = Character.toLowerCase(cName[0]);
        	Field f = this.getClass().getDeclaredField(new String(cName));
        	parameterTypes[0] = f.getType();
        }
        Object[] args = new Object[1];
        args[0] = value;

        try
        {
            Method method = this.getClass().getMethod("set" + name, parameterTypes);
            method.invoke(this, args);
        }
        catch (NoSuchMethodException e)
        {
            throw new NoSuchMethodException("set" + name + "("+parameterTypes[0].getName()+")");
        }
    }

    public Object getId()
    {
        return this.id;
    }

    public void setId(Object id)
    {
        this.id = id;
    }

}