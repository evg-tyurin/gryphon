package gryphon.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author Evgueni Tiourine
 */

public class DefaultContext implements Context
{
    private HashMap<Object, Object> properties = new HashMap<Object, Object>();

    public DefaultContext()
    {
    }

    public void setProperty(Object key, Object value)
    {
        properties.put(key, value);
    }

    public Object getProperty(Object key) throws Exception
    {
        return properties.get(key);
    }

	public Enumeration getPropertyNames()
	{
		return new Vector(properties.keySet()).elements();
	}

}