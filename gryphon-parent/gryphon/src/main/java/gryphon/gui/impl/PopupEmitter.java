package gryphon.gui.impl;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PopupEmitter extends MouseAdapter
{
    private SwingView creator;

    public PopupEmitter(SwingView creator)
    {
        this.creator = creator;
    }

    public void mouseReleased(MouseEvent e)
    {
        if (e.isPopupTrigger())
        {
            creator.createAndShowPopup(e);
        }
    }

}