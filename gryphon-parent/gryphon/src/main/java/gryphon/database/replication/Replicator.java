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
   * Посредник при работе с БД, из которой происходит репликация.
   */
  private AbstractDatabaseBroker source;
  /**
   * Посредник при работе с целевой БД.
   */
  private AbstractDatabaseBroker target;
  /**
   * Флаг означает игнорирование отдельных исключений
   * при выполнении операций над списковыми объектами.
   */
  private boolean ignoreExceptions = false;

  public Replicator() {
  }
  /**
   * Реплицирует указанные сущности с учетом указанных ограничений.
   * Ограничения могут быть такими:
   *   + с момента прошлой репликации
   *   + все записи с перезаписью существующих
   * В настоящее время поддерживается только первое ограничение.
   *
   * Реализация первого типа репликации основана на последовательной (монотонно-возрастающей)
   * нумерации реплицируемых сущностей. Сначала из целевого хранилища берется
   * запись с максимальным ид (число), а затем из хранилища-источника берутся
   * все записи с ид большим, чем этот ид.
   *
   * @param entityClassName имя класса сущности
   * @param constraints ограничения
   * @return список того, что было реплицировано
   */
  public List replicate(String entityClassName, int constraints) throws Exception {
    Logger.log("replicate...");
    if (constraints == SINCE_LAST_REPLICATION) {
      int maxId = 0;
      // поищем максимальный ид в целевом хранилище
      Properties replicationConstraints = new Properties();
      replicationConstraints.put("forReplication", "true");
      replicationConstraints.put("entityClassName", entityClassName);
      List list1 = target.select(ReplicationInfo.class.getName(), replicationConstraints);
      for (int i=0; i<list1.size(); i++) {// что-то нашли
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
      // берем список более новых сущностей
      int newMaxId = 0;
      List list = source.select(entityClassName, replicationConstraints);
      // реплицируем
      int replicated = 0;
      for (int i=0; i<list.size(); i++) {
        Logger.log("i="+i);
        try {
          // сохраняем каждую новую сущность
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
      // протоколируем результаты сеанса репликации
      ReplicationInfo info = new ReplicationInfo();
      info.setEntityClassName(entityClassName);
      info.setMaxId(newMaxId);
      target.insert(info);
      // сообщаем юзеру и выходим
      Logger.log(list.size()+" objects replicated.");
      // важное сообщение
      if (list.size()>0 && replicated == 0) {
        // при таких условиях в следующий раз будет то же самое
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
   * Важное уведомление, которе надо бы отправлять по мылу, но пока просто пишется в отдельный файл.
   * @param string
   */
  protected void makeNotification(String string) throws Exception {
    PrintStream out = new PrintStream(new FileOutputStream("warnings.txt", true));
    out.println(new Date().toString());
    out.println(string);
    out.close();
  }

}