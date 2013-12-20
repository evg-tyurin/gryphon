package gryphon.database.sql_impl;

import gryphon.Entity;
import gryphon.common.Logger;
import gryphon.database.DatabaseBroker;
import gryphon.database.EntityBroker;
import gryphon.database.MapCache;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Реализация объектного брокера для хранилищ, поддерживающих работу
 * через jdbc-соединение и язык запросов SQL.
 * 
 * @author ET
 */
public abstract class SqlEntityBroker implements EntityBroker
{
	/**
	 * Ключ, под которым в маппинге хранится имя таблицы. 
	 */
    protected static final String __TABLE = "__table";
	/**
	 * Ключ, под которым в маппинге хранится упорядоченный список хранимых атрибутов. 
	 */
	protected static final String __ATTRS = "__attrs";
	/**
	 * Ключ, под которым в маппинге хранится имя идентификационного столбца. 
	 */
    protected static final String __ID = "__id";
	/**
	 * Ключ, под которым в маппинге хранится выражение идентификационного столбца. 
	 */
    protected static final String __ID_EXPR = "__id_expr";
	/**
     * Брокер-диспетчер всего хранилища
     */
    private DatabaseBroker databaseBroker;
    
    private Connection conn;
    /**
     * Маппинг таблицы в БД и Java-класса по аналогии с Hibernate.
     */
	private HashMap mapping;
    /**
     * Таблица, в которой хранятся объекты данного типа.
     */
	private String table;
	/**
	 * Столбец таблицы, который является уникальным ид - PRIMARY KEY
	 */
	private String idColumn;
	/**
	 * Использовать ps.setNull для атрибутов с пустыми значениями
	 */
	protected boolean useSetNull = false;
	/**
	 * Какую кавычку использовать в SQL запросах со строковыми значениями.
	 */
	private String quote = "\"";
	
    public SqlEntityBroker()
    {
		if (mapping==null)
		{
			String key = this.getClass().getName()+".mapping";
			mapping = MapCache.getInstance().getMapping(key);
			if (mapping==null)
			{
				mapping = readMapping();
				MapCache.getInstance().putMapping(key, mapping);
			}
		}
		if (mapping.get(__TABLE)!=null)
		{
			setTable((String) mapping.get(__TABLE));
		}
		else
		{// default table name is name of the entity
			String cn = this.getClass().getName();
			cn = cn.substring(cn.lastIndexOf('.')+1);
			Logger.debug("setTable="+cn.substring(0,cn.length()-"Broker".length()));
			setTable(cn.substring(0,cn.length()-"Broker".length()));
		}
		if (mapping.get(__ID)!=null)
		{
			setIdColumn((String) mapping.get(__ID));
		}
		else
		{// default id column is table name + 'Id'
			setIdColumn(getTable()+"Id");
		}
    }

    /**
     * Создает объект в БД.
     * 
     * @param entity объект
     * @return number of inserted objects 
     * @throws java.lang.Exception
     */
    public int insert(Entity entity) throws Exception
    {
        String sql = getInsertSql(entity);
        Logger.log(getClass().getName() + ".insert(): " + sql);
        PreparedStatement ps = getConnection().prepareStatement(sql);
        setInsertParameters(ps, entity);
        int rows;
		try {
			rows = ps.executeUpdate();
		} finally{
	        ps.close();
		}
        Logger.log("insert.rows="+rows);
        return rows;
    }

	public <T extends Entity> int insert(List<T> list) throws Exception {
		if (list.size()==0)
			return 0;
        Logger.log("try to insert "+list.size()+" row(s)");
		Connection conn = getConnection();
		boolean oldFlag = conn.getAutoCommit();
		conn.setAutoCommit(false);
		int rows = 0;
        PreparedStatement ps = null;
		try {
			for (Iterator<T> iterator = list.iterator(); iterator.hasNext();) {
				Entity entity = iterator.next();
			    if (ps==null){
			        String sql = getInsertSql(entity);
			        Logger.log(getClass().getName() + ".insert(): " + sql);
			    	ps = conn.prepareStatement(sql);
			    }
			    setInsertParameters(ps, entity);
//	        rows += ps.executeUpdate();
			    ps.addBatch();
			    ps.clearParameters();
			}
			int[] batch = ps.executeBatch();
			for (int i = 0; i < batch.length; i++) {
				if (batch[i]>=0)
					rows++;
			}
			conn.commit();
		} catch (Exception e) {
			Logger.logThrowable(e);
			conn.rollback();
			throw e;
		}
		finally{
			conn.setAutoCommit(oldFlag);
			if (ps!=null)
				ps.close();
		}
        ps.close();
        Logger.log("insert.rows="+rows);
        return rows;
	}

	protected void setInsertParameters(PreparedStatement ps, Entity entity) throws Exception
	{
		int shift = 1;
    	// id column
		if (!getMapping().containsKey(__ID_EXPR)){
			ps.setObject(1, entity.getId());
			shift = 2;
		}
    	// other columns
		List attributes = (List) getMapping().get(__ATTRS);
		if (attributes==null)
			return;
		for (int i = 0; i < attributes.size(); i++)
		{
			String attrName = (String) attributes.get(i);
			Object value = entity.getAttribute(attrName);
			if (value==null && useSetNull)
				ps.setNull(i+shift, getSqlType(entity, attrName));
			else
			{
				if (value instanceof Timestamp)
					ps.setObject(i+shift, value, Types.TIMESTAMP);
				else if (value instanceof Date)
					ps.setObject(i+shift, new java.sql.Date(((Date)value).getTime()));
				else
					ps.setObject(i+shift, value);
			}
		}    	
	}
    /**
     * Удаляет объект из БД
     * @param id
     * @return количество удаленных записей
     * @throws Exception
     */
    public int delete(Object id) throws Exception
    {
    	String sql = getDeleteSql(id);
        Logger.log(getClass().getName() + ".delete(): " + sql);
    	PreparedStatement ps = getConnection().prepareStatement(sql);
    	ps.setObject(1, id);
    	int rows;
		try {
			rows = ps.executeUpdate();
		} finally{
	    	ps.close();
		}
        Logger.log("delete.rows=" + rows);
    	return rows;
    }

	protected String getDeleteSql(Object id) throws Exception
	{
		return "DELETE FROM "+getTable()+" WHERE "+getIdColumnExpr()+" = ? ";
	}
	/**
     * Выбирает объекты в соответствии с наложенными ограничениями
     * 
     * @param entityClassName
     * @param constraints
     *            ограничения
     * @return список экземпляров Entity
     * @throws java.lang.Exception
     */
    public List select(Properties constraints) throws Exception
    {
        String sql = getSelectSql(constraints);
        Logger.log(getClass().getName() + ".select(): " + sql);
        Statement s = getConnection().createStatement();
        List entityList;
		try {
			ResultSet rs = s.executeQuery(sql);
			entityList = new ArrayList();
			while (rs.next())
			{
			    entityList.add(createEntity(rs));
			}
		} finally{
	        s.close();
		}
        return entityList;
    }
    /**
     * Выбирает один объект из БД по его ид.
     */
    public Entity select1(Object id) throws Exception
    {
        String sql = getSelect1Sql(id);
        Logger.log(getClass().getName() + ".select1(): " + sql);
        PreparedStatement ps = getConnection().prepareStatement(sql);
        Entity e;
		try {
			ps.setObject(1, id);
			ResultSet rs = ps.executeQuery();
			e = null;
			if (rs.next())
			{
			    e = createEntity(rs);
			}
		} finally{
	        ps.close();
		}
        return e;
    }
    /**
     * @return имя java-класса, с которым работает брокер
     */
	protected abstract String getJavaType();

	public void setDatabaseBroker(DatabaseBroker broker)
    {
        this.databaseBroker = broker;
    }

    protected Connection getConnection() throws Exception
    {
        if (conn==null || conn.isClosed())
        {
            conn = ((SqlDatabaseBroker) getDatabaseBroker()).getConnection();
        }
        return conn;
    }

    public DatabaseBroker getDatabaseBroker()
    {
        return databaseBroker;
    }

	protected Entity createEntity(ResultSet rs) throws Exception
	{
		Class cls = Class.forName(getJavaType());
		Entity e = (Entity) cls.newInstance();
		e.setId(rs.getObject(getIdColumn()));
		for (Iterator iter = getMapping().keySet().iterator(); iter.hasNext();)
		{
			String attrName = (String) iter.next();
			if (attrName.equals(__ID) || attrName.equals(__ID_EXPR) || attrName.equals(__ATTRS) || attrName.equals(__TABLE))
				continue;
			String columnName = (String) getMapping().get(attrName);
			e.setAttribute(attrName, rs.getObject(columnName));
		}
		return e;
	}
	/**
	 * Обновляет объект в БД.
	 */
    public int update(Entity entity) throws java.lang.Exception
    {
        String sql = getUpdateSql(entity);
        Logger.log(getClass().getName() + ".update(): " + sql);
        PreparedStatement ps = getConnection().prepareStatement(sql);
        int rows;
		try {
			setUpdateParameters(ps,entity);
			rows = ps.executeUpdate();
		} finally {
	        ps.close();
		}
        Logger.debug("update.rows="+rows);
        return rows;
    }
	/**
	 * Обновляет список объектов в БД.
	 */
	public int update(List list) throws Exception {
		if (list.size()==0)
			return 0;
        Logger.log("try to update "+list.size()+" row(s)");
		Connection conn = getConnection();
		boolean oldFlag = conn.getAutoCommit();
		conn.setAutoCommit(false);
		int rows = 0;
        PreparedStatement ps = null;
		try {
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Entity entity = (Entity) iterator.next();
			    if (ps==null){
			        String sql = getUpdateSql(entity);
			        Logger.log(getClass().getName() + ".insert(): " + sql);
			    	ps = conn.prepareStatement(sql);
			    }
			    setUpdateParameters(ps, entity);
			    ps.addBatch();
			    ps.clearParameters();
			}
			int[] batch = ps.executeBatch();
			for (int i = 0; i < batch.length; i++) {
				if (batch[i]>=0)
					rows++;
			}
			conn.commit();
		} catch (Exception e) {
			Logger.logThrowable(e);
			conn.rollback();
			throw e;
		}
		finally{
			conn.setAutoCommit(oldFlag);
			if (ps!=null)
				ps.close();
		}
        ps.close();
        Logger.log("update.rows="+rows);
        return rows;
	}

	protected String getUpdateSql(Entity entity) throws Exception
	{
		return "UPDATE "+getTable()+" SET "+getNameValuePairs(entity)+" WHERE "+getIdColumnExpr()+" = ? ";
	}

	protected void setUpdateParameters(PreparedStatement ps, Entity entity) throws Exception
	{
		List attributes = (List) mapping.get(__ATTRS);
		if (attributes==null)
			return;
		for (int i = 0; i < attributes.size(); i++)
		{
			String attrName = (String) attributes.get(i);
			Object value = entity.getAttribute(attrName);
			if (value==null && useSetNull)
				ps.setNull(i+1, getSqlType(entity, attrName));
			else
			{
				if (value instanceof Timestamp)
					ps.setObject(i+1, value, Types.TIMESTAMP);
				else if (value instanceof Date)
					ps.setObject(i+1, new java.sql.Date(((Date)value).getTime()));
				else
					ps.setObject(i+1, value);
			}
		}
		ps.setObject(attributes.size()+1, entity.getId());
	}

	protected int getSqlType(Entity entity, String attrName)
	{
		return Types.NULL;
	}

	protected String getInsertSql(Entity entity) throws Exception
	{
		return "INSERT INTO "+getTable()+" ("+getColumnNames()+") VALUES ("+getColumnValues()+")";
	}

	protected String getColumnValues()
	{
		// id column
		String str = "";
		if (!getMapping().containsKey(__ID_EXPR)){
			str = "?";
		}
		// other columns
		List attrs = (List) getMapping().get(__ATTRS);
		if (attrs==null)
			return str;
		for (int i=0; i<attrs.size(); i++)
		{
			if (str.length()>0)
				str += ", ";
			str += "? ";
		}
		return str;
	}

	protected String getColumnNames()
	{
		// id column
		String str = "";
		if (!getMapping().containsKey(__ID_EXPR)){
			str = getIdColumn();
		}
		// other columns
		List attrs = (List) getMapping().get(__ATTRS);
		if (attrs==null)
			return str;
		for (Iterator iter = attrs.iterator(); iter.hasNext();)
		{
			String attrName = (String) iter.next();
			if (str.length()>0)
				str += ", ";
			str += (String)getMapping().get(attrName);
		}
		return str;
	}

	protected String getIdColumn()
	{
		return idColumn;
	}

	protected String getIdColumnExpr(){
    	if (getMapping().containsKey(__ID_EXPR))
    		return (String) getMapping().get(__ID_EXPR);
    	return getIdColumn();
	}

	protected String getTable()
	{
		return table;
	}
	
	protected void setTable(String table)
	{
		this.table = table;
	}
	protected void setIdColumn(String idColumn)
	{
		this.idColumn = idColumn;
	}
	protected String getSelect1Sql(Object id) throws Exception
	{
		return "SELECT * FROM "+getTable()+" WHERE "+getIdColumn()+" = ? ";
	}

	protected String getSelectSql(Properties constraints) throws Exception
	{
		String sc = getSearchCriteria(constraints);
		if (sc.trim().length()>0)
			sc = " WHERE "+sc;
		sc = sc.replaceAll(" @", " ");
		String idExpr = "";
		if (getMapping().containsKey(__ID_EXPR)){
			idExpr = getMapping().get(__ID_EXPR)+" as "+getIdColumn()+", ";
		}
		String sql = "SELECT "+idExpr+"* FROM "+getTable()+sc;
		String orderBy = (String) constraints.get("ORDER BY");
		if (orderBy !=null){
			sql += " ORDER BY "+orderBy;
		}
		return sql;
	}
    /**
     * Собираем из критериев запрос:
     * 1. принимаем во внимание ключи начинающиеся с @
     * 2. зачение параметра может быть строкой или строковым массивом (для условия по ИЛИ)
     */
    protected String getSearchCriteria(Properties params) {
        StringBuffer sb = new StringBuffer();
        Set keySet = params.keySet();
        Iterator iterator = keySet.iterator();
        int counter = 0;
        while (iterator.hasNext()) {
            String key = (String)iterator.next();
            if (!key.startsWith("@")) {
                continue;
            }
            Object value = params.get(key);
            if (!(value instanceof Number)
                    && !(value instanceof Number[]) 
                    && !(value instanceof String) 
                    && !(value instanceof String[]) ) 
            {
                throw new IllegalArgumentException("Bad param type in getSearchCriteria(Properties params) for key = "+key);
            }
            if (counter++>0) 
            {
                sb.append(" AND ");
            }
            if (value instanceof String) 
            {
                sb.append(key).append(" LIKE ").append(quote).append((String)value).append(quote).append(" ");
            } 
            else if (value instanceof Number) 
            {
                sb.append(key).append(" = ").append(value.toString()).append(" ");
            } 
            else 
            {
                sb.append(" ( ");
                StringBuffer innerOr = new StringBuffer();
                Object[] arr = (Object[])value;
                for (int i = 0; i < arr.length; i++) {
                    if (innerOr.length() > 0) {
                        innerOr.append(" OR "); 
                    }
                    if (arr[i] instanceof String)
                        innerOr.append(key).append(" LIKE ").append(quote).append(arr[i]).append(quote).append(" ");
                    else
                        innerOr.append(key).append(" = ").append(arr[i]).append(" ");
                            
                }
                sb.append(innerOr);
                sb.append(" ) ");
            }                    
        }
        return sb.toString();
    }
	protected String getNameValuePairs(Entity entity) throws Exception
	{
		String str = "";
		List attrs = (List) mapping.get(__ATTRS);
		if (attrs==null)
			return str;
		for (Iterator iter = attrs.iterator(); iter.hasNext();)
		{
			String attrName = (String) iter.next();
			if (str.length()>0)
				str += ", ";
			str += (String)mapping.get(attrName)+" = ? ";
		}
		return str;
	}

	protected HashMap getMapping()
	{
		return mapping;
	}
	protected HashMap readMapping()
	{
		try
		{
			String cn = getJavaType();
			cn = cn.substring(cn.lastIndexOf('.')+1);
			InputStream xmlMapping = this.getClass().getResourceAsStream(cn+".xml");
			if (xmlMapping==null)
			{
				Logger.warn("XML mapping for "+cn+" not found. Use default mapping.");
				return new HashMap();
			}
			Document xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlMapping);
			HashMap map = new HashMap();
			Element hbmMap = (Element) xml.getElementsByTagName("hibernate-mapping").item(0);
			Element hbmCls = (Element) hbmMap.getElementsByTagName("class").item(0);
			// table
			String _table = hbmCls.getAttribute("table");
			if (_table!=null && _table.length()>0)
				map.put(__TABLE, _table);
			// primary key
			Element hbmId = (Element) hbmCls.getElementsByTagName("id").item(0);
			{
			String name = hbmId.getAttribute("name");
			String column = hbmId.getAttribute("column");
			String expr = hbmId.getAttribute("expression");
			map.put(__ID, (column!=null&&column.length()>0)?column:name);
			if (expr!=null && expr.length()>0){
				map.put(__ID_EXPR, expr);
			}
			}
			// other columns
			ArrayList<String> attrs = new ArrayList<String>();
			NodeList hbmCols = hbmCls.getElementsByTagName("property");
			for (int i = 0; i < hbmCols.getLength(); i++)
			{
				Element col = (Element) hbmCols.item(i);
				String name = col.getAttribute("name");
				String column = col.getAttribute("column");
				String realColumn = (column!=null&&column.length()>0)?column:name;
				map.put(name, realColumn);
				attrs.add(name);
			}
			map.put(__ATTRS, attrs);
			return map;
		}
		catch (Exception e)
		{
			Logger.logThrowable(e);
			return new HashMap();
		}
	}
	
	public List selectByQuery(String sql) throws SQLException, Exception
	{
		Statement s = getConnection().createStatement();
        List entityList = new ArrayList();
		try {
			ResultSet rs = s.executeQuery(sql);
			while (rs.next())
			{
			    entityList.add(createEntity(rs));
			}
		} finally{
	        s.close();
		}
        return entityList;
	}
	
	public SqlEntityBroker getBroker(Entity entity) throws Exception{
		return (SqlEntityBroker) ((SqlDatabaseBroker)getDatabaseBroker()).getObjectBroker(entity);
	}

	public int getObjectCount(String sql) throws Exception
	{
		Statement s = getConnection().createStatement();
        int count = -1;
		try {
			ResultSet rs = s.executeQuery(sql);
			while (rs.next())
			{
				count = rs.getInt(1);
			}
		}
		finally{
			s.close();
		}
        return count;
	}
	public int deleteByQuery(String query) throws Exception{
    	String sql = "DELETE FROM "+getTable();
    	if (query!=null && query.trim().length()>0)
    		sql += " WHERE "+query;
        Logger.log(getClass().getName() + ".delete(): " + sql);
    	PreparedStatement ps = getConnection().prepareStatement(sql);
    	int rows;
		try {
			rows = ps.executeUpdate();
		} finally{
	    	ps.close();
		}
        Logger.log("delete.rows=" + rows);
    	return rows;
	}
	public <T extends Entity> int delete(List<T> object) throws Exception{
    	String sql = "DELETE FROM "+getTable()+" WHERE "+getIdColumn()+" IN ( ";
    	sql += getIds(object);
    	sql += " )";
        Logger.log(getClass().getName() + ".delete(): " + sql);
    	PreparedStatement ps = getConnection().prepareStatement(sql);
    	int rows;
		try {
			rows = ps.executeUpdate();
		} finally{
	    	ps.close();
		}
        Logger.log("delete.rows=" + rows);
    	return rows;
	}
	protected <T extends Entity> String getIds(List<T> list){
		String s = "";
		for (T t : list) {
			if (s.length()>0)
				s += " , ";
			if (t.getId() instanceof Number)
				s += t.getId();
			else
				s += "'"+t.getId()+"'";
		}
		return s;
	}
	public Entity select1(Integer id) throws Exception
	{
		Object oId = id;
		return select1(oId);
	}
	public Entity select1(String id) throws Exception
	{
		Object oId = id;
		return select1(oId);
	}

	public String getQuote()
	{
		return quote;
	}

	public void setQuote(String quote)
	{
		this.quote = quote;
	}

}