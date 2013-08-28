package gryphon;

import gryphon.common.Logger;

import java.util.ResourceBundle;

import javax.swing.UIManager;

/**
 * Helper class that may serve as default launcher of Gryphon applications.
 * 
 * @author Evgueni Tiouine
 */

public class Gryphon
{
    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            ResourceBundle b = ResourceBundle.getBundle("gryphon");
            System.out.println("Gryphon " + b.getString("gryphon.version"));
        }
        else
        {
            // запускает только своих знакомых
            Application app = (Application) Class.forName(args[0]).newInstance();
            app.init();
            app.start();
        }
    }
    public static void setSystemLookAndFeel(){
    	try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Throwable e)
		{
			Logger.err("Can't set system look and feel");
			Logger.logThrowable(e);
		}
    }
    public static void launch(Class applicationClass) throws Exception{
    	main(new String[]{applicationClass.getName()});
    }
}