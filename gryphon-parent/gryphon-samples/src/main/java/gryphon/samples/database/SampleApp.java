package gryphon.samples.database;

import gryphon.common.Logger;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class SampleApp
{

	/**
	 * Some examples of how to use DB access layer.
	 * Don't forget to include your JDBC-driver in the classpath.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		SampleDatabaseBroker db = new SampleDatabaseBroker();
		db.setKeepAlive(true);
		// delete old records
		// leave WHERE clause empty to delete all records
		db.deleteByQuery(Person.class, "");
		// insert a person
		insertPerson(db);
		//select all persons
		List<Person> persons = db.select(Person.class, new Properties());
		debug(persons);
		// select persons by email
		Properties params = new Properties();
		params.put("@Email", new String[]{"ivan.d%","i.d%"});
		persons = db.select(Person.class, params);
		debug(persons);
		// select one person by id
		int someId = (Integer) persons.get(0).getId();
		Person p = db.select1(Person.class, someId);
		debug(p);
		// update person in the database
		p.setEmail("i.draga@gmail.com");
		db.update(p);
		// select the same person and check our changes
		p = db.select1(Person.class, someId);
		debug(p);
	}

	private static void insertPerson(SampleDatabaseBroker db) throws Exception
	{
		int newId = db.nextId();
		Person p = new Person();
		p.setEmail("ivan.draga@gmail.com");
		p.setId(newId);
		p.setName("Ivan Draga");
		p.setPhone("112");
		p.setLastUpdate(new Date());
		db.insert(p);
	}

	private static void debug(List<Person> list){
		for (Iterator<Person> iter = list.iterator(); iter.hasNext();)
		{
			Person p = iter.next();
			debug(p);
		}		
	}
	private static void debug(Person p)
	{
		Logger.debug(p.getId()+": "+p.getName()+"; "+p.getEmail()+"; "+p.getLastUpdate());
	}

}
