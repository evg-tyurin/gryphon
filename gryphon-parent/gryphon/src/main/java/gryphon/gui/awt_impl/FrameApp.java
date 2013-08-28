package gryphon.gui.awt_impl;

import gryphon.Form;
import gryphon.UserAction;
import gryphon.common.ActionContainer;
import gryphon.common.ActionController;
import gryphon.common.Context;
import gryphon.common.DefaultContext;
import gryphon.common.DefaultDescriptor;
import gryphon.common.Descriptor;
import gryphon.common.FriendlyException;
import gryphon.common.Logger;
import gryphon.gui.FormContainer;
import gryphon.gui.GuiApplication;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class FrameApp extends Frame implements GuiApplication
{
	private static final long serialVersionUID = 1L;
	//duplicated from JFrameApp
    protected static final String TITLE = "title";
    //duplicated from JFrameApp
    protected static final String ACTION_CONTAINER = "actionContainer";

    private Context context;

    private FormContainer formContainer;

    private ActionContainer actionContainer;

    private Descriptor descriptor = new DefaultDescriptor();

    private AwtActionController actionController;
    
    private Label statusBar = new Label("Status bar");
    
    public FrameApp()
    {
        addWindowListener(new WindowAdapter(){

            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }

            });
//        JFrameApplication.application = this;
        setMenuBar(new MenuBar());

    }

    public Component getComponent()
    {
        return this;
    }

    public FormContainer getFormContainer() throws Exception
    {
        return formContainer;
    }

    public ActionController getActionController()
    {
        return actionController;
    }

    public void init() throws Exception
    {
        Logger.log("Make the frame visible");
        setTitle(getDescriptor().getValue(TITLE));
        setSize(800, 600);
        setLocation(100, 100);
        // создать контекст
        this.context = new DefaultContext();
        // создать контейнер для форм
        createFormContainer();
        // создать все системные экшены
        createActions();
        // создать главное меню
        createMenuBar();
    }

    public void start() throws Exception
    {
        setVisible(true);        
    }

    public void handle(Exception ex)
    {
        ex.printStackTrace();
        if (ex instanceof FriendlyException)
        {
            FriendlyException myex = (FriendlyException) ex;
            if (myex.getThrowable()!=null)
                myex.getThrowable().printStackTrace();
        }
        String text = ex.getClass().getName() + ": " + ex.getMessage();
        msgbox(text);
        
    }

    public Context getContext() throws Exception
    {
        return context;
    }

    public ActionContainer getActionContainer()
    {
        return actionContainer;
    }

    public void changeState(String stateName) throws Exception
    {
        getFormContainer().switchToForm(stateName);        
    }

    public void setProperty(Object key, Object value) throws Exception
    {
        getContext().setProperty(key, value);
    }

    public Object getProperty(Object key) throws Exception
    {
        return getContext().getProperty(key);
    }

    public Form getActiveState() throws Exception
    {
        return getFormContainer().getActiveForm();
    }

    public void msgbox(String message)
    {
        Logger.debug(message);
        this.statusBar.setText(message);
        this.statusBar.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, statusBar.getSize().height);
    }
    public Descriptor getDescriptor() {
        return descriptor;
    }
    protected void createFormContainer() throws Exception {
        FormContainer fc = new AwtFormContainer(this);
        fc.getComponent().setVisible(true);

        setLayout(new BorderLayout());
        add(fc.getComponent(), BorderLayout.CENTER);

        Panel p = new Panel(new GridLayout(1,1));
        p.add(statusBar);
        add(p, BorderLayout.SOUTH);

        this.formContainer = fc;
    }
    protected void createActions() throws Exception {
    	String actionControllerClassName = this.getClass().getName() + "Controller";
    	try
		{
			actionController = (AwtActionController) Class.forName(actionControllerClassName).newInstance();
		}
		catch (ClassNotFoundException e)
		{
			Logger.debug("Controller class not found. Default controller will be used.");
	        actionController = new AwtActionController();
		}
        actionController.setApplication(this);

        String actionContainerClassName = this.getClass().getName() + "Actions";
        if (getDescriptor().getValue(ACTION_CONTAINER) != null)
        {
            actionContainerClassName = getDescriptor().getValue(ACTION_CONTAINER);
        }
        ActionContainer ac = (ActionContainer) Class.forName(actionContainerClassName).newInstance();
        ac.setApplication(this);
        ac.init();
        this.actionContainer = ac;
    }
    protected Menu createMenu(String name) {
        Menu menu = new Menu(name);
        getMenuBar().add(menu);
        return menu;
      }
    protected abstract void createMenuBar() throws Exception;
    /**
     * Создает пункт подменю в указанном пункте главного меню.
     * @param menu пункт главного меню
     * @param actionKey ключ действия, которое будет привязано к данному пункту подменю
     * @return созданный пункт подменю
     * @throws java.lang.Exception если ключ не существует или еще что-то
     */
    protected MenuItem createMenuItem(Menu menu, Object actionKey) throws Exception {
      MenuItem item = new MenuItem();//getActionController());
      item.addActionListener((AwtActionController)getActionController());
      item.setActionCommand(actionKey.toString());
      item.setLabel((String)getActionContainer().getUserAction(actionKey).getValue(UserAction.NAME));
      menu.add(item);
      return item;
    }

}
