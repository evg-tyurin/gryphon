package gryphon.gui;

import gryphon.Form;

public interface Dialog extends Form
{

    String getTitle();
    
    Object[] getOptions();

    Object getDefaultOption();
    Object getCloseOption();    
}
