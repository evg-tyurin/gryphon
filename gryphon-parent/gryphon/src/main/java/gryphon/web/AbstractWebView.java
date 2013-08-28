package gryphon.web;

import gryphon.Entity;
import gryphon.common.Descriptor;
import gryphon.common.Logger;

public abstract class AbstractWebView implements WebView {
  private Descriptor descriptor;

  private Entity entity;

  private String text;

  public AbstractWebView() {
  }
  public void setEntity(Entity entity) throws Exception {
    this.entity = entity;
  }
  public Entity getEntity() throws Exception {
    return entity;
  }
  public abstract void updateView() throws Exception;

  public void updateEntity() throws Exception {
    Logger.log("AbstractWebView.updateEntity() does nothing. ");
  }
  public Descriptor getDescriptor() throws Exception {
    return descriptor;
  }
  public void setDescriptor(Descriptor descriptor) throws Exception {
    this.descriptor = descriptor;
  }
  public void init() throws java.lang.Exception {
    Logger.log("AbstractWebView.init() does nothing. ");
  }
  public String getText() {
    return text;
  }
  protected void setText(String text) {
    this.text = text;
  }

}