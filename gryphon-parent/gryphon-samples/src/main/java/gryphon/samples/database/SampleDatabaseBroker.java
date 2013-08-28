package gryphon.samples.database;

import gryphon.database.sql_impl.SqlDatabaseBroker;

public class SampleDatabaseBroker extends SqlDatabaseBroker
{
	public SampleDatabaseBroker()
	{
		String cn = this.getClass().getName();
		setPackageName(cn.substring(0,cn.lastIndexOf('.')));
		setDriverClassName("com.mysql.jdbc.Driver");
		setUrl("jdbc:mysql://localhost/xplanner?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8");
		setUser("xplanner");
		setPass("123456");
	}

}
