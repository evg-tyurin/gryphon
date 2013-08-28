package gryphon.samples.swing;

import java.util.Date;

import gryphon.domain.SimpleEntity;

public class Person extends SimpleEntity
{

	private static final long serialVersionUID = 1L;
	
	private String firstName;
	
	private String lastName;
	
	private String phone;
	
	private String email;
	
	private Date birthDate;
	
	private Integer age;
	
	private Boolean flagged;

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate(Date birthDate)
	{
		this.birthDate = birthDate;
	}

	public Boolean isFlagged()
	{
		return flagged;
	}

	public Boolean getFlagged()
	{
		return flagged;
	}

	public void setFlagged(Boolean flagged)
	{
		this.flagged = flagged;
	}

	public Integer getAge()
	{
		return age;
	}

	public void setAge(Integer age)
	{
		this.age = age;
	}

}
