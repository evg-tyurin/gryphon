package gryphon.web;

import gryphon.common.GryphonEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * <p>
 * Объект события для веб-приложения.
 * От предка отличается тем, что может внутри себя передавать
 * объекты запроса и ответа.
 * </p>
 * @author ET
 */
public class GryphonWebEvent extends GryphonEvent {
  private HttpServletRequest request;

  private HttpServletResponse response;

  public GryphonWebEvent(Object source) {
    super(source);
  }

  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  public void setResponse(HttpServletResponse response) {
    this.response = response;
  }
  public HttpServletRequest getRequest() {
    return request;
  }
  public HttpServletResponse getResponse() {
    return response;
  }
}