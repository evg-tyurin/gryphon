package gryphon.samples.cmdline;

import gryphon.cmdline.CommandLineAction;
import gryphon.common.GryphonEvent;

public class HelloAction extends CommandLineAction
{
    public HelloAction()
    {
        setCommand("hello");
        setOption("-h");
        setDescription("prints hello message");
    }
    public void doAction(GryphonEvent event) throws Exception
    {
        System.out.println("--------------");
        System.out.println("Hello world");
        System.out.println("--------------");
    }

}
