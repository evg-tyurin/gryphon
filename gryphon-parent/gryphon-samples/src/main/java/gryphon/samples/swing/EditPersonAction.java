package gryphon.samples.swing;

import gryphon.UserAction;
import gryphon.common.FriendlyException;
import gryphon.common.GryphonEvent;

public class EditPersonAction extends UserAction
{
	public EditPersonAction(){
		super("�������� �������");
        setNextForm(EditPersonDialog.class.getName());
	}
    public void doAction(GryphonEvent event) throws java.lang.Exception
    {
    	PersonListForm form = (PersonListForm) getApplication().getActiveState();
    	Person p = (Person) form.personListView.getSelectedEntity();
    	if (p==null)
    		throw new FriendlyException("�������� �������");
    	getApplication().setProperty(SampleAppNames.PERSON, p);
    }

}