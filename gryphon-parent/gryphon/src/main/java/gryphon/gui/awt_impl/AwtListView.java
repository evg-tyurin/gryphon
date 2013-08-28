package gryphon.gui.awt_impl;

import gryphon.Entity;
import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;
import gryphon.common.ListView;
import gryphon.common.Logger;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.MouseEvent;

/**
 * 
 * @author ET
 */
public class AwtListView extends Panel implements ListView, AwtView
{
    private Entity entity;
    private Descriptor descriptor;
    private java.util.List entityList;
    private List awtList = new List();
    
    public AwtListView()
    {
        setLayout(new BorderLayout());
        add(awtList, BorderLayout.CENTER);
    }
    /* (non-Javadoc)
     * @see gryphon.View#setEntity(gryphon.Entity)
     */
    public void setEntity(Entity entity) throws Exception
    {
        this.entity = entity;
    }

    /* (non-Javadoc)
     * @see gryphon.View#getEntity()
     */
    public Entity getEntity() throws Exception
    {
        return this.entity;
    }

    /* (non-Javadoc)
     * @see gryphon.View#updateView()
     */
    public void updateView() throws Exception
    {
        getAwtList().removeAll();
        DescriptorEntry entry = getDescriptor().getEntry(0);
        for(int i=0; i<getList().size(); i++)
        {
            Entity ent = (Entity) getList().get(i);
            awtList.add((String) ent.getAttribute(entry.getAttribute()));
        }
    }

    /* (non-Javadoc)
     * @see gryphon.View#updateEntity()
     */
    public void updateEntity() throws Exception
    {
        
    }

    /* (non-Javadoc)
     * @see gryphon.View#getDescriptor()
     */
    public Descriptor getDescriptor() throws Exception
    {
        return this.descriptor;
    }

    /* (non-Javadoc)
     * @see gryphon.View#setDescriptor(gryphon.common.Descriptor)
     */
    public void setDescriptor(Descriptor descriptor) throws Exception
    {
        this.descriptor = descriptor;
    }

    /* (non-Javadoc)
     * @see gryphon.View#init()
     */
    public void init() throws Exception
    {
    }

    /* (non-Javadoc)
     * @see gryphon.common.ListView#getList()
     */
    public java.util.List getList() throws Exception
    {
        return this.entityList;
    }

    /* (non-Javadoc)
     * @see gryphon.common.ListView#setList(java.util.List)
     */
    public void setList(java.util.List list) throws Exception
    {
        this.entityList = list;
    }

    /* (non-Javadoc)
     * @see gryphon.common.ListView#getSelectedEntity()
     */
    public Entity getSelectedEntity() throws Exception
    {
        int index = awtList.getSelectedIndex();
        if (index >= 0) {
          return (Entity)getList().get(index);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see gryphon.common.ListView#getSelection()
     */
    public Entity[] getSelection() throws Exception
    {
        int[] indices = awtList.getSelectedIndexes();
        Entity[] selection = new Entity[indices.length];
        for (int i= 0; i<indices.length; i++) {
          selection[i] = (Entity)getList().get(indices[i]);
        }
        return selection;
    }

    /* (non-Javadoc)
     * @see gryphon.gui.SwingView#getComponent()
     */
    public Component getComponent() throws Exception
    {
        return this;
    }

    /* (non-Javadoc)
     * @see gryphon.gui.SwingView#createAndShowPopup(java.awt.event.MouseEvent)
     */
    public void createAndShowPopup(MouseEvent e)
    {
        Logger.debug("createAndShowPopup() not yet implemented for AWT");
    }
    public List getAwtList()
    {
        return awtList;
    }
}
