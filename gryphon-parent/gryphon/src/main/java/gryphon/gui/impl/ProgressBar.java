package gryphon.gui.impl;

import gryphon.common.Logger;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author ET
 */
public class ProgressBar {
	private static final String DEFAULT_TEXT = "Подождите";
	private String title = "Выполняется действие";
	JDialog dialog;
	private JLabel label;
	private JPanel progressBarPane = new JPanel(new BorderLayout());
//    private JProgressBar bar = new JProgressBar();
    private Component parent;
    /** текст на прогресс-баре */
	private String labelText = DEFAULT_TEXT;
    
    public ProgressBar(JApplet applet) 
    {
        this.parent = applet;
        reset();
    }
    public ProgressBar(Component parent) 
    {
        this.parent = parent;
        reset();
    }
	/**
	 * 
	 */
	public void hide() {
		if (dialog==null) return;			
		dialog.setVisible(false);
		reset();
	}
	public void setText(String text) {
		labelText = text;
	    label.setText("<html>"+labelText+"</html>");
	    if (dialog!=null)
	    	dialog.pack();
	}
	public void setValue(int value) {
//	    bar.setValue(value);
	}
	public void show() {
		show(getParent());
	}
	/**
	 * 
	 */
	public void show(Component parentComp) {
		// create dialog window
		if (dialog==null) {
			JOptionPane op = new JOptionPane(progressBarPane, 
			        JOptionPane.INFORMATION_MESSAGE, 
			        JOptionPane.DEFAULT_OPTION, 
			        null, 
			        new Object[0]);
			dialog = op.createDialog(parentComp, title);
			dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			Logger.debug("modal? "+dialog.isModal());
		}
		// force the dialog to be shown
		new Thread(new Runnable() {
			public void run() {
				dialog.setVisible(true);
				
			}
		}).start();
		// wait until dialog is visible
		while (!dialog.isVisible()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}

    protected Component getParent()
    {
    	return parent;
    }
    /**
     * resets progress bar pane
     */
    private void reset()
    {
        progressBarPane.removeAll();
        progressBarPane.setLayout(new BorderLayout());
        label = new JLabel(DEFAULT_TEXT);
        setText(DEFAULT_TEXT);
        progressBarPane.add(label, BorderLayout.CENTER);
//        bar.setMinimum(0);
//        bar.setMaximum(100);
//        bar.setValue(0);
//        progressBarPane.add(bar, BorderLayout.CENTER);           
        
    }
    public Component getComponent()
    {
		return dialog;
    }
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getText()
	{
		return labelText;
	}
}
