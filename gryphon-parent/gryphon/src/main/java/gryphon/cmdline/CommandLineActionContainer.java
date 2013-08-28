package gryphon.cmdline;

import gryphon.common.AbstractActionContainer;

import java.util.Collection;
import java.util.Iterator;

/**
 * <p>
 * Implementation of ActionContainer for using in command line Gryphon applications.
 * </p>
 * 
 * @author Evgueni Tiourine
 */

public abstract class CommandLineActionContainer extends
        AbstractActionContainer
{
    public static Object EXIT;

    public CommandLineActionContainer()
    {
    }

    /**
     * 
     * @param command
     *            �������, ������� ������ ���� � �������
     * @return ����
     */
    public CommandLineAction getActionByCommand(String command)
    {
        Collection coll = getAll();
        for (Iterator i = coll.iterator(); i.hasNext();)
        {
            CommandLineAction a = (CommandLineAction) i.next();
            if (command.equals(a.getCommand()))
            {
                return a;
            }
        }
        return null;
    }

    /**
     * 
     * @param option
     *            �����, ������� ������ ���� � �������
     * @return ����
     */
    public CommandLineAction getActionByOption(String option)
    {
        Collection coll = getAll();
        for (Iterator i = coll.iterator(); i.hasNext();)
        {
            CommandLineAction a = (CommandLineAction) i.next();
            if (option.equals(a.getOption()))
            {
                return a;
            }
        }
        return null;
    }

    /**
     * ������ ��������, ����� � ������ ���� ����������� ������� � �����.
     * 
     * @param actionClass
     *            ����� ������, �� �������� ����� ������ ��� ���������
     * @return ���� ������, ��� ������� �� �������� � ����������
     * @throws java.lang.Exception
     */
    protected Object createAction(Class actionClass) throws java.lang.Exception
    {
        Object actionKey = super.createAction(actionClass);
        CommandLineAction a = (CommandLineAction) getUserAction(actionKey);
        if (a.getCommand() == null || a.getCommand().equals(""))
        {
            throw new Exception("Command undefined for action "
                    + actionClass.getName());
        }
        if (a.getOption() == null || a.getOption().equals(""))
        {
            throw new Exception("Option undefined for action "
                    + actionClass.getName());
        }
        return actionKey;
    }

    /**
     * ������ ������ ������� � �� �������� ��� ������ �����.
     * 
     * @return �����
     */
    public String list()
    {
        String s = "";
        for (Iterator i = getAll().iterator(); i.hasNext();)
        {
            CommandLineAction a = (CommandLineAction) i.next();
            s += a.getCommand() + " " + a.getOption() + " "
                    + a.getDescription() + "\n";
        }
        return s;
    }

    /**
     * ��������� ����������� ���� ������ �� �������. ������� ������ ��������
     * super.init(), ���� �� ��������� ���� ����.
     * 
     * @throws java.lang.Exception
     */
    public void init() throws java.lang.Exception
    {
        EXIT = createAction(ExitAction.class);
    }
}