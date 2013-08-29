package gryphon.samples.database;

import gryphon.database.sql_impl.SqlEntityBroker;

public class PersonBroker extends SqlEntityBroker
{
	{
		setQuote("'");
	}
	protected String getJavaType()
	{
		return Person.class.getName();
	}

}
