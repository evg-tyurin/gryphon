package gryphon.common;

import gryphon.Application;
import gryphon.UserAction;

import java.util.Collection;
import java.util.HashMap;

/**
 * 
 * @author Evgueni Tiourine
 */

public abstract class AbstractActionContainer implements ActionContainer
{

    private HashMap<Object, UserAction> actions = new HashMap<Object, UserAction>();

    private Application application;

    protected int counter = 1;

    public AbstractActionContainer()
    {
    }
    /**
     * Invokes #initActions() and #initStateMachine(StateMachine)  
     */
    public void init() throws Exception
    {
        initActions();
        // state machine
        StateMachine machine = new StateMachine();
        initStateMachine(machine);
        getApplication().setProperty(StateMachine.class.getName(), machine);

    }

    public UserAction getUserAction(Object key) throws Exception
    {
        if (key instanceof String)
        {
            return getUserAction(Integer.valueOf((String) key));
        }
        return actions.get(key);
    }

    public void putUserAction(Object key, UserAction action) throws Exception
    {
        actions.put(key, action);
    }

    public Object createAction(String actionClassName) throws Exception
    {
        return createAction(Class.forName(actionClassName));
    }

    protected Object createAction(Class actionClass) throws Exception
    {
        UserAction a1 = (UserAction) actionClass.newInstance();
        a1.setApplication(getApplication());
        Object actionKey = new Integer(counter++);
        putUserAction(actionKey, a1);
        return actionKey;
    }

    public Application getApplication()
    {
        return application;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

    protected Collection<UserAction> getAll()
    {
        return actions.values();
    }

}