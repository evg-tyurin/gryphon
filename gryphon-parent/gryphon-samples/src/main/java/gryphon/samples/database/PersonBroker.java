package gryphon.samples.database;

import gryphon.Entity;
import gryphon.database.sql_impl.SqlEntityBroker;

public class PersonBroker extends SqlEntityBroker
{
	public PersonBroker()
	{
		setIdColumn("id");
	}

	protected String getJavaType()
	{
		return Person.class.getName();
	}

	public Entity select1(String id) throws Exception
	{
		return super.select1((Object)id);
	}

	public int delete(String id) throws Exception
	{
		return super.delete((Object)id);
	}

}
