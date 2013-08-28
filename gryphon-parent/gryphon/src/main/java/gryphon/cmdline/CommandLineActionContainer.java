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
     *            команда, которую набрад юзер в консоли
     * @return экшн
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
     *            опция, которую набрад юзер в консоли
     * @return экшн
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
     * Делает проверку, чтобу у экшена была установлена команда и опция.
     * 
     * @param actionClass
     *            класс экшена, из которого будет создан его экземпляр
     * @return ключ экшена, под которым он хранится в контейнере
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
     * Выдает список комманд и их описаний для показа юзеру.
     * 
     * @return текст
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
     * Добавляет стандартный экшн выхода из консоли. Потомки должны вызывать
     * super.init(), если им необходим этот экшн.
     * 
     * @throws java.lang.Exception
     */
    public void init() throws java.lang.Exception
    {
        EXIT = createAction(ExitAction.class);
    }
}