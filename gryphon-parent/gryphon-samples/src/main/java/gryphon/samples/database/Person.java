package gryphon.samples.database;

import java.util.Date;

import gryphon.domain.SimpleEntity;

public class Person extends SimpleEntity
{

	private static final long serialVersionUID = 1L;
	
	private String name;

	private String email;
	
	private Date lastUpdate;
	
	private String phone;

	private Boolean hidden;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Date getLastUpdate()
	{
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate)
	{
		this.lastUpdate = lastUpdate;
	}

	public Boolean getHidden()
	{
		return hidden;
	}

	public void setHidden(Boolean hidden)
	{
		this.hidden = hidden;
	}

}
