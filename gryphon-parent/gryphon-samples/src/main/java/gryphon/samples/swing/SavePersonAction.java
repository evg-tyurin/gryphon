package gryphon.samples.swing;

import gryphon.common.GryphonEvent;

import java.util.List;

public class SavePersonAction extends ViewPersonListAction
{
	public SavePersonAction(){
		super("���������");
        setNextForm(PersonListForm.class.getName());
	}
    public void doAction(GryphonEvent event) throws java.lang.Exception
    {
    	EditPersonDialog form = (EditPersonDialog) getApplication().getActiveState();
    	form.personView.updateEntity();
    	Person p = (Person) form.personView.getEntity();
    	boolean addToList = p.getId()==null;
    	// ��� ��������� ������� � ��
    	// ...
    	// � �������� ���������� id
    	if (p.getId()==null)
    		p.setId(""+p.hashCode());
    	// ����� ��������� � ������
    	List personList = (List) getApplication().getProperty(SampleAppNames.PERSON_LIST);
    	if (personList==null){
    		personList = getPersonList();
    		getApplication().setProperty(SampleAppNames.PERSON_LIST, personList);
    	}
    	if (addToList)
    		personList.add(p);
    }

}
