package gryphon.gui.impl;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BooleanRenderer extends JCheckBox implements TableCellRenderer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BooleanRenderer()
	{
		super();
		setHorizontalAlignment(JLabel.CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (isSelected)
		{
			setForeground(table.getSelectionForeground());
			super.setBackground(table.getSelectionBackground());
		}
		else
		{
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		setSelected((value != null && ((Boolean) value).booleanValue()));
		return this;
	}
}
