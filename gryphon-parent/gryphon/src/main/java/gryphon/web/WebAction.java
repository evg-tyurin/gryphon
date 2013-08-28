package gryphon.web;

import gryphon.*;
import gryphon.common.*;

public abstract class WebAction extends UserAction {
  private String command;

  public WebAction() {
  }
  public WebAction(String name) {
    super(name);
  }
  public String getCommand() {
    return command;
  }
  public void setCommand(String command) {
    this.command = command;
  }

  protected String getParameter(GryphonEvent e, String name) {
    GryphonWebEvent webEvent = (GryphonWebEvent)e;
    return webEvent.getRequest().getParameter(name);
  }

}