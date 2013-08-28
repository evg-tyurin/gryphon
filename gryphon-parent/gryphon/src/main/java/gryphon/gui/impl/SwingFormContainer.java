package gryphon.gui.impl;

import gryphon.Form;
import gryphon.common.FriendlyException;
import gryphon.common.Logger;
import gryphon.gui.Dialog;
import gryphon.gui.FormContainer;
import gryphon.gui.GuiApplication;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * 
 * @author Evgueni Tiourine
 */

public class SwingFormContainer extends JPanel implements FormContainer
{
	private static final long serialVersionUID = 1L;

	protected static final Integer DLG_VALUE_WHEN_ESCAPED = new Integer(-1);

    /**
     * Основное графическое приложение системы.
     */
    private GuiApplication application;

    /**
     * Карточный лэй-аут контейнера.
     */
    private CardLayout cardLayout = new CardLayout();

    /**
     * Формы, с которыми работает контейнер.
     */
    private HashMap forms = new HashMap();

    private Form activeForm;

    private Form previousForm;

    private JDialog activeDialog;
    /**
     * Stack of modal dialogs currently shown on the screen.
     */
    private Stack dialogs = new Stack();

    /**
     * Конструктор. Устанавливает карточный лей-аут.
     * 
     * @param application
     *            Основное графическое приложение системы
     */
    public SwingFormContainer(GuiApplication application)
    {
        this.application = application;
        setLayout(cardLayout);
    }

    public GuiApplication getApplication()
    {
        return this.application;
    }

    public void switchToForm(String formName) throws Exception
    {
        if (formName.equals(CLOSE_DIALOG))
        {
            activeDialog.setVisible(false);
            activeDialog.dispose();
            dialogs.pop();
            if (dialogs.size()>0)
            {
                Object[] dialogForm = (Object[]) dialogs.lastElement();
                activeDialog = (JDialog) dialogForm[0];
                activeForm = (Form) dialogForm[1];
            }
            else
            {
                activeForm = previousForm;
                previousForm = null;
            }
            activeForm.resume();
            if (!(activeForm instanceof Dialog))
                cardLayout.show(this, activeForm.getClass().getName());
            return;
        }
        Logger.log("goto: " + formName);
        // initialize new form if needed
        String nextFormName = formName;
        if (!forms.containsKey(nextFormName))
        {
            try
            { // to create & prepare new form
                Logger.debug("create "+nextFormName);
                Form newForm = instantiateForm(nextFormName);
                newForm.setContainer(this);
                newForm.prepare();
                forms.put(nextFormName, newForm);
                // возможно, потом понадобятся проверки типа формы.
                // ибо могут быть нормальные формы, диалоги и м.б. еще что-то.
                add(getComponent(newForm), nextFormName);
                if (!(newForm instanceof Dialog))
                    cardLayout.addLayoutComponent(getComponent(newForm),nextFormName);
            }
            catch (Exception e)
            {
                throw new FriendlyException(
                        "Не удается инициализировать экранную форму", e);
            }
        }
        final Form form = (Form) forms.get(nextFormName);

        // hide current form or dialog if any
        if (getActiveForm() != null)
        {
            if (getActiveForm() instanceof Dialog)
            {
                // если на подходе диалог, то предыдущий диалог не прятать
                // а если на подходе форма, то прятать
                if (!(form instanceof Dialog))
                {
                    activeDialog.setVisible(false);
                    activeDialog.dispose();
                    dialogs.pop();
                }
            }
            else
            {
                // если на подходе диалог, то предыдущую форму не прятать
                // а если на подходе форма, то прятать
                if (!(form instanceof Dialog))
                {
                    getActiveForm().pause();
//                    Logger.debug("setVisible false 2");
//                    getComponent(getActiveForm()).setVisible(false);
                }
            }
        }
        else
        {
            previousForm = null;
        }

        if (form instanceof Dialog)
        {
            final Dialog dialog = (Dialog) form;
            // save the state to the History object
            // getHistory().addState(
            // getClass().getName(),
            // nextFormName);

            try
            {
                form.resume();
                Logger.debug("show dialog");
//                Logger.debug("activeForm="+activeForm.getClass().getName());
                // form.getComponent().setVisible(true);
                // cardLayout.show(this, nextFormName);
                
                // если текущая форма - диалог, то не менять предыдущую
                if (!(activeForm instanceof Dialog))
                    previousForm = activeForm;
                activeForm = form;
                revalidate();
                repaint();

                new Thread(new Runnable(){
                    public void run()
                    {
                        try
                        {
                            activeDialog = new JDialog((Frame) getApplication(),
                                    dialog.getTitle(), true);
                            activeDialog.getContentPane().setLayout(new BorderLayout());
                            activeDialog.getContentPane().add(getComponent(dialog),BorderLayout.CENTER);
                            getComponent(form).setVisible(true);
                            activeDialog.pack();
                            activeDialog.setLocationRelativeTo((Frame) getApplication());
                            activeDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                            if (dialog.getDefaultOption() instanceof JButton)
                                activeDialog.getRootPane().setDefaultButton((JButton) dialog.getDefaultOption());
                            activeDialog.addWindowListener(new WindowAdapter() {
                                public void windowClosing(WindowEvent e) {
                                    JButton closeBtn = (JButton)dialog.getCloseOption();
                                    if (closeBtn != null)
                                        closeBtn.doClick();
                                }
                            });
                            dialogs.push(new Object[]{activeDialog,form});
                            activeDialog.setVisible(true);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Logger.debug("dialog is shown");
            }
            catch (Exception ex)
            {
                // откатиться назад
                if (getActiveForm() != null)
                {
                    if (getActiveForm() instanceof Dialog)
                    {
                        
                    }
                    else
                    {
                        cardLayout.show(this, getActiveForm().getClass().getName());
                    }
                }
                // выкинуть иксепшн
                throw new FriendlyException("Не удается переключить экранную форму",
                        ex);
            }
        }
        else // if (form instanceof Form)
        {
            // save the state to the History object
            // getHistory().addState(
            // getClass().getName(),
            // nextFormName);

            try
            {
                form.resume();
//                form.getComponent().setVisible(true);
                cardLayout.show(this, nextFormName);
                previousForm = activeForm;
                activeForm = form;
            }
            catch (Exception ex)
            {
                // откатиться назад
                if (getActiveForm() != null)
                {
                    cardLayout.show(this, getActiveForm().getClass().getName());
                }
                // выкинуть иксепшн
                throw new FriendlyException("Не удается переключить экранную форму",
                        ex);
            }
        }
    }// switchToForm(formName);

    protected Component getComponent(Form form)
    {
        return (Component) form.getComponent();
    }

    public Form getActiveForm() throws Exception
    {
        return activeForm;
    }

    private Form instantiateForm(String formName) throws Exception
    {
        Form form = (Form) Class.forName(formName).newInstance();
        // ... some initialization steps
        return form;
    }

    public Component getComponent() throws Exception
    {
        return this;
    }

}