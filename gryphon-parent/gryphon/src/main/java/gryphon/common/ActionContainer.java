package gryphon.common;

import gryphon.*;

/**
 * @author Evgueni Tiourine
 */

public interface ActionContainer
{
    void setApplication(Application application);

    Application getApplication();

    void init() throws Exception;

    void initActions() throws Exception;

    void initStateMachine(StateMachine machine) throws Exception;

    UserAction getUserAction(Object key) throws Exception;

    void putUserAction(Object key, UserAction action) throws Exception;

}