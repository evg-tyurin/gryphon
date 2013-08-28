package gryphon.gui.impl;

import gryphon.Entity;
import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;
import gryphon.common.Logger;
import gryphon.gui.TreeView;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;

/**
 * Representation of the entity instance using tree nodes and leafes. 
 * @author ET
 */
public class JTreeView extends JPanelView implements TreeView
{
	private static final long serialVersionUID = 1L;

	private JTree tree;
	
	private FontSizeChanger fontSizeChanger = new FontSizeChanger();

    public JTreeView()
    {
    }

    public void updateView() throws Exception
    {
    	// запомним свойства шрифта
		Font font = null;
		if (tree!=null)
			font = tree.getFont();
        removeAll();
        setLayout(new BorderLayout());
        tree = new MyTree(getDescriptor());
        if (getDescriptor().getValue(DescriptorEntry.RENDERER)!=null)
        {
        	String renderer = getDescriptor().getValue(DescriptorEntry.RENDERER);
        	tree.setCellRenderer((TreeCellRenderer) Class.forName(renderer).newInstance());
        }
        if (getDescriptor().getValue(DescriptorEntry.EDITOR)!=null)
        {
        	String editor = getDescriptor().getValue(DescriptorEntry.EDITOR);
        	tree.setCellEditor((TreeCellEditor) Class.forName(editor).newInstance());
        }
        JScrollPane scroll = new JScrollPane(tree);
        add(scroll, BorderLayout.CENTER);
        // восстановим свойства шрифта
		if (font!=null)
			tree.setFont(new Font(font.getName(),font.getStyle(),font.getSize()));
        // показыватель контекстных менюшек, если надо
        if (getDescriptor().getValue(SHOW_POPUP) != null)
        {
            getTree().addMouseListener(popupEmitter);
        }
        // слушатель колесика мыши
        tree.removeMouseWheelListener(fontSizeChanger);
        tree.addMouseWheelListener(fontSizeChanger);
    }

    public Entity getSelectedEntity() throws Exception
    {
        Object o = getTree().getLastSelectedPathComponent();
        return (Entity) o;
    }

    public JTree getTree()
    {
        return tree;
    }
    class MyTree extends JTree
    {
		private static final long serialVersionUID = 1L;
		
		private TreeValueConverter converter;
		
		MyTree(Descriptor d) throws Exception
		{
	        try
			{
				String converterClass = d.getValue(MyTreeModel.CONVERTER);
				if (converterClass!=null)
					this.converter = (TreeValueConverter) Class.forName(converterClass).newInstance();
			}
			catch (Exception e)
			{
				Logger.logThrowable(e);
			}
			setModel(new MyTreeModel(getEntity(), getDescriptor()));
		}
		
		public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
		{
			if (converter==null)
				return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
			return converter.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
		}
		
    }
}
