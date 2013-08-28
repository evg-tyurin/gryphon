package gryphon.gui;

import gryphon.Application;
import gryphon.common.ActionController;

import java.awt.Component;

/**
 * 
 * @author Evgueni Tiourine
 */

public interface GuiApplication extends Application
{
    Component getComponent();

    FormContainer getFormContainer() throws Exception;

    public ActionController getActionController();
    /**
     * Set text of status bar of this GUI application.
     * @param text
     * @param b whether this text is important
     */
    void setStatus(String text, boolean isImportant);
}