package gryphon.database.replication;

import gryphon.Application;
import gryphon.Entity;
import gryphon.common.Logger;
import gryphon.database.AbstractDatabaseBroker;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author ET
 */

public class Replicator {
  public static final int SINCE_LAST_REPLICATION = 1;
  private Application application;
  /**
   * ��������� ��� ������ � ��, �� ������� ���������� ����������.
   */
  private AbstractDatabaseBroker source;
  /**
   * ��������� ��� ������ � ������� ��.
   */
  private AbstractDatabaseBroker target;
  /**
   * ���� �������� ������������� ��������� ����������
   * ��� ���������� �������� ��� ���������� ���������.
   */
  private boolean ignoreExceptions = false;

  public Replicator() {
  }
  /**
   * ����������� ��������� �������� � ������ ��������� �����������.
   * ����������� ����� ���� ������:
   *   + � ������� ������� ����������
   *   + ��� ������ � ����������� ������������
   * � ��������� ����� �������������� ������ ������ �����������.
   *
   * ���������� ������� ���� ���������� �������� �� ���������������� (���������-������������)
   * ��������� ������������� ���������. ������� �� �������� ��������� �������
   * ������ � ������������ �� (�����), � ����� �� ���������-��������� �������
   * ��� ������ � �� �������, ��� ���� ��.
   *
   * @param entityClassName ��� ������ ��������
   * @param constraints �����������
   * @return ������ ����, ��� ���� �������������
   */
  public List replicate(String entityClassName, int constraints) throws Exception {
    Logger.log("replicate...");
    if (constraints == SINCE_LAST_REPLICATION) {
      int maxId = 0;
      // ������ ������������ �� � ������� ���������
      Properties replicationConstraints = new Properties();
      replicationConstraints.put("forReplication", "true");
      replicationConstraints.put("entityClassName", entityClassName);
      List list1 = target.select(ReplicationInfo.class.getName(), replicationConstraints);
      for (int i=0; i<list1.size(); i++) {// ���-�� �����
        ReplicationInfo replicationInfo = (ReplicationInfo)list1.get(i);
        if (replicationInfo.getEntityClassName().equals(entityClassName)) {
          if (maxId < replicationInfo.getMaxId()) {
            maxId = replicationInfo.getMaxId();
          }
        }
      }
      Logger.log("current max id of "+entityClassName+" = "+maxId);
      replicationConstraints.put("maxId", String.valueOf(maxId));
      Object limit = getApplication().getProperty("replication.limit");
      if (limit == null) {
        limit = "100";
      }
      replicationConstraints.setProperty("limit",(String)limit);
      // ����� ������ ����� ����� ���������
      int newMaxId = 0;
      List list = source.select(entityClassName, replicationConstraints);
      // �����������
      int replicated = 0;
      for (int i=0; i<list.size(); i++) {
        Logger.log("i="+i);
        try {
          // ��������� ������ ����� ��������
          Entity e = (Entity) list.get(i);
          target.insertOrUpdate(e);
          int entityId = Integer.parseInt(e.getAttribute("Id").toString());
          if (newMaxId < entityId) {
            newMaxId = entityId;
          }
          replicated++;
        }
        catch (Exception ex) {
          if (getIgnoreExceptions()) {
            Logger.logThrowable(ex);
          }
          else {
            throw ex;
          }
        }
      }
      // ������������� ���������� ������ ����������
      ReplicationInfo info = new ReplicationInfo();
      info.setEntityClassName(entityClassName);
      info.setMaxId(newMaxId);
      target.insert(info);
      // �������� ����� � �������
      Logger.log(list.size()+" objects replicated.");
      // ������ ���������
      if (list.size()>0 && replicated == 0) {
        // ��� ����� �������� � ��������� ��� ����� �� �� �����
        makeNotification("There were replicated 0 of "+list.size()+" entities. ");
      }
      return list;
    }
    throw new Exception ("Unknown constraint: "+constraints);
  }
  public List replicate(Class entityClass, int constraints) throws Exception {
    return replicate(entityClass.getName(),constraints);
  }

  public void setSource(AbstractDatabaseBroker source) {
    this.source = source;
  }

  public boolean getIgnoreExceptions() {
    return ignoreExceptions;
  }

  public void setTarget(AbstractDatabaseBroker target) {
    this.target = target;
  }
  public void setIgnoreExceptions(boolean ignoreExceptions) {
    this.ignoreExceptions = ignoreExceptions;
  }
  public Application getApplication()
  {
    return application;
  }
  public void setApplication(Application application)
  {
    this.application = application;
  }
  /**
   * ������ �����������, ������ ���� �� ���������� �� ����, �� ���� ������ ������� � ��������� ����.
   * @param string
   */
  protected void makeNotification(String string) throws Exception {
    PrintStream out = new PrintStream(new FileOutputStream("warnings.txt", true));
    out.println(new Date().toString());
    out.println(string);
    out.close();
  }

}