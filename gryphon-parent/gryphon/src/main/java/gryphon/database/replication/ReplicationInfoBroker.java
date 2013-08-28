package gryphon.database.replication;

import gryphon.Entity;
import gryphon.database.sql_impl.SqlEntityBroker;

import java.sql.ResultSet;
import java.util.Properties;



/**
 * @author ET
 */

public class ReplicationInfoBroker extends SqlEntityBroker {
  public ReplicationInfoBroker() {
  }
  protected String getUpdateSql(Entity entity) throws Exception {
    /**@todo Implement this gryphon.permanent.SqlEntityBroker abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getUpdateSql() not yet implemented.");
  }
  protected String getSelectSql(Properties constraints) throws Exception {
    String select = "SELECT * FROM REPLICATION_INFO ";
    String where = "";
    if (!constraints.getProperty("entityClassName", "").equals("")) {
      where = "Entity='"+constraints.getProperty("entityClassName", "")+"' ";
    }
    if (!where.equals("")) {
      select += "WHERE " + where;
    }
    return select;
  }
  protected String getSelect1Sql(Object id) throws Exception {
    return "SELECT * FROM REPLICATION_INFO WHERE InfoId="+id.toString();
  }
  protected String getInsertSql(Entity entity) throws Exception {
    ReplicationInfo info = (ReplicationInfo)entity;
    String insert = "INSERT INTO REPLICATION_INFO (Entity, MaxId) "+
        "VALUES ('"+info.getEntityClassName()+"', "+info.getMaxId()+")";
    return insert;
  }
  protected Entity createEntity(ResultSet rs) throws Exception {
    ReplicationInfo info = new ReplicationInfo();
    info.setEntityClassName(rs.getString("Entity"));
    info.setMaxId(rs.getInt("MaxId"));
    return info;
  }
	protected String getJavaType()
	{
		return ReplicationInfo.class.getName();
	}

}