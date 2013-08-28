package gryphon.gui.impl;

import gryphon.common.AbstractActionContainer;
import gryphon.common.StateMachine;

public class StandardActions extends AbstractActionContainer
{
    public static Object DELETE_FROM_LISTVIEW;

    public StandardActions()
    {
    }

    public void initActions() throws Exception
    {
        DELETE_FROM_LISTVIEW = createAction(DeleteFromListViewAction.class.getName());
    }

    public void initStateMachine(StateMachine machine) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

}