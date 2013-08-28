package gryphon.cmdline;

import gryphon.UserAction;
import gryphon.common.GryphonNames;

/**
 * Implementation of UserAction for using in command line Gryphon applications.
 * 
 * @author Evgueni Tiourine
 */
public abstract class CommandLineAction extends UserAction
{
    /**
     * Полное название команды, которая приводит к запуску действия. Например,
     * 'help'.
     */
    private String command;

    /**
     * Краткое название команды (опция), которая приводит к запуску действия.
     * Например, '-h' вместо 'help'.
     */
    private String option;

    private String description;

    public CommandLineAction()
    {
    }

    public CommandLineAction(String name)
    {
        super(name);
    }

    public String getCommand()
    {
        return command;
    }

    public String getOption()
    {
        return option;
    }

    public void setOption(String option)
    {
        this.option = option;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String[] getArgs() throws Exception
    {
        String[] args = (String[]) getApplication().getProperty(
                GryphonNames.CMDLINE_ARGS);
        return args;
    }
}