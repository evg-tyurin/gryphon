package gryphon.samples.web;

import gryphon.domain.SimpleEntity;

public class HelloEntity extends SimpleEntity {
  private String language;
  private String hello;

  public HelloEntity() {
  }
  public HelloEntity(String language, String hello) {
    setLanguage(language);
    setHello(hello);
  }
  public String getHello() {
    return hello;
  }
  public void setHello(String hello) {
    this.hello = hello;
  }
  public String getLanguage() {
    return language;
  }
  public void setLanguage(String language) {
    this.language = language;
  }

}