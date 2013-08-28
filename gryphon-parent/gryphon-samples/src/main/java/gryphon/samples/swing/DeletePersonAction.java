package gryphon.samples.swing;

import java.util.Arrays;

import gryphon.Entity;
import gryphon.UserAction;
import gryphon.common.FriendlyException;
import gryphon.common.GryphonEvent;

public class DeletePersonAction extends UserAction
{
	public DeletePersonAction(){
		super("������� �������");
	}
    public void doAction(GryphonEvent event) throws java.lang.Exception
    {
    	PersonListForm form = (PersonListForm) getApplication().getActiveState();
    	Entity[] selected = form.personListView.getSelection();
    	if (selected.length==0)
    		throw new FriendlyException("�������� �������");
    	// ��� ������ �������� �� ��
    	// ...
    	// ����� �� ������
    	form.personListView.getList().removeAll(Arrays.asList(selected));
    	getApplication().msgbox("�������� �������");
    }

}