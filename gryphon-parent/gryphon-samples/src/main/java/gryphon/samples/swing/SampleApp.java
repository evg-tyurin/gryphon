package gryphon.samples.swing;

import gryphon.Gryphon;
import gryphon.gui.impl.EmptyForm;
import gryphon.gui.impl.JFrameApplication;

import javax.swing.JMenu;

public class SampleApp extends JFrameApplication
{
    public SampleApp()
    {
    }

    public void init() throws Exception
    {
        super.init();
        setTitle("Sample Gryphon application");
    }

    public void start() throws Exception
	{
		super.start();
		changeState(EmptyForm.class.getName());
	}

	protected void createMenuBar() throws java.lang.Exception
    {
        JMenu file = createMenu("File");
        createMenuItem(file, SampleAppActions.CREATE_NEW_PERSON);
        createMenuItem(file, SampleAppActions.VIEW_PERSONS);
        createMenuItem(file, SampleAppActions.EXIT);
    }
    public static void main(String[] args) throws Exception
    {
    	Gryphon.setSystemLookAndFeel();
        Gryphon.launch(SampleApp.class);
    }
}