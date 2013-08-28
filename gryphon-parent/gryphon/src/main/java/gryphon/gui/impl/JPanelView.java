package gryphon.gui.impl;

import gryphon.Entity;
import gryphon.UserAction;
import gryphon.common.DefaultDescriptor;
import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;
import gryphon.common.Logger;
import gryphon.gui.GuiApplication;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Date;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * @author Evgueni Tiourine
 */

public class JPanelView extends JPanel implements SwingView
{
	private static final long serialVersionUID = 1L;

	private Entity entity;

    private Descriptor descriptor = new DefaultDescriptor();

    protected PopupEmitter popupEmitter;
    
    protected GuiApplication application;

    /** По количеству записей descriptorEntry */ 
    protected Class[] attrTypes;

    public JPanelView()
    {
        popupEmitter = new PopupEmitter(this);
    }

    public void setEntity(Entity entity) throws Exception
    {
        this.entity = entity;
        attrTypes = null;
    	ensureEntityInfo();
    }

    public Entity getEntity() throws Exception
    {
        return this.entity;
    }

    public void updateView() throws Exception
    {//
    }

    public void updateEntity() throws Exception
    {//
    }
    /** Собрать информацию о типах атрибутов */
    protected void ensureEntityInfo() throws Exception {
    	if (attrTypes==null){
			Descriptor d = getDescriptor();
    		attrTypes = new Class[d.size()];
			for (int i=0; i<d.size(); i++)
			{
				DescriptorEntry e = d.getEntry(i);
				int type = e.getType();
				switch (type)
				{
					case DescriptorEntry.TYPE_CHECKBOX:
						attrTypes[i] = Boolean.class;
						break;
					case DescriptorEntry.TYPE_RADIO:
						attrTypes[i] = Boolean.class;
						break;
					case DescriptorEntry.TYPE_SHORT:
						attrTypes[i] = Short.class;
						break;
					case DescriptorEntry.TYPE_INTEGER:
						attrTypes[i] = Integer.class;
						break;
					case DescriptorEntry.TYPE_LONG:
						attrTypes[i] = Long.class;
						break;
					case DescriptorEntry.TYPE_DATE:
						attrTypes[i] = Date.class;
						break;
					default://TYPE_TEXT
						try
						{// по умолчанию пытаемся прочитать тип атрибута из рефлексии
							Field f = getEntity().getClass().getDeclaredField(toLowerCase1(e.getAttribute()));
							attrTypes[i] = f.getType();
						}
						catch (Exception e1)
						{
							Logger.err(e1.getClass().getName()+": "+e1.getMessage());
							attrTypes[i] = String.class;
						}
						break;
				}
			}
    	}
	}

	protected String toLowerCase1(String str) {
		char[] ch = str.toCharArray();
		ch[0]=Character.toLowerCase(ch[0]);
		return new String(ch);
	}

	public Descriptor getDescriptor()
    {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    public JComponent getComponent() throws java.lang.Exception
    {
        return this;
    }

    /**
     * Базовая реализация ничего не делает,
     * 
     * @throws java.lang.Exception
     */
    public void init() throws Exception
    {//
    }

    /**
     * Должен быть переопределен теми потомками, которые хотят показывать
     * контекстные менюшки.
     * 
     * @param e
     */
    public void createAndShowPopup(MouseEvent e)
    {
    	if (!(e.getSource() instanceof JComponent))
    		return;
		JComponent comp = (JComponent) e.getSource();
		if (application==null){
			DefaultForm form = (DefaultForm) getParent(comp,DefaultForm.class);
			if (form==null){
				Logger.debug("DefaultForm not found among parents of view "+getClass().getName());
				return;
			}
			application = form.getContainer().getApplication();
		}
		try
		{
			JPopupMenu popup = createPopupMenu(e);
			if (popup!=null)
				popup.show(this,e.getX(), e.getY());
		}
		catch (Exception e1)
		{
			if (application!=null)
				application.handle(e1);
			else
				JOptionPane.showMessageDialog(this, e1.getClass().getName()+": "+e1.getMessage());
		}
    }
    /** 
     * Переопределяется потомками, которые хотят показывать контекстные меню. 
     * Типичный сценарий:<br>
     * <code>
     * JPopupMenu p = new JPopupMenu();<br>
     * createPopupItem(p, MyProjectActions.MY_ACTION);<br>
     * return p;<br>
     * </code> 
     * @param e событие мыши, которое привело к вызову метода
     */
	protected JPopupMenu createPopupMenu(MouseEvent e) throws Exception
	{
		return null;
	}

	protected JComponent getParent(JComponent comp, Class parentClass)
	{
		Container p = comp.getParent();
		while(p!=null){
			if (parentClass.isInstance(p))
				return (JComponent) p;
			p = p.getParent();
		}
		return null;
	}
	
	protected void createPopupItem(JPopupMenu popup, Object actionKey) throws Exception
	{
        JMenuItem item = new JMenuItem((SwingActionController)application.getActionController());
        item.setActionCommand(actionKey.toString());
        item.setText((String) application.getActionContainer().getUserAction(actionKey).getValue(UserAction.NAME));
		popup.add(item);
	}

}