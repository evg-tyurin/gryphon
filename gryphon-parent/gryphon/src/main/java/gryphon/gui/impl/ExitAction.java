package gryphon.gui.impl;

import gryphon.UserAction;
import gryphon.common.GryphonEvent;

import javax.swing.JOptionPane;
/**
 * Standard action to perform exit from the command line application.
 * Developers may subclass it to add specific functionality.
 * @author ET
 */
public class ExitAction extends UserAction
{
    public ExitAction()
    {
        super("Выход");
    }

    public void doAction(GryphonEvent event) throws java.lang.Exception
    {
        int op = JOptionPane.showConfirmDialog(null,"Выйти из системы?","Exit",JOptionPane.YES_NO_OPTION);
        if (op==JOptionPane.YES_OPTION)
            System.exit(0);
    }

}