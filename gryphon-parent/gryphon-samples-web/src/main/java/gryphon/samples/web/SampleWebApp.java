package gryphon.samples.web;

import javax.servlet.*;

import gryphon.web.*;

public class SampleWebApp extends GryphonWebApp {
  public SampleWebApp() {
  }
  /**
   * «агружает глобальные параметры, бандлы и пр.
   * @throws java.lang.Exception
   */
  public void init() throws ServletException {
    try {
      getDescriptor().putValue(ACTION_CONTAINER, SampleActions.class.getName());
      super.init();
    }
    catch (Exception ex) {
      throw new ServletException("Can't init SampleWebApp",ex);
    }
  }

}