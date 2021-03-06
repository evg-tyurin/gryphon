package gryphon.database.sql_impl;

import gryphon.common.Logger;
import gryphon.database.AbstractDatabaseBroker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
				connection = null;
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
	/** ���� �������� */
	public Object getValue(String sql) throws Exception{
		Logger.debug("Query: "+sql);
		Object value = null;
		Connection conn = getConnection();
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()){
				value = rs.getObject(1);
			}
			rs.close();
			s.close();
		}
		finally{
			if (!isKeepAlive())
				close();
		}
		return value;
	}
	/** ��������� �������� �� ����� ������ */
	public Object[] getValues(String sql, Object[] values) throws Exception{
		Logger.debug("Query: "+sql);
		Connection conn = getConnection();
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			if (rs.next()){
				for (int i = 0; i < values.length; i++) {
					values[i] = rs.getObject(i+1);					
				}
			}
			rs.close();
			s.close();
		}
		finally{
			if (!isKeepAlive())
				close();
		}
		return values;
	}
	/** ��������� ������� */
	public List<Object[]> getValues(String sql) throws Exception{
		Logger.debug("Query: "+sql);
		Connection conn = getConnection();
		ArrayList<Object[]> values = new ArrayList<Object[]>(); 
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(sql);
			int columnCount = rs.getMetaData().getColumnCount();
			while (rs.next()){
				Object[] row = new Object[columnCount]; 
				for (int i = 0; i < columnCount; i++) {
					row[i] = rs.getObject(i+1);					
				}
				values.add(row);
			}
			rs.close();
			s.close();
		}
		finally{
			if (!isKeepAlive())
				close();
		}
		Logger.debug(values.size()+" record(s)");
		return values;
	}
	/** ��������� �������. � ������� ����� �������������� ���������. */
	public List<Object[]> getValuesWithParams(String sql, Object[] params) throws Exception{
		Logger.debug("Query: "+sql);
		Connection conn = getConnection();
		ArrayList<Object[]> values = new ArrayList<Object[]>(); 
		try {
			PreparedStatement s = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				s.setObject(i+1, params[i]);
			}
			ResultSet rs = s.executeQuery();
			int columnCount = rs.getMetaData().getColumnCount();
			while (rs.next()){
				Object[] row = new Object[columnCount]; 
				for (int i = 0; i < columnCount; i++) {
					row[i] = rs.getObject(i+1);					
				}
				values.add(row);
			}
			rs.close();
			s.close();
		}
		finally{
			if (!isKeepAlive())
				close();
		}
		Logger.debug(values.size()+" record(s)");
		return values;
	}


}