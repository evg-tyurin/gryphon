package gryphon.database.sql_impl;

import gryphon.common.Logger;
import gryphon.database.AbstractDatabaseBroker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * ���������� ����������� ������� ��� ��������, �������������� ������ ����� jdbc-���������� 
 * 
 * @author ET
 */
public class SqlDatabaseBroker extends AbstractDatabaseBroker
{
    private Connection connection;

    /**
     * JDBC-URL � ���� ������
     */
    private String url;

    /**
     * ������������ ���� ������
     */
    private String user;

    /**
     * ������ ������������ ���� ������
     */
    private String pass;

    /**
     * �������� ������ ��������.
     */
    private String driverClassName;

    /**
     * �������� ������ ��� ������������� ������ �������� � URL. 
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

}