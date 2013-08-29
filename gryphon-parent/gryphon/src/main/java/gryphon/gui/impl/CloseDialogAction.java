package gryphon.gui.impl;

import gryphon.Navigation;
import gryphon.UserAction;
import gryphon.common.GryphonEvent;
/**
 * ƒействие пользовател€ прии закрытии диалоговой формы.
 * @author ET
 *
 */
public class CloseDialogAction extends UserAction
{
	public CloseDialogAction(){
		super("«акрыть");
		setNextForm(Navigation.CLOSE_DIALOG);
	}
	public CloseDialogAction(String name){
		super(name);
		setNextForm(Navigation.CLOSE_DIALOG);
	}
	public void doAction(GryphonEvent event) throws Exception
	{// ничего не делаем
	}

}
