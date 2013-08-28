package gryphon;

import gryphon.common.ActionContainer;
import gryphon.common.Context;

/**
 * 
 * @author Evgueni Tiourine
 */

public interface Application
{
    public static final String ACTION_CONTAINER = "actionContainer";
    /**
     * Method is invoked by launcher of the aplication at start up process.
     * Order of invocations: default constructor, init(), start();
     * @throws Exception
     */
    void init() throws Exception;
    /**
     * Method is invoked by launcher of the aplication at start up process.
     * Order of invocations: default constructor, init(), start();
     * @throws Exception
     */
    void start() throws Exception;
    /**
     * Handles the exception.
     * The way the exception is handled may vary depending of GRyphon implementation.
     * @param ex
     */
    void handle(Exception ex);

    /**
     * 
     * @return контекст приложения
     * @throws Exception
     * @deprecated use Application#setProperty(Object, Object) и
     *             Application#getProperty(Object)
     */
    Context getContext() throws Exception;

    ActionContainer getActionContainer();
    /**
     * Changes state of the application.
     * @param stateName name of the next state
     * @throws Exception
     */
    void changeState(String stateName) throws Exception;
    /**
     * Store property
     * @param key
     * @param value
     * @throws Exception
     */
    void setProperty(Object key, Object value) throws Exception;
    /**
     * Restore property
     * @param key
     * @return property value or null
     * @throws Exception
     */
    Object getProperty(Object key) throws Exception;
    /**
     * 
     * @return current state of the application
     * @throws Exception
     */
    Form getActiveState() throws Exception;
    /**
     * Shows textual message to the user.
     * The way the text is shown may vary depending of Gryphon implementation.
     * It may be message box in Swing/AWT/SWT/web applications or just text output
     * in command line applications.
     * @param message text to be shown to the user
     */
    void msgbox(String message);

}
