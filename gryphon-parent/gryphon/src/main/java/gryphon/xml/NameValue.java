package gryphon.xml;

import java.io.Serializable;

public class NameValue implements Serializable
{
	public String name;
	public String value;

	public NameValue(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String toString()
	{
		return name + "=" + value;
	}

}