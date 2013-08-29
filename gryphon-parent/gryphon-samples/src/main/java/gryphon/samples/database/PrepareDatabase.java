package gryphon.samples.database;

import gryphon.common.Logger;

/**
 * Prepare your database before running SampleApp.
 * @author etyurin
 *
 */
public class PrepareDatabase
{

	/**
	 * Create table that is used in SampleApp.
	 * Don't forget to include your JDBC-driver in the classpath.
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		SampleDatabaseBroker db = new SampleDatabaseBroker();
		db.prepareDatabase();
		Logger.debug("ok");
	}

}
