package gryphon.web;

import gryphon.UserAction;
import gryphon.common.GryphonNames;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ET
 */

public class ActionControllerServlet extends HttpServlet /*implements UserAction*/ {
  private static final String CONTENT_TYPE = "text/html; charset=windows-1251";

  public void init() throws ServletException {
  }
  //Process the HTTP Get request
  public final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
  }
  //Clean up resources
  public void destroy() {
  }
  /**
   * Тут разбираются параметры и происходит перенаправление а соотв. экшн-сервлет.
   * @param parm1
   * @param parm2
   * @throws javax.servlet.ServletException
   * @throws java.io.IOException
   */
  protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // заготовим сессионную ссылку на приложение
    WebApplication webApp = ensureApplication(request);

    GryphonWebEvent e = new GryphonWebEvent(this);
    e.setRequest(request);
    e.setResponse(response);
    try {
      // определим действие и перенаправим
      String actionKey = request.getParameter("action");
      if (actionKey == null || actionKey.equals("")) {
        actionKey = "/default";
      }
      // do action
      UserAction action = ((WebActionContainer)webApp.getActionContainer()).getUserActionByCommand(actionKey);
      action.setApplication(webApp);
      action.userActionPerformed(e);

      webApp.setProperty(GryphonNames.NEXT_STATE,
                         action.getNextForm());

      // переход к след. форме, либо к обработчику исключений
      getServletContext().getRequestDispatcher(action.getNextForm()).forward(request, response);
    }
    catch (Exception ex) {
      webApp.handle(ex);
    }
    finally {
      String nextForm = null;
      try {
        nextForm = (String) webApp.getProperty(GryphonNames.
            NEXT_STATE);
      }
      catch (Exception ex1) {
        throw new ServletException("Can't get next form name", ex1);
      }
      // переход к обработчику исключений
      getServletContext().getRequestDispatcher(nextForm).forward(request, response);
    }
  }

  protected WebApplication ensureApplication(HttpServletRequest request) throws ServletException {
    try {
      WebApplication theApp = (WebApplication) request.getSession().
          getAttribute(GryphonNames.APPLICATION_NAME);
      if (theApp == null) {
        WebApplication globalApp = (WebApplication) getServletContext().
            getAttribute(GryphonNames.APPLICATION_NAME);
        theApp = globalApp.cloneMe();
        request.getSession().setAttribute(GryphonNames.APPLICATION_NAME, theApp);
      }
      return theApp;
    }
    catch (Exception ex) {
      throw new ServletException("Can't ensure app reference",ex);
    }
  }
}