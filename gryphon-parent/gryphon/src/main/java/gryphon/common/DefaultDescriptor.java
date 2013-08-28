package gryphon.common;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Evgueni Tiourine
 */

public class DefaultDescriptor extends ArrayList<DescriptorEntry> implements Descriptor
{
    private HashMap<String, String> values = new HashMap<String, String>();

    public DefaultDescriptor()
    {
    }

    public DescriptorEntry getEntry(int index) throws Exception
    {
        return get(index);
    }

    public void addEntry(DescriptorEntry entry) throws Exception
    {
        add(entry);
    }

    public String getValue(String key) throws Exception
    {
        return values.get(key);
    }

    public void putValue(String key, String value) throws Exception
    {
        values.put(key, value);
    }

}