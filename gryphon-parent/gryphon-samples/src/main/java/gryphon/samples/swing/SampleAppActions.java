package gryphon.samples.swing;


import gryphon.common.AbstractActionContainer;
import gryphon.common.StateMachine;
import gryphon.gui.impl.CloseDialogAction;
import gryphon.gui.impl.ExitAction;

import java.util.HashMap;

public class SampleAppActions extends AbstractActionContainer
{

    public static Object EDIT_PERSON = null;

	public static Object DELETE_PERSON = null;

	public static Object SAVE_PERSON;

	public static Object EXIT;

    public static Object VIEW_PERSONS;

    public static Object CREATE_NEW_PERSON;
    
    public static Object CLOSE_DIALOG;

    public SampleAppActions()
    {//
    }

    public void initActions() throws java.lang.Exception
    {
        VIEW_PERSONS = createAction(ViewPersonListAction.class);
        CREATE_NEW_PERSON = createAction(CreatePersonAction.class);
        SAVE_PERSON = createAction(SavePersonAction.class);
        EDIT_PERSON = createAction(EditPersonAction.class);
        DELETE_PERSON = createAction(DeletePersonAction.class);
        CLOSE_DIALOG = createAction(CloseDialogAction.class);
        EXIT = createAction(ExitAction.class);
    }

    public void initStateMachine(StateMachine machine) throws Exception
    {
        HashMap rule = machine.createRule("*");
        machine.registerForm(rule,PersonListForm.class);
        machine.registerForm(rule,EditPersonDialog.class);
    }

}