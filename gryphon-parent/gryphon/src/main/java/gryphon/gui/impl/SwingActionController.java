package gryphon.gui.impl;

import gryphon.UserAction;
import gryphon.common.ActionContainer;
import gryphon.common.Logger;
import gryphon.gui.GuiApplication;
import gryphon.gui.awt_impl.AwtActionController;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
/**
 * Контроллер действий пользователя с возможностью отображать блокирующее модальное окно 
 * с прогресс-баром и/или сообщением.
 * 
 * @see UserAction#isProgressable()
 * @author etyurin
 *
 */
public class SwingActionController extends AwtActionController implements Action
{
	protected ProgressBar progressBar;  

	public void actionPerformed(final ActionEvent e)
	{
        try
		{
			ActionContainer actionCont = ((GuiApplication) getApplication()).getActionContainer();
			UserAction action = actionCont.getUserAction(e.getActionCommand());
			
			if (action.isProgressable()){
			    getProgressBar().show();

				Thread thread = new Thread(new Runnable(){

					public void run()
					{
						try
						{
							SwingActionController.super.actionPerformed(e);
						}
						finally
						{
				            getProgressBar().hide();
						}
					}});
				thread.start();
			}
			else{
				super.actionPerformed(e);
			}
		}
		catch (Exception e1)
		{
			getApplication().handle(e1);
		}
	}
	protected ProgressBar getProgressBar()
	{
		if (progressBar==null)
		{
			progressBar = new ProgressBar((Component)getApplication());
			try
			{
				getApplication().setProperty("progressBar", progressBar);
			}
			catch (Exception e)
			{
				Logger.logThrowable(e);
			}
		}
		return progressBar;
	}

    public Object getValue(String key)
    {
        return null;
    }

    public void putValue(String key, Object value)
    {
        throw new RuntimeException("Not implemented");
    }

    public void setEnabled(boolean b)
    {
        throw new RuntimeException("Not implemented");
    }

    public boolean isEnabled()
    {
        return true;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener)
    {//
    }

    public void removePropertyChangeListener(PropertyChangeListener listener)
    {//
    }

}