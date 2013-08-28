package gryphon.gui.impl;

import javax.swing.*;

import gryphon.*;
import gryphon.gui.*;

public class MyPopupFactory {
  private static GuiApplication app;

  public static JPopupMenu createPopup(Object[] actionKeys)
  {
    JPopupMenu popup = null;
    try {
      popup = new JPopupMenu();
      for (int i=0; i<actionKeys.length; i++) {
        JMenuItem item = (JMenuItem) createAbstractButton(
            JMenuItem.class,
            actionKeys[i]);
        popup.add(item);
      }
    }
    catch (Exception ex) {
      app.handle(ex);
    }
    return popup;
  }

  private static AbstractButton createAbstractButton(Class buttonClass,
                                             Object actionKey) throws Exception
  {
    AbstractButton b = (AbstractButton) buttonClass.newInstance();
    b.setAction((SwingActionController)app.getActionController());
    b.setText( (String) app.getActionContainer().getUserAction(actionKey).getValue(UserAction.NAME));
    b.setActionCommand(actionKey.toString());
    return b;
  }

  public static void init(GuiApplication application) {
    MyPopupFactory.app = application;
  }

}
