package gryphon.gui.impl;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import gryphon.gui.Dialog;

public abstract class DefaultDialog extends DefaultForm implements Dialog
{

    public DefaultDialog()
    {
        // tool bar shoud be rendered a the bottom 
        // and tool bar buttons should be centered 
        getContentArea().remove(getToolBar());
        getContentArea().add(getToolBar(), BorderLayout.AFTER_LAST_LINE);
        getToolBar().setLayout(new FlowLayout());
    }

}
