package gryphon.gui.awt_impl;

import gryphon.Application;
import gryphon.Form;
import gryphon.Navigation;
import gryphon.UserAction;
import gryphon.common.ActionContainer;
import gryphon.common.ActionController;
import gryphon.common.GryphonEvent;
import gryphon.common.Logger;
import gryphon.common.StateMachine;
import gryphon.gui.GuiApplication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AwtActionController implements ActionListener, ActionController
{
    private Application application;

    public void actionPerformed(ActionEvent e)
    {
        try
        {
            ActionContainer actionCont = ((GuiApplication) getApplication()).getActionContainer();
            UserAction action = actionCont.getUserAction(e.getActionCommand());

            action.userActionPerformed(new GryphonEvent(e.getSource()));

            while (action.getNextAction()!=null)
            {
                action = actionCont.getUserAction(action.getNextAction()); 
                action.userActionPerformed(new GryphonEvent(e.getSource()));
            }
            String nextFormKey = (String) action.getValue(UserAction.NEXT_FORM);
            String nextForm = null;
            if (!Navigation.CLOSE_DIALOG.equals(nextFormKey))
            {
                if (nextFormKey==null)
                {
                    nextFormKey = action.getClass().getName();
                }
                Form currentForm = getApplication().getActiveState();
                String currentFormKey = "*";
                if (currentForm!=null){
                    currentFormKey = currentForm.getClass().getName();
                }
                nextForm = getFormName(currentFormKey, nextFormKey);
            }
            else
            {
                nextForm = nextFormKey;
            }
            if (nextForm != null)
            {
                getApplication().changeState(nextForm);
            }
            else
            {
                Logger.debug("resume the same form");
                getApplication().getActiveState().resume();
            }
        }
        catch (Exception ex)
        {
            getApplication().handle(ex);
        }
    }
    /**
     * Определяет следующую форму для перехода
     * @param currentForm
     * @param nextFormKey 
     * @return
     * @throws Exception 
     */
    protected String getFormName(String currentForm, String nextFormKey) throws Exception
    {
        StateMachine machine = (StateMachine) getApplication().getProperty(StateMachine.class.getName());
        return machine.getForm(currentForm, nextFormKey);
    }

    public Application getApplication()
    {
        return this.application;
    }

    public void setApplication(Application application)
    {
        this.application = application;
    }

}
