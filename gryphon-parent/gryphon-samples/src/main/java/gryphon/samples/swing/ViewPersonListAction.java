package gryphon.samples.swing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gryphon.UserAction;
import gryphon.common.GryphonEvent;
import gryphon.gui.impl.ProgressBar;
/**
 * �������� ������������ ��� ��������� ������ ���������.
 * �������� ������ ��������� � ��������� ��� � ������ ��� ������������ ������������� ��������-������.
 * @author ET
 */
public class ViewPersonListAction extends UserAction
{
	private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	
    public ViewPersonListAction()
    {
        this("�������� �����");
    }
    public ViewPersonListAction(String name)
    {
        super(name);
        putValue(NEXT_FORM, PersonListForm.class.getName());
        setProgressable(true);
    }

    public void doAction(GryphonEvent event) throws Exception
    {
    	ProgressBar pb = getProgressBar();
    	pb.setText("��������� 3 ��� ��� ������������ � ������������� ����������� ��������");
    	Thread.sleep(3000);
    	List<Person> personList = getPersonList();
    	getApplication().setProperty(SampleAppNames.PERSON_LIST, personList);
    }

	protected List<Person> getPersonList() throws ParseException
	{
		// ������� ��������� �������� ������� � �����
    	// � ������� �������� ������ ����� ������� �� ��
    	ArrayList<Person> personList = new ArrayList<Person>();
    	add(personList,"����","������",sdf.parse("20-08-1968"));
    	add(personList,"����","������",sdf.parse("20-09-1978"));
    	add(personList,"�����","�������",sdf.parse("20-10-1988"));
		return personList;
	}

	private void add(ArrayList<Person> personList, String firstName, String lastName, Date birthDate)
	{
		Person p = new Person();
		p.setFirstName(firstName);
		p.setLastName(lastName);
		p.setBirthDate(birthDate);
		long age = (new Date().getTime()-birthDate.getTime())/1000/60/60/24/365;
		p.setAge(new Integer((int) age));
		p.setId(""+personList.size());
		personList.add(p);
	}

	protected ProgressBar getProgressBar() throws Exception
	{
		ProgressBar pb = (ProgressBar) getApplication().getProperty("progressBar");
		return pb;
	}

}