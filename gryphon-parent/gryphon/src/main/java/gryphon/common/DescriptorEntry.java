package gryphon.common;

import java.util.HashMap;

/**
 * @author Evgueni Tiourine
 */

public class DescriptorEntry
{
    public static final int TYPE_TEXT = 1;

    public static final int TYPE_CHECKBOX = 2;

    public static final int TYPE_RADIO = 3;

    public static final int TYPE_SHORT = 4;

    public static final int TYPE_INTEGER = 5;

    public static final int TYPE_LONG = 6;

    public static final int TYPE_DATE = 7;
    
    /** ����, ��� ������� � Entry �������� ��� ������ ��������� */ 
	public static final String RENDERER = "renderer";
    /** ����, ��� ������� � Entry �������� ��� ������ ������� */ 
	public static final String EDITOR = "editor";

    /**
     * ��������� �����, ���������� ����� ��������
     */
    private String label;

    /**
     * ��� ��������, � �������� ���� ����� ��������
     */
    private String attribute;

    /**
     * ��� �������� ��� �����������: �����, ���-����, �����-������ � ��.
     */
    private int type = TYPE_TEXT;

    /**
     * ������� ��������������� ��������
     */
    private boolean editable = false;

    private HashMap<String, Object> values;

    public DescriptorEntry(String label, String attribute)
    {
        setLabel(label);
        setAttribute(attribute);
    }

    public DescriptorEntry(String label, String attribute, int type,
            boolean isEditable)
    {
        this(label, attribute);
        setType(type);
        setEditable(isEditable);
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getAttribute()
    {
        return attribute;
    }

    public void setAttribute(String attribute)
    {
        this.attribute = attribute;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }

    public Object getValue(String key)
    {
        if (values != null)
        {
            return values.get(key);
        }
        return null;
    }

    public int getIntValue(String key)
    {
        return Integer.parseInt((String) getValue(key));
    }

    public boolean getBooleanValue(String key)
    {
        Object value = getValue(key);
        if (value == null)
        {
            return false;
        }
        return Boolean.valueOf((String) value).booleanValue();
    }

    public void putValue(String key, Object value)
    {
        if (values == null)
        {
            values = new HashMap<String, Object>();
        }
        values.put(key, value);
    }

    public void putIntValue(String key, int i)
    {
        putValue(key, String.valueOf(i));
    }
}