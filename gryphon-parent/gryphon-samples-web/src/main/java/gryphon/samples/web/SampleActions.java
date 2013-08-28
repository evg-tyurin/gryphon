package gryphon.samples.web;

import gryphon.common.StateMachine;
import gryphon.web.WebActionContainer;

public class SampleActions extends WebActionContainer
{
    public static Object HELLO;

    public SampleActions()
    {
    }

    public void initActions() throws java.lang.Exception
    {
        HELLO = createAction(HelloAction.class);
    }

    public void initStateMachine(StateMachine machine) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

}