package gryphon.gui.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

public class DateInputVerifier extends InputVerifier
{
	private String format;
	
	private SimpleDateFormat sdf;

	public DateInputVerifier(String format){
		this.format = format;
		sdf = new SimpleDateFormat(this.format);
	}
	
	public boolean verify(JComponent input) {
		try {
			String text = ((JTextField)input).getText();
			if (text.length()>0)
				sdf.parse(text);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

}
