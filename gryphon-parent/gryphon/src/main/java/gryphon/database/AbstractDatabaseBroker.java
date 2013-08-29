package gryphon.database;

import gryphon.Application;
import gryphon.Entity;
import gryphon.common.Logger;
import gryphon.database.sql_impl.Sequence;
import gryphon.database.sql_impl.SequenceBroker;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

/**
 * Базовый класс для датабазных брокеров.
 * 
 * @author ET
 */
public abstract class AbstractDatabaseBroker implements DatabaseBroker
{
    private Application application;
    
    private String packageName;

    /** 
     * Если false, то брокер будет освобождать ресурсы (закрывать соединение) 
     * после каждого запроса. Если true, тогда закрытие брокера через close() 
     * должно осуществляться на прикладном уровне.
     */
	private boolean keepAlive = false;

    public AbstractDatabaseBroker()
    {
    }

    public final Integer insert(Entity entity) throws Exception
    {
        Command cmd = new Command("insert",entity.getClass().getName(),entity);
        return (Integer)executeAndGetResult(cmd);
    }

    public Integer delete(Entity entity) throws Exception
	{
        Command cmd = new Command("delete",entity.getClass().getName(),entity.getId());
        return (Integer)executeAndGetResult(cmd);
	}

    public Object executeAndGetResult(Command cmd) throws Exception
    {
        Command result = execute(cmd);      
        if (result.getOperation().equals(Command.STATUS_FAILED)) {
            throw (Exception)result.getAttachment();
        }
        return result.getAttachment();
    }

    public final Integer insertOrUpdate(Entity entity) throws Exception
    {
        if (entity.getId()==null || select1(entity.getClass(),entity.getId())==null)
        {
            return insert(entity);
        }
        return update(entity);
    }

    public Integer update(Entity entity) throws Exception
    {
        Command cmd = new Command("update",entity.getClass().getName(),entity);
        return (Integer) executeAndGetResult(cmd);
    }

    public List select(String entityClassName, Properties constraints)
            throws Exception
    {
        Command cmd = new Command("select",entityClassName,constraints);
        return (List) executeAndGetResult(cmd);
    }

    public <T extends Entity> List<T> select(Class<T> entityClass, Properties constraints)
            throws Exception
    {
        Command cmd = new Command("select",entityClass.getName(),constraints);
        return (List<T>) executeAndGetResult(cmd);
    }

    public Entity select1(String entityClassName, Object id) throws Exception
    {
        Command cmd = new Command("select1",entityClassName,id);
        return (Entity) executeAndGetResult(cmd);
    }

    public <T extends Entity> T select1(Class<T> entityClass, Object id) throws Exception
    {
        Command cmd = new Command("select1",entityClass.getName(),id);
        return (T) executeAndGetResult(cmd);
    }

	public <T extends Entity> Integer insert(List<T> entityList) throws Exception {
		if (entityList.size()==0)
			return 0;
		Entity entity = entityList.get(0);
        Command cmd = new Command("insert",entity.getClass().getName(),entityList);
        return (Integer)executeAndGetResult(cmd);
	}

	public <T extends Entity> List<T> selectByQuery(Class<T> clazz, String query) throws Exception{
		Logger.debug("Query: "+query);
        Command cmd = new Command("selectByQuery",clazz.getName(),query);
        return (List<T>)executeAndGetResult(cmd);		
	}
	
	public <T extends Entity> Integer getObjectCount(Class<T> entityClass, String sql) throws Exception{
        Command cmd = new Command("getObjectCount",entityClass.getName(),sql);
        return (Integer) executeAndGetResult(cmd);
	}
	
	public <T extends Entity> Integer delete(List<T> entityList) throws Exception {
		if (entityList.size()==0)
			return 0;
		Entity entity = entityList.get(0);
        Command cmd = new Command("delete",entity.getClass().getName(),entityList);
        return (Integer)executeAndGetResult(cmd);
	}
	
	public <T extends Entity> Integer deleteByQuery(Class<T> entityClass, String query) throws Exception {
        Command cmd = new Command("deleteByQuery",entityClass.getName(),query);
        return (Integer)executeAndGetResult(cmd);
	}
	
	public <T extends Entity> Integer update(List<T> entityList) throws Exception {
		if (entityList.size()==0)
			return 0;
		Entity entity = entityList.get(0);
        Command cmd = new Command("update",entity.getClass().getName(),entityList);
        return (Integer)executeAndGetResult(cmd);
	}
    /**
     * Создает экземпляр объектного брокера.
     */
    protected EntityBroker instantiateObjectBroker(String entityClassName)
            throws Exception
    {
        // broker class name
        String className = getBrokerClassName(entityClassName);
        EntityBroker broker = (EntityBroker) Class.forName(className).newInstance();
        broker.setDatabaseBroker(this);
        return broker;
    }

    protected String getBrokerClassName(String entityClassName)
    {
    	if (entityClassName.equals(Sequence.class.getName()))
    		return SequenceBroker.class.getName();
        int start = entityClassName.lastIndexOf(".");
        return getPackageName()
                + entityClassName.substring(start, entityClassName.length())
                + "Broker";
    }

    protected void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    protected String getPackageName()
    {
        return packageName;
    }
    /** 
     * Выполняет команду и возвращает результат выполнения
     */
    public Command execute(Command command)
    {
        Command result = new Command();
        try
        {
            EntityBroker broker = instantiateObjectBroker(command.getObjectName());
            Class[] parameterTypes = new Class[1];
            if (command.getAttachment() instanceof Entity)
            {
                parameterTypes[0] = Entity.class;
            }
            else if (command.getAttachment() instanceof List)
            {
                parameterTypes[0] = List.class;
            }
            else
            {
                parameterTypes[0] = command.getAttachment().getClass();
            }
            Logger.log(broker.getClass().getName() + "." + command.getOperation());
            Method method = broker.getClass().getMethod(command.getOperation(),
                    parameterTypes);
            Object o = method.invoke(broker,
                    new Object[] { command.getAttachment() });
            result.setObjectName(command.getObjectName());
            result.setOperation(Command.STATUS_OK);
            result.setAttachment(o);
        }
        catch (Throwable e)
        {
            Logger.logThrowable(e);
            result.setOperation(Command.STATUS_FAILED);
            result.setObjectName(command.getObjectName());
            result.setAttachment(e);            
        }
        finally{
        	if (!keepAlive)
        		close();
        }
        return result;
    }
    /** Освобождает занятые ресурсы - закрывает соединение и т.п. */
    public abstract void close();

	public Application getApplication()
    {
        return application;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

    public EntityBroker getObjectBroker(Entity entity) throws Exception{
		return instantiateObjectBroker(entity.getClass().getName());
	}

	public boolean isKeepAlive()
	{
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive)
	{
		this.keepAlive = keepAlive;
	}
    
}