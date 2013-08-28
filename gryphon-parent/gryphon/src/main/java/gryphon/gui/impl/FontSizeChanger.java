package gryphon.gui.impl;

import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
/**
 * Слушатель колесика мыши, который при зажатом Ctrl меняет размер шрифта компонента-источника события.
 * 
 * @author etyurin
 *
 */
public class FontSizeChanger implements MouseWheelListener{

	public void mouseWheelMoved(MouseWheelEvent e)
	{
		if (!e.isControlDown())
			return;
		if (!(e.getSource() instanceof JComponent))
			return;
		JComponent tree = (JComponent) e.getSource();
		Font font = tree.getFont();
//		Logger.debug("wheel "+e.getWheelRotation()+"; "+font.getName());
		if (e.getWheelRotation()>0)
			tree.setFont(new Font(font.getName(),Font.PLAIN,font.getSize()+1));
		else
			tree.setFont(new Font(font.getName(),Font.PLAIN,font.getSize()-1));		
	}
	
}