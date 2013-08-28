package gryphon.gui.impl;

public interface TreeValueConverter
{

	String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus);

}
