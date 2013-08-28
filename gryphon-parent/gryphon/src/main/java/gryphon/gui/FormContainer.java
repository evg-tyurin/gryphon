package gryphon.gui;

import gryphon.Form;
import gryphon.Navigation;

import java.awt.Component;

/**
 * 
 * @author Evgueni Tiourine
 */

public interface FormContainer extends Navigation
{
    GuiApplication getApplication();

    Component getComponent() throws Exception;

    /**
     * ����������� ������������ �� ������ �������� �����.
     * 
     * @param formName
     *            ��� �����
     * @throws java.lang.Exception
     * @deprecated ���� ������������ Application#changeState(String)
     */
    void switchToForm(String formName) throws Exception;

    Form getActiveForm() throws Exception;
}