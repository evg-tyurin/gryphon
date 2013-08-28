package gryphon.gui.awt_impl;

import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.TextField;

/**
 * Представление атрибутов объекта в виде двухстолбцовой ячеистой структуры (grid).
 * Левый столбик занимают текстовые метки с названием/описанием атрибута,
 * а правый - значения атрибутов.
 * @author ET
 *
 */
public class GridView extends PanelView 
{

	private static final long serialVersionUID = 1L;
	
	private TextField[] values;

	private boolean inited = false;

	public void updateEntity() throws Exception
	{
		Descriptor d = getDescriptor();
		for (int i=0; i<d.size(); i++)
		{
			DescriptorEntry e = d.getEntry(i);
			Object oldValue = getEntity().getAttribute(e.getAttribute());
			getEntity().setAttribute(e.getAttribute(),cast(values[i].getText(),oldValue));
		}
	}
	/**
	 * Возвращает значение правильного типа в зависимости от типа второго параметра.
	 * @param value
	 * @param oldValue
	 * @return
	 */
	private Object cast(String value, Object oldValue)
	{
		if (oldValue instanceof Integer)
			return Integer.valueOf(value);
		if (oldValue instanceof Double)
			return Double.valueOf(value);
		return value;
	}

	public void updateView() throws Exception
	{
		if (!inited)
			init();
		if (getEntity()==null)
			return;
		Descriptor d = getDescriptor();
		for (int i=0; i<d.size(); i++)
		{
			DescriptorEntry e = d.getEntry(i);
			values[i].setText(getEntity().getAttribute(e.getAttribute()).toString());
		}
	}

	public void init() throws Exception
	{
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,5,0,5);
//        setFont(new Font("Helvetica", Font.PLAIN, 14));
        setLayout(gridbag);
        
        Descriptor d = getDescriptor();
        values = new TextField[d.size()];
        for (int i=0; i<d.size(); i++)
        {
        	DescriptorEntry e = d.getEntry(i);

        	c.gridwidth = 2;
        	
        	c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0;
    		Label l = new Label(e.getLabel());
    		gridbag.setConstraints(l, c);
    		add(l);
//            makebutton("Button2", gridbag, c);
//            makebutton("Button3", gridbag, c);

        	   c.gridwidth = GridBagConstraints.REMAINDER; //end row
       		values[i] = new TextField();
    		gridbag.setConstraints(values[i], c);
    		add(values[i]);
        }
        inited=true;
//        c.weightx = 0.0;		   //reset to the default
//        makebutton("Button5", gridbag, c); //another row
//
//	   c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last in row
//        makebutton("Button6", gridbag, c);
//
//	   c.gridwidth = GridBagConstraints.REMAINDER; //end row
//        makebutton("Button7", gridbag, c);

//	   c.gridwidth = 1;	   	   //reset to the default
//	   c.gridheight = 2;
//        c.weighty = 1.0;
//        makebutton("Button8", gridbag, c);
//
//        c.weighty = 0.0;		   //reset to the default
//	   c.gridwidth = GridBagConstraints.REMAINDER; //end row
//	   c.gridheight = 1;		   //reset to the default
//        makebutton("Button9", gridbag, c);
//        makebutton("Button10", gridbag, c);
//
//        setSize(300, 100);

	}
	
	

}
