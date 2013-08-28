package gryphon.samples.swing;

import gryphon.UserAction;
import gryphon.common.GryphonEvent;
/**
 * Действие пользователя для создания нового контакта.
 * Создать и подготовить новый экземпляр класса {@link Person} и сохранить его в сессии 
 * для последующего использования объектом-формой (в данном приложении это {@link EditPersonDialog}).
 * @author ET
 */
public class CreatePersonAction extends UserAction
{
	public CreatePersonAction(){
		super("Добавить контакт");
        setNextForm(EditPersonDialog.class.getName());
	}
    public void doAction(GryphonEvent event) throws java.lang.Exception
    {
    	Person p = new Person();
    	getApplication().setProperty(SampleAppNames.PERSON, p);
    }

}