package gryphon.samples.swing;

import java.util.ArrayList;
import java.util.List;

import gryphon.View;
import gryphon.common.DefaultDescriptor;
import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;
import gryphon.gui.impl.DefaultForm;
import gryphon.gui.impl.JTableListView;
/**
 * Экранная форма для просмотра списка контактов. 
 * @author ET
 */
public class PersonListForm extends DefaultForm
{
	private static final long serialVersionUID = 1L;
	
	JTableListView personListView;
	
    public PersonListForm()
    {//
    }

    protected void createComponents() throws Exception
    {
        personListView = (JTableListView) createView(JTableListView.class, getTableDescriptor(), CENTER);
    }

    private Descriptor getTableDescriptor() throws Exception
	{
    	Descriptor d = new DefaultDescriptor();
    	d.addEntry(new DescriptorEntry("First Name", "FirstName"));
    	d.addEntry(new DescriptorEntry("Last Name", "LastName"));
    	d.addEntry(new DescriptorEntry("Phone", "Phone"));
    	d.addEntry(new DescriptorEntry("Email", "Email"));
    	d.addEntry(new DescriptorEntry("Birth Date", "BirthDate"));
    	d.addEntry(new DescriptorEntry("Age", "Age"));
    	d.addEntry(new DescriptorEntry("Flagged", "Flagged",DescriptorEntry.TYPE_CHECKBOX,false));
		return d;
	}

	public View getMainView() throws java.lang.Exception
    {
        return personListView;
    }

    protected void createButtons() throws java.lang.Exception
    {
    	createButton(SampleAppActions.CREATE_NEW_PERSON);
    	createButton(SampleAppActions.EDIT_PERSON);
    	createButton(SampleAppActions.DELETE_PERSON);
    }

	public void resume() throws Exception
	{
		List personList = (List) getProperty(SampleAppNames.PERSON_LIST);
		if (personList==null)
			personList = new ArrayList();
		personListView.setList(personList);
		personListView.updateView();
	}
}