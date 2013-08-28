package gryphon.samples.database;

import java.util.Date;

import gryphon.domain.SimpleEntity;

public class Person extends SimpleEntity
{

	private static final long serialVersionUID = 1L;
	
	private String name;

	private String email;
	
	private Date last_update;
	
	private String phone;
	private String initials;
	private String userId;
	private String  password;
	private byte hidden;

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

	public Date getLast_update()
	{
		return last_update;
	}

	public void setLast_update(Date last_update)
	{
		this.last_update = last_update;
	}

	public String getInitials()
	{
		return initials;
	}

	public void setInitials(String initials)
	{
		this.initials = initials;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public byte getHidden()
	{
		return hidden;
	}

	public void setHidden(Byte hidden)
	{
		this.hidden = hidden.byteValue();
	}

}
