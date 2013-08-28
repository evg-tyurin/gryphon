package gryphon.gui.impl;

import gryphon.Application;
import gryphon.Form;
import gryphon.UserAction;
import gryphon.common.ActionContainer;
import gryphon.common.ActionController;
import gryphon.common.Context;
import gryphon.common.DefaultContext;
import gryphon.common.DefaultDescriptor;
import gryphon.common.Descriptor;
import gryphon.common.Logger;
import gryphon.common.MyException;
import gryphon.common.StateMachine;
import gryphon.gui.FormContainer;
import gryphon.gui.GuiApplication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Реализация приложения на основе Swing.
 * @author ET
 */

public abstract class JFrameApplication extends JFrame implements GuiApplication
{
	private static final long serialVersionUID = 1L;

	protected static final String TITLE = "title";

    /**
     * @deprecated более правильно, начиная с версии 0.1.6, называть класс
     *             экшенов <app_name>Actions
     */
    protected static final String ACTION_CONTAINER = "actionContainer";

    private Descriptor descriptor = new DefaultDescriptor();

    private static Application application;

    private Context context;

    private FormContainer formContainer;

    private ActionContainer actionContainer;

    private SwingActionController actionController;

    private JLabel statusBar = new JLabel("");

    public JFrameApplication()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JFrameApplication.application = this;
        setJMenuBar(new JMenuBar());
    }

    public static Application getInstance()
    {
        return JFrameApplication.application;
    }

    public void init() throws Exception
    {
        Logger.log("Make the frame visible");
        setTitle(getDescriptor().getValue(TITLE));
        centerAndSize(90);
        // создать контекст
        this.context = new DefaultContext();
        // прочитать все проперти, если они есть
        // стандартное название пропертей:
        // для mypackage.MyApp - это myapp.properties
        String bundleName = this.getClass().getName();
        bundleName = bundleName.substring(bundleName.lastIndexOf(".") + 1).toLowerCase();
        ResourceBundle bundle = null;
        try
        {
            bundle = ResourceBundle.getBundle(bundleName);
        }
        catch (MissingResourceException ex)
        {
            // если бандла нет, то может это и не плохо
            Logger.debug(ex.getMessage());
        }
        if (bundle != null)
        {
            for (Enumeration keys = bundle.getKeys(); keys.hasMoreElements();)
            {
                Object key = keys.nextElement();
                setProperty(key, bundle.getString((String) key));
            }
        }
        // создать контейнер для форм
        createFormContainer();
        // создать все системные экшены
        createActions();
        // создать главное меню
        createMenuBar();
        // инициация разных классов
        MyPopupFactory.init(this);
    }
    /** 
     * Отцентровать окно программы и установить размер окна в процентах от размеров экрана.
     * @param percent проценты от размера экрана
     */
	protected void centerAndSize(int percent)
	{
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(percent*size.width/100, percent*size.height/100);
		setLocation((size.width-getWidth())/2, (size.height-getHeight())/2);
	}

    public Component getComponent()
    {
        return this;
    }

    protected void createActions() throws Exception
    {
    	String actionControllerClassName = this.getClass().getName() + "Controller";
    	try
		{
			actionController = (SwingActionController) Class.forName(actionControllerClassName).newInstance();
		}
		catch (ClassNotFoundException e)
		{
			Logger.debug("Controller class not found. Default controller will be used.");
	        actionController = new SwingActionController();
		}
        actionController.setApplication(this);

        String actionContainerClassName = this.getClass().getName() + "Actions";
        if (getDescriptor().getValue(ACTION_CONTAINER) != null)
        {
            actionContainerClassName = getDescriptor().getValue(ACTION_CONTAINER);
        }
        ActionContainer ac = (ActionContainer) Class.forName(actionContainerClassName).newInstance();
        ac.setApplication(this);
        ac.initActions();
        // state machine
        StateMachine machine = new StateMachine();
        ac.initStateMachine(machine);
        setProperty(StateMachine.class.getName(), machine);
        this.actionContainer = ac;
    }

    public void handle(Exception e)
    {
        String text = "";
        if (e instanceof MyException)
        {
            text = e.getMessage();
            Throwable th = ((MyException) e).getThrowable();
            if (th != null)
            {
                th.printStackTrace();
            }
        }
        else
        {
            text = e.getClass().getName() + ": " + e.getMessage();
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(getComponent(), text, "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }

    protected void createFormContainer() throws Exception
    {
        FormContainer fc = new SwingFormContainer(this);
        fc.getComponent().setVisible(true);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(fc.getComponent(), BorderLayout.CENTER);

        JPanel p = new JPanel(new BorderLayout());
        p.add(statusBar, BorderLayout.WEST);
        getContentPane().add(p, BorderLayout.SOUTH);

        this.formContainer = fc;
    }

    protected abstract void createMenuBar() throws Exception;

    protected JMenu createMenu(String name)
    {
        JMenu menu = new JMenu(name);
        getJMenuBar().add(menu);
        return menu;
    }

    /**
     * Создает пункт подменю в указанном пункте главного меню.
     * 
     * @param menu
     *            пункт главного меню
     * @param actionKey
     *            ключ действия, которое будет привязано к данному пункту
     *            подменю
     * @return созданный пункт подменю
     * @throws java.lang.Exception
     *             если ключ не существует или еще что-то
     */
    protected JMenuItem createMenuItem(JMenu menu, Object actionKey)
            throws Exception
    {
        JMenuItem item = new JMenuItem((SwingActionController)getActionController());
        item.setActionCommand(actionKey.toString());
        item.setText((String) getActionContainer().getUserAction(actionKey).getValue(UserAction.NAME));
        menu.add(item);
        return item;
    }

    public void msgbox(String message)
    {
        JOptionPane.showMessageDialog(getComponent(), message, "Сообщение",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public Context getContext() throws Exception
    {
        return context;
    }

    public FormContainer getFormContainer() throws Exception
    {
        return this.formContainer;
    }

    public ActionContainer getActionContainer()
    {
        return this.actionContainer;
    }

    public Descriptor getDescriptor()
    {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    public void start() throws Exception
    {
        setVisible(true);
//        changeState(EmptyForm.class.getName());
    }

    public void changeState(String stateName) throws Exception
    {
        getFormContainer().switchToForm(stateName);
    }

    public Object getProperty(Object key) throws java.lang.Exception
    {
        return getContext().getProperty(key);
    }

    public ActionController getActionController()
    {
        return actionController;
    }

    public void setProperty(Object key, Object value) throws Exception
    {
        getContext().setProperty(key, value);
    }

    public Form getActiveState() throws Exception
    {
        return getFormContainer().getActiveForm();
    }

    public void setStatus(String text, boolean isImportant)
    {
        statusBar.setText(text);
        statusBar.setForeground(isImportant?Color.RED:Color.black);
    }
    
}