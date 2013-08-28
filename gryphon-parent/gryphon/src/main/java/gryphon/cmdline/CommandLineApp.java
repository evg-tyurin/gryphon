package gryphon.cmdline;

import gryphon.Application;
import gryphon.Form;
import gryphon.UserAction;
import gryphon.common.ActionContainer;
import gryphon.common.Context;
import gryphon.common.DefaultContext;
import gryphon.common.DefaultDescriptor;
import gryphon.common.Descriptor;
import gryphon.common.GryphonNames;
import gryphon.common.Logger;
import gryphon.common.StateMachine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
/**
 * Default implementation of command line Gryphon applications.
 * @author ET
 */
public abstract class CommandLineApp implements Application
{
    protected static final String ACTION_CONTAINER = "actionContainer";

    private Descriptor descriptor = new DefaultDescriptor();

    private Context context;

    private ActionContainer actionContainer;

    public CommandLineApp()
    {
    }

    public void init() throws Exception
    {
        context = new DefaultContext();

        ActionContainer ac = (ActionContainer) Class.forName(
                getDescriptor().getValue(ACTION_CONTAINER)).newInstance();
        ac.setApplication(this);
        ac.initActions();
        // state machine
        StateMachine machine = new StateMachine();
        ac.initStateMachine(machine);
        setProperty(StateMachine.class.getName(), machine);

        this.actionContainer = ac;
    }

    /**
     * Запуск и начало работы.
     */
    public void start() throws Exception
    {
        for (;;)
        {
            printUsage();
            String cmd = readInputLine();
            dispatchCommand(cmd);
        }
    }

    public void handle(Exception e)
    {
        Logger.logThrowable(e);
    }

    public Context getContext() throws Exception
    {
        return context;
    }

    public void changeState(String stateName) throws Exception
    {
        /** @todo Implement this gryphon.common.Application method */
        throw new java.lang.UnsupportedOperationException(
                "Method changeState() not yet implemented.");
    }

    protected void dispatchCommand(String cmd)
    {
        // парсим команду и аргументы
        try
        {
            StringTokenizer st = new StringTokenizer(cmd, " ", false);
            ArrayList args = new ArrayList();
            for (int i = 0;; i++)
            {
                String arg = st.nextToken();
                if (!arg.equals(""))
                {
                    args.add(arg);
                }
                if (!st.hasMoreTokens())
                {
                    break;
                }
            }
            //
            String actionKey = (String) args.remove(0);
            String[] cmdArgs = (String[]) args.toArray(new String[] {});
            getContext().setProperty(GryphonNames.CMDLINE_ARGS, cmdArgs);

            CommandLineActionContainer clac = (CommandLineActionContainer) getActionContainer();
            UserAction action = null;
            if (actionKey.startsWith("-"))
            {
                action = clac.getActionByOption(actionKey);
            }
            else
            {
                action = clac.getActionByCommand(actionKey);
            }
            if (action == null)
            {
                throw new Exception("Action undefined for '" + actionKey + "'");
            }
            action.userActionPerformed(null);
        }
        catch (Exception ex)
        {
            handle(ex);
        }

    }

    /**
     * Читает строку из стандартного входа.
     * 
     * @return строка из стандартного входа
     */
    protected void printUsage()
    {
        String text = getCommandLineActionContainer().list();
        System.out.println();
        System.out.println(text);
        System.out.print(getWelcomeString() + "> ");
    }

    public ActionContainer getActionContainer()
    {
        return actionContainer;
    }

    protected String readInputLine() throws IOException
    {
        int b = -1;
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        while ((b = System.in.read()) != 10)
        {
            if (b >= 32)
            {
                buf.write(b);
            }
        }
        return buf.toString();
    }

    public Object getProperty(Object key) throws java.lang.Exception
    {
        return getContext().getProperty(key);
    }

    public void setProperty(Object key, Object value)
            throws java.lang.Exception
    {
        getContext().setProperty(key, value);
    }

    public Form getActiveState() throws Exception
    {
        throw new Exception("getActiveState() not yet implemented");
    }

    protected abstract String getWelcomeString();

    protected CommandLineActionContainer getCommandLineActionContainer()
    {
        return (CommandLineActionContainer) getActionContainer();
    }

    public void msgbox(String message)
    {
        Logger.info(message);
    }

    public Descriptor getDescriptor()
    {
        return descriptor;
    }

    public void exit()
    {
        Logger.log("normal exit");
        System.exit(0);
    }
}