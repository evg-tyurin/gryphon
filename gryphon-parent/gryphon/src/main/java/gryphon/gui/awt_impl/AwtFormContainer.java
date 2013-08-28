package gryphon.gui.awt_impl;

import gryphon.Form;
import gryphon.common.FriendlyException;
import gryphon.common.Logger;
import gryphon.gui.FormContainer;
import gryphon.gui.GuiApplication;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Panel;
import java.util.HashMap;

//TODO refactor: all implementation of the class is copied from SwingFormContainer (except base class)
public class AwtFormContainer extends Panel implements FormContainer
{
    /**
     * �������� ����������� ���������� �������.
     */
    private GuiApplication application;

    /**
     * ��������� ���-��� ����������.
     */
    private CardLayout cardLayout = new CardLayout();

    /**
     * �����, � �������� �������� ���������.
     */
    private HashMap forms = new HashMap();

    private Form activeForm;

    private Form previousForm;

    /**
     * �����������. ������������� ��������� ���-���.
     * 
     * @param application
     *            �������� ����������� ���������� �������
     */
    public AwtFormContainer(GuiApplication application)
    {
        this.application = application;
        setLayout(cardLayout);
    }

    public GuiApplication getApplication()
    {
        return this.application;
    }

    public void switchToForm(String formName) throws Exception
    {
        Logger.log("goto: " + formName);
        // initialize new form if needed
        String nextFormName = formName;
        if (!forms.containsKey(nextFormName))
        {
            try
            { // to create & prepare new form
                Form newForm = instantiateForm(nextFormName);
                newForm.setContainer(this);
                newForm.prepare();
                forms.put(nextFormName, newForm);
                // ��������, ����� ����������� �������� ���� �����.
                // ��� ����� ���� ���������� �����, ������� � �.�. ��� ���-��.
                add(getComponent(newForm), nextFormName);
                cardLayout.addLayoutComponent(getComponent(newForm),
                        nextFormName);
            }
            catch (Exception e)
            {
                throw new FriendlyException(
                        "�� ������� ���������������� �������� �����", e);
            }
        }
        Form form = (Form) forms.get(nextFormName);

        // hide current form or dialog if any
        if (getActiveForm() != null)
        {
            getActiveForm().pause();
            getComponent(getActiveForm()).setVisible(false);
        }
        else
        {
            previousForm = null;
        }

        if (form instanceof Form)
        {
            // save the state to the History object
            // getHistory().addState(
            // getClass().getName(),
            // nextFormName);

            try
            {
                form.resume();
                getComponent(form).setVisible(true);
                cardLayout.show(this, nextFormName);
                previousForm = activeForm;
                activeForm = form;
            }
            catch (Exception ex)
            {
                // ���������� �����
                if (getActiveForm() != null)
                {
                    getComponent(getActiveForm()).setVisible(true);
                }
                // �������� �������
                throw new FriendlyException("�� ������� ����������� �������� �����",
                        ex);
            }
        }
        ((FrameApp)getApplication()).validate();
    }// switchToForm(formName);
    protected Component getComponent(Form form)
    {
        return (Component) form.getComponent();
    }
    public Form getActiveForm() throws Exception
    {
        return activeForm;
    }

    private Form instantiateForm(String formName) throws Exception
    {
        Form form = (Form) Class.forName(formName).newInstance();
        // ... some initialization steps
        return form;
    }

    public Component getComponent() throws Exception
    {
        return this;
    }

}