package gryphon.samples.cmdline;

import gryphon.cmdline.CommandLineActionContainer;
import gryphon.cmdline.ExitAction;
import gryphon.common.StateMachine;

public class SampleAppActions extends CommandLineActionContainer
{
    public SampleAppActions()
    {
    }

    public void initActions() throws java.lang.Exception
    {
        createAction(ExitAction.class);
        createAction(HelloAction.class);
    }

    public void initStateMachine(StateMachine machine) throws Exception
    {
        // TODO Auto-generated method stub
        
    }

}