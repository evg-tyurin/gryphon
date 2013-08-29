package gryphon.samples.database;

import gryphon.database.sql_impl.SqlDatabaseBroker;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

public class SampleDatabaseBroker extends SqlDatabaseBroker
{
	public SampleDatabaseBroker()
	{
		String cn = this.getClass().getName();
		setPackageName(cn.substring(0,cn.lastIndexOf('.')));
		setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
		setUrl("jdbc:derby:derbyDB;create=true");
//		setUser("user");
//		setPass("pass");
	}
	/** 
	 * Create tables, insert value into Sequence.
	 * This is for example only. Usually we have long SQL scripts in other place and
	 * invoke them in special manner. 
	 */
	public void prepareDatabase() throws Exception{
		ResourceBundle b = ResourceBundle.getBundle("prepare-db");
		String[] names = new String[]{
				"createSequence", "fillSequence", "createPerson", 
		};
		Connection conn = getConnection();
		for (String name : names) {
			String sql = b.getString(name);
			Statement s = conn.createStatement();
			try {
				s.executeUpdate(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally{
				s.close();
			}
		}
		conn.close();
	}
}
