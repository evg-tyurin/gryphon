package gryphon.xml;

public class NameValue{
	public String name;
	public String value;
	public NameValue(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	public String toString()
	{
		return name+"="+value;
	}
	
}