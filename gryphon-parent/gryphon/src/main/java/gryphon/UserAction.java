package gryphon;

import gryphon.common.GryphonEvent;
import gryphon.common.Logger;

import java.util.HashMap;

/**
 * Abstract user operation he/she can perform in a system.
 * 
 * @author Evgueni Tiourine
 */

public abstract class UserAction implements Cloneable
{
    /**
     * Такой ключ следует использовать для сохранения в свойствах
     * действий-потомков названия следующей формы.
     */
    public static final String NEXT_FORM = "userAction.nextForm";
    /**
     * Такой ключ следует использовать для сохранения в свойствах
     * действий-потомков кода следующей операции (экшена).
     */
    public static final String NEXT_ACTION = "userAction.nextAction";

    public static final String NAME = "userAction.name";

    public static final String SHORT_DESCRIPTION = "userAction.shortDescription";

    private Application application;

    // private EventObject event;
    private HashMap values = new HashMap();
    
    /** Флаг показывает, что выполнение действия должно сопровождаться мониторингом */
    private boolean progressable;

    public UserAction()
    {
        putValue(NAME, "nonamed action");
    }

    public UserAction(String name)
    {
        putValue(NAME, name);
    }

    public final void userActionPerformed(GryphonEvent event) throws Exception
    {

        preConditions(event);

        doAction(event);

        postCondition(event);
    }

    /**
     * Проверка пред-условий. Базовая реализация ничего не делает
     * 
     * @throws java.lang.Exception
     */
    public void preConditions(GryphonEvent event) throws Exception
    {

    }

    /**
     * Собственно, вся логика системного действия. Должен реализовываться
     * потомками.
     * 
     * @throws java.lang.Exception
     */
    public abstract void doAction(GryphonEvent event) throws Exception;

    /**
     * Проверка пост-условий. Базовая реализация ничего не делает.
     * 
     * @throws java.lang.Exception
     */
    public void postCondition(GryphonEvent event) throws Exception
    {

    }
    /**
     * Helper method incapsulates call chain. 
     * @param key
     * @return
     * @throws Exception 
     */
    public Object getProperty(String key) throws Exception
    {
        return getApplication().getProperty(key);
    }
    /**
     * Helper method incapsulates call chain. 
     * @param key
     * @param value
     * @throws Exception 
     */
    public void setProperty(String key, Object value) throws Exception
    {
        getApplication().setProperty(key, value);
    }

    public Application getApplication()
    {
        return application;
    }

    public void setNextForm(String form)
    {
        putValue(NEXT_FORM, form);
    }

    public void putValue(String key, Object value)
    {
        values.put(key, value);
    }

    public String getNextForm()
    {
        return (String) getValue(NEXT_FORM);
    }

    public Object getValue(String key)
    {
        return values.get(key);
    }

    public void setNextAction(Object actionKey)
    {
        putValue(NEXT_ACTION, actionKey);
    }
    public Object getNextAction()
    {
        return getValue(NEXT_ACTION);
    }
    /**
     * установка названия операции по умолчанию для кнопок и др. элементов GUI
     */
    public void setApplication(Application application)
    {
        this.application = application;
        if (getValue(NAME)==null || getValue(NAME).equals("nonamed action"))
        {
            try
            {
                String name = (String) getApplication().getProperty(
                        getClass().getName() + ".name");
                putValue(NAME,name);
            }
            catch (Exception e)
            {
                Logger.debug("Name of the action is not specified "+getClass().getName());
            }
        }
    }

	public boolean isProgressable()
	{
		return progressable;
	}

	public void setProgressable(boolean progressable)
	{
		this.progressable = progressable;
	}
    
}