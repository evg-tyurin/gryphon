package gryphon.web.taglib;

import gryphon.Application;
import gryphon.Entity;
import gryphon.common.ListView;
import gryphon.common.Logger;
import gryphon.web.JspUtil;
import gryphon.web.WebView;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author ET
 */

public class FormTag
    extends TagSupport {
  private String title;
  private String north;
  private String northEntity;
  private String formAttr;
  private String west;
  private String westEntity;
  private String east;
  private String eastEntity;
  public FormTag() {
  }

  /**
   * Напечатать HTML-заголовок и приветствие.
   * @return
   * @throws JspException
   */
  public int doStartTag() throws JspException {
    try {
      String text = "<html>" +
          "<head>" +
//          "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=windows-1251\">"+
          "<title>" + getTitle() + "</title>" +
          "<script language=Javascript src='js/buttons.js'></script>" +
          "</head>\n" +
          "<body bgcolor='#ffffff'>\n";
      // после этого идет обработка кнопок
      //...
      pageContext.getOut().println(text);
//      pageContext.getResponse().getOutputStream().write(text.getBytes());
      return EVAL_BODY_INCLUDE;
    }
    catch (IOException ex) {
      throw new JspException(ex);
    }
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getNorth() {
    return north;
  }

  public void setNorth(String north) {
    this.north = north;
  }

  /**
   * Напечатать стандартное окончание - навигационное меню.
   * Реализация взята из <ss18:page param="bottomMenu">
   * @return
   * @throws javax.servlet.jsp.JspException
   */
  public int doEndTag() throws JspException {
    String text = "";
    // форма, через которую будут отправляться данные
    text += "<form name='myform' action='' method=POST>"+
        "<input type=hidden name=oid value='-1'>\n";
    // предопределенные атрибуты HTML-формы
    if (getFormAttr() != null) {
      StringTokenizer st = new StringTokenizer(getFormAttr(), ";");
      while (st.hasMoreTokens()) {
        String pair = st.nextToken();
        if (pair.length() > 0) {
          String name = pair.substring(0, pair.indexOf('='));
          String value = pair.substring(pair.indexOf('='));
          text += "<input type='hidden' name='" + name + "' value='" + value +
              "'>\n";
        }
      }
    }
    text += "</form>\n";
    // обработка вьюшек
    try {
      // NORTH
      String northText = "";
      if (getNorth() != null && !getNorth().equals("")) {
        northText =  getViewText(getNorth(), getNorthEntity());
      }
      String eastText = "";
      if (getEast() != null && !getEast().equals("")) {
        eastText = getViewText(getEast(), getEastEntity());
      }
      String westText = "";
      if (getWest() != null && !getWest().equals("")) {
        westText = getViewText(getWest(), getWestEntity());
      }
      String centerText = "";
      String southText = "";
      // сделаем подобие бордер-лэйаута
      text += "<table>"+
          "<tr><td colspan=3>"+northText+"</td></tr>"+
          "<tr><td>"+westText+"</td><td>"+centerText+"</td><td>"+eastText+"</td></tr>"+
          "<tr><td colspan=3>"+southText+"</td></tr>"+
          "</table>";
    }
    catch (Exception ex1) {
      Logger.logThrowable(ex1);
      ex1.printStackTrace();
      text += "Error processing component: " + ex1.getClass().getName() + " - " +
          ex1.getMessage();
    }
    try {
      text += "</body></html>";
      pageContext.getOut().write(text);
//      pageContext.getResponse().getOutputStream().write(text.getBytes());
      return EVAL_PAGE;
    }
    catch (IOException ex) {
      throw new JspException(ex);
    }
  }

  public String getNorthEntity() {
    return northEntity;
  }

  public void setNorthEntity(String northEntity) {
    this.northEntity = northEntity;
  }
  public String getFormAttr()
  {
    return formAttr;
  }

  private String getViewText(String viewWebName, String viewEntity) throws Exception {
    WebView view = (WebView) Class.forName(viewWebName).newInstance();
    view.init();
//    String viewEntity = getNorthEntity();
    Application app = JspUtil.getApplication(pageContext);
    Object entity = app.getContext().getProperty(viewEntity);
    if (view instanceof ListView) {
      ( (ListView) view).setList( (List) entity);
    }
    else {
      view.setEntity( (Entity) entity);
    }
    view.updateView();
    return view.getText();
  }

  public void setFormAttr(String formAttr)
  {
    this.formAttr = formAttr;
  }
  public String getWest() {
    return west;
  }
  public void setWest(String west) {
    this.west = west;
  }
  public String getWestEntity() {
    return westEntity;
  }
  public void setWestEntity(String westEntity) {
    this.westEntity = westEntity;
  }
  public String getEast() {
    return east;
  }
  public void setEast(String east) {
    this.east = east;
  }
  public String getEastEntity() {
    return eastEntity;
  }
  public void setEastEntity(String eastEntity) {
    this.eastEntity = eastEntity;
  }
}
