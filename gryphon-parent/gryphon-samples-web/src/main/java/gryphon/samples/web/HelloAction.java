package gryphon.samples.web;

import gryphon.common.GryphonEvent;
import gryphon.web.WebAction;

import java.util.ArrayList;
import java.util.List;


public class HelloAction extends WebAction {
  private static final String CONTENT_TYPE = "text/html; charset=windows-1251";
  public HelloAction() {
    super("Hello");
    putValue(NEXT_FORM, "/HelloForm.jsp");
    setCommand("hello");
  }
  public void doAction(GryphonEvent e) throws java.lang.Exception {
    // мы сами создаем сущность, однако обычно их берут из хранилища (базы данных)
    List helloList = new ArrayList();

    HelloEntity h1 = new HelloEntity("&#1088;&#1091;&#1089;&#1089;&#1082;&#1080;&#1081;","&#1079;&#1076;&#1086;&#1088;&#1086;&#1074;&#1086;, &#1084;&#1091;&#1078;&#1080;&#1082;&#1080;");
    h1.setId("1");
    helloList.add(h1);

    HelloEntity h2 = new HelloEntity("english","hi, guys");
    h2.setId("2");
    helloList.add(h2);

    HelloEntity h3 = new HelloEntity("espanol","hola, amigos");
    h3.setId("3");
    helloList.add(h3);

    getApplication().setProperty(HelloNames.HELLO, helloList);
  }

}