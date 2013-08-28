package gryphon.samples.swing;

import gryphon.UserAction;
import gryphon.common.GryphonEvent;
/**
 * �������� ������������ ��� �������� ������ ��������.
 * ������� � ����������� ����� ��������� ������ {@link Person} � ��������� ��� � ������ 
 * ��� ������������ ������������� ��������-������ (� ������ ���������� ��� {@link EditPersonDialog}).
 * @author ET
 */
public class CreatePersonAction extends UserAction
{
	public CreatePersonAction(){
		super("�������� �������");
        setNextForm(EditPersonDialog.class.getName());
	}
    public void doAction(GryphonEvent event) throws java.lang.Exception
    {
    	Person p = new Person();
    	getApplication().setProperty(SampleAppNames.PERSON, p);
    }

}