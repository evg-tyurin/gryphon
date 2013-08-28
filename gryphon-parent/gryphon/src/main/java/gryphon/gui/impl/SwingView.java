package gryphon.gui.impl;

import java.awt.event.*;
import javax.swing.*;

import gryphon.*;
/**
 * 
 * @author ET
 */
public interface SwingView extends View
{
    String SHOW_POPUP = "showPopup";

    JComponent getComponent() throws Exception;

    void createAndShowPopup(MouseEvent e);
}