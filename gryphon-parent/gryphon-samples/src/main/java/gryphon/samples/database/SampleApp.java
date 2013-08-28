package gryphon.samples.database;

import gryphon.common.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class SampleApp
{

	/**
	 * Some examples of how to use brokers.
	 * Don't forget to include your JDBC-driver in the classpath.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		SampleDatabaseBroker db = new SampleDatabaseBroker();
		//select all persons
		Properties params = new Properties();
		List<Person> persons = db.select(Person.class, params);
		for (Iterator<Person> iter = persons.iterator(); iter.hasNext();)
		{
			Person p = iter.next();
			debug(p);
		}
		// select persons whose email starts with "e.t%" or is empty string
		params.put("@email", new String[]{"e.t%",""});
		persons = db.select(Person.class, params);
		for (Iterator<Person> iter = persons.iterator(); iter.hasNext();)
		{
			Person p = iter.next();
			debug(p);
		}
		// select one person by id
		Person p = db.select1(Person.class, "2072");
		debug(p);

		// update person in the database
//		db.update(p);

		// create person in the database 
		p.setId("9999");
//		db.insert(p);
		
		// delete person from the database. id value must be set.
//		db.delete(p);
	}

	private static void debug(Person p)
	{
		Logger.debug(p.getId()+": "+p.getName()+"; "+p.getEmail());
	}

}
