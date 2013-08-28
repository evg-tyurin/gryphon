package gryphon.web.taglib;

import gryphon.UserAction;
import gryphon.web.JspUtil;
import gryphon.web.WebAction;
import gryphon.web.WebActionContainer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


/**
 * @author ET
 */

public class ButtonTag extends TagSupport {
  private String name;
  private String value;
  private String tooltip;
  private Object action;
  public ButtonTag() {
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getValue() {
    return value;
  }
  public void setValue(String value) {
    this.value = value;
  }
  public String getTooltip() {
    return tooltip;
  }
  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }
  public Object getAction() {
    return action;
  }
  public void setAction(String action) {
    this.action = action;
  }
  public void setAction(Object action) {
    this.action = action;
  }
  public int doStartTag() throws javax.servlet.jsp.JspException {
    try {
      String text = "";
      Object action = getAction();
      if (! (action instanceof String) ||
          ( ( (String) action).length() > 0 &&
           Character.isDigit( ( (String) action).charAt(0)))) {
           // новая реализация кнопки, когда вся инфа берется из экшена
           WebActionContainer actions = (WebActionContainer) JspUtil.
               getApplication(pageContext).getActionContainer();
           WebAction webAction = (WebAction) actions.getUserAction(action);
           String buttonName = webAction.getCommand();
           if (getName() != null && !getName().equals("")) {
             buttonName = getName();
           }
           String buttonValue = (String)webAction.getValue(UserAction.NAME);
           if (getValue() != null && !getValue().equals("")) {
             buttonValue = getValue();
           }
           String title = (String)webAction.getValue(UserAction.SHORT_DESCRIPTION);
           if (getTooltip() != null && !getTooltip().equals("")) {
             title = getTooltip();
           }
           text = "<input type=button "+
               "name='" + buttonName + "' "+
               "value='" + buttonValue + "' ";
           if (title != null) {
             text += "title='" + webAction.getValue(UserAction.SHORT_DESCRIPTION) + "' ";
           }
           text += "onClick=\"submitForm('index?action=" +
               webAction.getCommand() + "');\" >\n";
      }
      else {
        // старая реализация кнопки
        text = "<input type=button name='" + getName() + "' value='" +
            getValue() + "' title='" + getTooltip() +
            "' onClick=\"submitForm('" +
            action.toString() + "');\" >\n";
      }

      pageContext.getOut().println(text);
    }
    catch (Exception ex) {
      throw new JspException(ex);
    }

    return EVAL_BODY_INCLUDE;
  }

}