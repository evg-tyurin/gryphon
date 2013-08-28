package gryphon.samples.cmdline;

import gryphon.Gryphon;
import gryphon.cmdline.CommandLineApp;

public class SampleApp extends CommandLineApp
{
    public SampleApp()
    {
    }

    protected String getWelcomeString()
    {
        return "sample_app";
    }

    public void init() throws java.lang.Exception
    {
        getDescriptor().putValue(ACTION_CONTAINER,
                SampleAppActions.class.getName());
        super.init();
    }

    public static void main(String[] args) throws Exception
    {
        Gryphon.main(new String[] { SampleApp.class.getName() });
    }

}