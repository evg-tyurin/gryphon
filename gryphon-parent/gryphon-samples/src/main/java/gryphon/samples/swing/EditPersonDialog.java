package gryphon.samples.swing;

import java.awt.Dimension;

import gryphon.View;
import gryphon.common.DefaultDescriptor;
import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;
import gryphon.gui.impl.DefaultDialog;
import gryphon.gui.impl.GridView;

import javax.swing.JButton;
/**
 * Диалоговая форма для изменения данных нового или существующего контакта.
 * @author ET
 */
public class EditPersonDialog extends DefaultDialog
{
	private static final long serialVersionUID = 1L;
	
	GridView personView;
	
	private JButton bSave;
	
	private JButton bCancel;
	
    protected void createButtons() throws Exception
    {
    	bSave = createButton(SampleAppActions.SAVE_PERSON);
    	bCancel = createButton(SampleAppActions.CLOSE_DIALOG);
    }

    protected void createComponents() throws Exception
    {
        personView = (GridView) createView(GridView.class, getGridDescriptor(), CENTER);
        Dimension size = ((SampleApp)getContainer().getApplication()).getSize();
        personView.setPreferredSize(new Dimension(size.width/2,size.height/2));
    }

    private Descriptor getGridDescriptor() throws Exception
	{
    	Descriptor d = new DefaultDescriptor();
    	d.addEntry(new DescriptorEntry("First Name", "FirstName"));
    	d.addEntry(new DescriptorEntry("Last Name", "LastName"));
    	d.addEntry(new DescriptorEntry("Phone", "Phone"));
    	d.addEntry(new DescriptorEntry("Email", "Email"));
    	d.addEntry(new DescriptorEntry("Birth Date", "BirthDate"));
    	d.addEntry(new DescriptorEntry("Age", "Age"));
    	d.addEntry(new DescriptorEntry("Flagged", "Flagged",DescriptorEntry.TYPE_CHECKBOX,true));
		return d;
	}

	public View getMainView() throws Exception
    {
        return personView;
    }

    public String getTitle()
    {
        return "Информация о человеке";
    }

    public Object[] getOptions()
    {
        return new Object[]{bSave,bCancel};
    }

    public Object getDefaultOption()
    {
        return bSave;
    }

    public Object getCloseOption()
    {
        return bCancel;
    }

	public void resume() throws Exception
	{
		Person p = (Person) getProperty(SampleAppNames.PERSON);
		personView.setEntity(p);
		personView.updateView();
	}

}
