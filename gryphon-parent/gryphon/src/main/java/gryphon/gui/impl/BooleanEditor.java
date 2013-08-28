package gryphon.gui.impl;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;

public class BooleanEditor extends DefaultCellEditor 
{
	private static final long serialVersionUID = 1L;

	public BooleanEditor() {
	    super(new JCheckBox());
	    JCheckBox checkBox = (JCheckBox)getComponent();
	    checkBox.setHorizontalAlignment(JCheckBox.CENTER);
	}
}
