package gryphon.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import gryphon.common.*;
import gryphon.*;

/**
 * @author ET
 */

public class ExceptionServlet extends HttpServlet {
  private static final String CONTENT_TYPE = "text/html; charset=windows-1251";
  //Initialize global variables
  public void init() throws ServletException {
  }
  //Process the HTTP Get request
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    response.setContentType(CONTENT_TYPE);
    try {
      Application app = (Application) request.getSession().getAttribute(
          GryphonNames.APPLICATION_NAME);
      Throwable th = (Throwable) app.getContext().getProperty(GryphonNames.EXCEPTION);
      // print exception
      Logger.logThrowable(th);
      // report to the user
      ServletOutputStream out = response.getOutputStream();
      out.write("<html>".getBytes());
      out.write("<head>".getBytes());
//      out.println("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=UTF-8\">");
      out.write("<title>Ошибка</title></head>".getBytes());
      out.write("<body bgcolor=\"#ffffff\">".getBytes());
      out.write(("<p>" + th.getClass().getName() + ": " + th.getMessage() +"</p>").getBytes());
      th.printStackTrace(new PrintWriter(out));
      out.write("</body></html>".getBytes());
    }
    catch (Exception ex) {
      Logger.logThrowable(ex);
      throw new ServletException(ex.getClass().getName()+" - "+ex.getMessage());
    }
  }
  //Process the HTTP Post request
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
  //Clean up resources
  public void destroy() {
  }
}