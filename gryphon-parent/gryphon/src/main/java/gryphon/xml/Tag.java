package gryphon.xml;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Tag{
	public StringBuffer text = new StringBuffer();
	public String name;
	public boolean hasBody;
	public ArrayList<Tag> children = new ArrayList<Tag>();
	public ArrayList<NameValue> attributes = new ArrayList<NameValue>();
	private static final String LN = "\n";
	public Tag(String name){
		this.name = name;
	}
	public NameValue getAttribute(String name)
	{
		for (Iterator<NameValue> iterator = attributes.iterator(); iterator.hasNext();)
		{
			NameValue nv = iterator.next();
			if (nv.name.equals(name))
				return nv;
		}
		return null;
	}
	public Tag getChild(String name)
	{
		for (Iterator<Tag> iterator = children.iterator(); iterator.hasNext();)
		{
			Tag tag = iterator.next();
			if (tag.name.equals(name))
				return tag;
		}
		return null;
	}
	public Tag(String name, Tag child)
	{
		this(name);
		children.add(child);
	}
	public Tag(String name, String text)
	{
		this(name);
		this.text.append(text);
	}
	public Tag(String name, String aname, String avalue)
	{
		this(name);
		addAttribute(aname, avalue);
	}
	public void addAttribute(String name, String value)
	{
		attributes.add(new NameValue(name, value));
	}
	public String toString()
	{
		return "<"+name+">"+text.toString()+"</"+name+">";
	}
	public void printTree(PrintStream ps, String indent)
	{
		if (name.length()>0)
			ps.print(indent+"<"+name);
		if (attributes.size()>0)
			ps.print(" ");
		for (int i = 0; i < attributes.size(); i++)
		{
			NameValue a = attributes.get(i);
			ps.print(a.name+"=\""+a.value+"\"");
			if (i<attributes.size()-1)
				ps.print(" ");
		}
		if (children.size()==0 && text.length()==0){
			if (name.length()>0)
				ps.print("/>"+LN);
		}
		else{
			String ln = children.size()>0?LN:"";
			if (name.length()>0)
				ps.print(">"+ln);
			ps.print(text);
			for (int i = 0; i < children.size(); i++)
			{
				Tag child = children.get(i);
				child.printTree(ps, indent+"\t");
			}
			if (name.length()>0){
				String ind = children.size()>0 ? indent : "";  
				ps.print(ind+"</"+name+">"+LN);
			}
		}
	}

	public String getText()
	{
		if (getChild("")==null)
			return "";
		return getChild("").text.toString();
	}

}