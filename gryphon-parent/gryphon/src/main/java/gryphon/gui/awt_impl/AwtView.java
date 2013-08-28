package gryphon.gui.awt_impl;

import gryphon.View;

import java.awt.Component;
import java.awt.event.MouseEvent;

/**
 * 
 * @author ET
 */
public interface AwtView extends View
{
    String SHOW_POPUP = "showPopup";

    Component getComponent() throws Exception;

    void createAndShowPopup(MouseEvent e);

}
