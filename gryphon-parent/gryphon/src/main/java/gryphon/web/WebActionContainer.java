package gryphon.web;

import gryphon.UserAction;
import gryphon.common.AbstractActionContainer;

import java.util.Collection;
import java.util.Iterator;

public abstract class WebActionContainer extends AbstractActionContainer {
  public WebActionContainer() {
  }

  public UserAction getUserActionByCommand(String command) {
    Collection<UserAction> coll  = getAll();
    for (Iterator<UserAction> i = coll.iterator(); i.hasNext(); ) {
      WebAction a = (WebAction)i.next();
      if (command.equals(a.getCommand())) {
        return a;
      }
    }
    return null;
  }
}