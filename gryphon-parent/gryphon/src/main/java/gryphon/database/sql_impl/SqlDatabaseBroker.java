package gryphon.database.sql_impl;

import gryphon.common.Logger;
import gryphon.database.AbstractDatabaseBroker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * Реализация датабазного брокера для хранилищ, поддерживающих работу через jdbc-соединение 
 * 
 * @author ET
 */
public class SqlDatabaseBroker extends AbstractDatabaseBroker
{
    private Connection connection;

    /**
     * JDBC-URL к базе данных
     */
    private String url;

    /**
     * Пользователь базы данных
     */
    private String user;

    /**
     * Пароль пользователя базы данных
     */
    private String pass;

    /**
     * Название класса драйвера.
     */
    private String driverClassName;

    /**
     * Источник данных для использования вместо драйвера и URL. 
     */
    private DataSource dataSource;

    public SqlDatabaseBroker()
    {
    	this(false);
    }

    public SqlDatabaseBroker(boolean keepAlive)
    {
    	setKeepAlive(keepAlive);
    }

    protected Connection getConnection() throws Exception
    {
        if (connection == null)
        {
        	if (getDataSource()!=null){
        		connection = getDataSource().getConnection();
        	}
        	else{
	            Class.forName(getDriverClassName());
	            if (getUser() != null && getPass() != null)
	            {
	                connection = DriverManager.getConnection(getUrl(), getUser(),
	                        getPass());
	            }
	            else
	            {
	                connection = DriverManager.getConnection(getUrl());
	            }
        	}
        }
        return connection;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    protected String getPass()
    {
        return pass;
    }

    public void setPass(String pass)
    {
        this.pass = pass;
    }

    public String getDriverClassName()
    {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName)
    {
        this.driverClassName = driverClassName;
    }

	@Override
	public void close()
	{
		try {
			if (connection!=null && !connection.isClosed()){
				connection.close();
			}
		} catch (SQLException e) {
			Logger.logThrowable(e);
		}
	}

	public DataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}
	/** 
	 * Get next id from the sequence. 
	 * This is global id for all object types of the application. 
	 */
	public int nextId() throws Exception
	{
		Sequence seq = null;
		for(;;){
			// select the only record
			List<Sequence> list = select(Sequence.class, new Properties());
			// increment
			if (list.size()>0){
				seq = list.get(0);
				seq.setId(seq.getIntId()+1);
			}
			else{
				seq = new Sequence();
				seq.setId(1);
			}
			// update and check that sequence was not changed by other user
			int r = update(seq);
			if (r>0)
				break;
		}
		return seq.getIntId();
	}

}