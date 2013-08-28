package gryphon.samples.swing;

import java.util.Arrays;

import gryphon.Entity;
import gryphon.UserAction;
import gryphon.common.FriendlyException;
import gryphon.common.GryphonEvent;

public class DeletePersonAction extends UserAction
{
	public DeletePersonAction(){
		super("Удалить контакт");
	}
    public void doAction(GryphonEvent event) throws java.lang.Exception
    {
    	PersonListForm form = (PersonListForm) getApplication().getActiveState();
    	Entity[] selected = form.personListView.getSelection();
    	if (selected.length==0)
    		throw new FriendlyException("Выберите контакт");
    	// тут удляем контакты из БД
    	// ...
    	// потом из списка
    	form.personListView.getList().removeAll(Arrays.asList(selected));
    	getApplication().msgbox("Контакты удалены");
    }

}