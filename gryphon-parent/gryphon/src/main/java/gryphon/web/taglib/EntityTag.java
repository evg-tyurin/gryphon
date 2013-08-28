package gryphon.web.taglib;

import gryphon.Entity;
import gryphon.common.Logger;
import gryphon.web.JspUtil;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;


public class EntityTag extends TagSupport {
  private String key;
  private String attrName;
  public EntityTag() {
  }
  public int doStartTag() throws JspException {
    String text = "";
    try {
      Entity entity = (Entity) JspUtil.getApplication(pageContext).getProperty(getKey());
      text = entity.getAttribute(getAttrName()).toString();
    }
    catch (Exception ex) {
      Logger.logThrowable(ex);
      ex.printStackTrace();
      text = "Error processing entity: "+ex.getClass().getName()+" - "+ex.getMessage();
    }
    try {
      pageContext.getOut().print(text);
    }
    catch (IOException ex1) {
      throw new JspException(ex1);
    }

    return EVAL_BODY_INCLUDE;
  }
  public String getKey() {
    return key;
  }
  public void setKey(String key) {
    this.key = key;
  }
  public String getAttrName() {
    return attrName;
  }
  public void setAttrName(String attrName) {
    this.attrName = attrName;
  }

}