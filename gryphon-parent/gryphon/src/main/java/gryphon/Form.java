package gryphon;

import gryphon.gui.FormContainer;

/**
 * The interface defines basic functionality of state of the application (screen form).
 * 
 * @author Evgueni Tiourine
 */

public interface Form
{
    void setContainer(FormContainer container);

    FormContainer getContainer();

    void prepare() throws Exception;

    void start() throws Exception;

    void pause() throws Exception;

    void resume() throws Exception;

    View getMainView() throws Exception;

    Object getComponent();
}
