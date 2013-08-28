package gryphon.gui.impl;

import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Представление атрибутов объекта в виде двухстолбцовой ячеистой структуры (grid).
 * Левый столбик занимают текстовые метки с названием/описанием атрибута,
 * а правый - значения атрибутов. Значения могут быть отображены в видетекстовых полей,
 * чек-боксов или радио-кнопок.
 * 
 * @author ET
 *
 */
public class GridView extends JPanelView 
{

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";
	
	private JComponent[] values;

	private boolean inited = false;
	
	public void updateEntity() throws Exception
	{
		Descriptor d = getDescriptor();
		for (int i=0; i<d.size(); i++)
		{
			DescriptorEntry e = d.getEntry(i);
			getEntity().setAttribute(e.getAttribute(),cast(getValue(i),i));
		}
	}
	private Object getValue(int i) throws Exception
	{
		DescriptorEntry e = getDescriptor().getEntry(i);		
    	if (e.getType()==DescriptorEntry.TYPE_CHECKBOX)
    		return new Boolean(((JCheckBox)values[i]).isSelected());
    	if (e.getType()==DescriptorEntry.TYPE_RADIO)
    		return new Boolean(((JRadioButton)values[i]).isSelected());
    	//DescriptorEntry.TYPE_TEXT   
    	return ((JTextField)values[i]).getText();
	}
	/**
	 * Возвращает значение правильного типа в зависимости от типа целевого атрибута.
	 * @param value
	 * @param attrIndex порядковый индекс целевого атрибута
	 * @return
	 */
	private Object cast(Object value, int attrIndex) throws Exception
	{
		Class type = attrTypes[attrIndex];
		if (Number.class.isAssignableFrom(type)){
			if (String.valueOf(value).equals(""))
				return null;
			Method valueOf = type.getMethod("valueOf", new Class[]{String.class});
			return valueOf.invoke(null, new Object[]{value});
		}
		if (Date.class.equals(type)){
			if (String.valueOf(value).equals(""))
				return null;
			String format = nvl((String) getDescriptor().getEntry(attrIndex).getValue("format"),DEFAULT_DATE_FORMAT);
			return new SimpleDateFormat(format).parse((String) value);
		}
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
			Object value = getEntity().getAttribute(e.getAttribute());
        	if (e.getType()==DescriptorEntry.TYPE_CHECKBOX)
        		((JCheckBox)values[i]).setSelected(value!=null?((Boolean)value).booleanValue():false);
        	else if (e.getType()==DescriptorEntry.TYPE_RADIO)
        		((JRadioButton)values[i]).setSelected(value!=null?((Boolean)value).booleanValue():false);
        	else{//DescriptorEntry.TYPE_TEXT   
        		JTextField textField = (JTextField)values[i];
        		String text = "";
        		if (value!=null){
        			if (value instanceof Date){
        				String format = nvl((String) e.getValue("format"),DEFAULT_DATE_FORMAT);
        				text = new SimpleDateFormat(format).format((Date)value);
            			textField.setInputVerifier(new DateInputVerifier(format));
            			textField.setToolTipText(format);
        			}
        			else{
        				text = String.valueOf(value);        				
        			}
        		}
				textField.setText(text);
        	}
		}
	}

	public void init() throws Exception
	{
		removeAll();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0,5,0,5);
//        setFont(new Font("Helvetica", Font.PLAIN, 14));
        setLayout(gridbag);
        
        Descriptor d = getDescriptor();
        values = new JComponent[d.size()];
        for (int i=0; i<d.size(); i++)
        {
        	DescriptorEntry e = d.getEntry(i);

        	c.gridwidth = 2;
        	
        	c.fill = GridBagConstraints.BOTH;
            c.weightx = 1.0;
    		JLabel l = new JLabel(e.getLabel());
    		gridbag.setConstraints(l, c);
    		add(l);
//            makebutton("Button2", gridbag, c);
//            makebutton("Button3", gridbag, c);

        	c.gridwidth = GridBagConstraints.REMAINDER; //end row
        	if (e.getType()==DescriptorEntry.TYPE_CHECKBOX)
        		values[i] = new JCheckBox();
        	else if (e.getType()==DescriptorEntry.TYPE_RADIO)
        		values[i] = new JRadioButton();
        	else//DescriptorEntry.TYPE_TEXT   
        		values[i] = new JTextField();
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
	public String nvl(String str, String defValue){
		return str!=null?str:defValue;
	}
	

}
