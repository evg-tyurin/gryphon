package gryphon.cmdline;

import gryphon.common.GryphonEvent;
/**
 * Standard action to perform exit from the command line application.
 * Developers may subclass it to add specific functionality.
 * @author ET
 */
public class ExitAction extends CommandLineAction
{
    public ExitAction()
    {
        setCommand("quit");
        setOption("-q");
        setDescription("quit the program");
    }

    public void doAction(GryphonEvent event) throws java.lang.Exception
    {
        ((CommandLineApp) getApplication()).exit();
    }

}