package gryphon.web;

import gryphon.Application;
import gryphon.Form;
import gryphon.common.ActionContainer;
import gryphon.common.Context;
import gryphon.common.DefaultContext;
import gryphon.common.DefaultDescriptor;
import gryphon.common.Descriptor;
import gryphon.common.GryphonNames;
import gryphon.common.Logger;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GryphonWebApp extends HttpServlet implements WebApplication {
  private static final String CONTENT_TYPE = "text/html; charset=windows-1251";

  private Context context;

  private ActionContainer actionContainer;

  private Descriptor descriptor = new DefaultDescriptor();
  //Initialize global variables
  public void init() throws ServletException {
      Logger.debug("set Gryphon app = "+this.getClass().getName());
    getServletContext().setAttribute(GryphonNames.APPLICATION_NAME, this);
    context = new DefaultContext();
    // create action container
    try {
      actionContainer = (ActionContainer) Class.forName(getDescriptor().getValue(Application.ACTION_CONTAINER)).newInstance();
      actionContainer.setApplication(this);
      actionContainer.init();
    }
    catch (Exception ex) {
      throw new ServletException("Can't init WebApp", ex);
    }
  }
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }
  /**
   * Выполняет роль контроллера операций.
   * @param request HTTP-запрос пользователя
   * @param response HTTP-ответ сервера
   * @throws ServletException
   * @throws IOException
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.getOutputStream().println("<html><h1>Gryphon web app</h1></html>");
  }
  public void destroy() {
  }
  public WebApplication cloneMe() throws Exception {
    return (WebApplication)clone();
  }
  public void start() throws Exception {
  }
  public void handle(Exception ex) {
    try {
      getContext().setProperty(GryphonNames.EXCEPTION, ex);
      getContext().setProperty(GryphonNames.NEXT_STATE, "/"+GryphonNames.EXCEPTION);
    }
    catch (Exception ex1) {
      Logger.logThrowable(ex);
      Logger.logThrowable(ex1);
    }
  }
  public Context getContext() throws Exception {
    return this.context;
  }
  /**
   * Запоминает название формы, чтобы потом перейти.
   * @param stateName название формы, куда нужно перейти
   * @throws java.lang.Exception
   */
  public void changeState(String stateName) throws Exception {
    getContext().setProperty(GryphonNames.NEXT_STATE, stateName);
  }
  public Object getProperty(Object key) throws java.lang.Exception {
    return getContext().getProperty(key);
  }
  public void setProperty(Object key, Object value) throws Exception {
    getContext().setProperty(key, value);
  }
  public Form getActiveState() {
    /**@todo Implement this gryphon.Application abstract method*/
    throw new java.lang.UnsupportedOperationException("Method getActiveState() not yet implemented.");
  }
  public void msgbox(String message) {
    throw new java.lang.UnsupportedOperationException("Method msgbox() not yet implemented.");
  }
  public ActionContainer getActionContainer() {
    return actionContainer;
  }
  public Descriptor getDescriptor() {
    return descriptor;
  }
}