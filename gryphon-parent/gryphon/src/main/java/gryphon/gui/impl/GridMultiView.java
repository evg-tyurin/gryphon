package gryphon.gui.impl;

import gryphon.Entity;
import gryphon.View;

import java.awt.Component;
import java.awt.GridLayout;

/**
 * 
 * @author ET
 */
public class GridMultiView extends JPanelView
{
	private static final long serialVersionUID = 1L;

	private View[] views;

    public GridMultiView(int viewCount)
    {
        views = new View[viewCount];
        setLayout(new GridLayout(1, viewCount));
        // setBorder();
    }

    public void updateView() throws java.lang.Exception
    {
        for (int i = 0; i < views.length; i++)
        {
            views[i].updateView();
        }
    }

    public void updateEntity() throws java.lang.Exception
    {
        for (int i = 0; i < views.length; i++)
        {
            views[i].updateEntity();
        }
    }

    protected void addImpl(Component comp, Object constraints, int index)
    {
        views[getComponentCount()] = (View) comp;
        super.addImpl(comp, constraints, index);
    }

    public void setEntity(Entity entity) throws java.lang.Exception
    {
        for (int i = 0; i < views.length; i++)
        {
            views[i].setEntity(entity);
        }
    }

}