package gryphon.persistent.xml_impl;

import gryphon.Entity;
import gryphon.database.AbstractDatabaseBroker;
import gryphon.database.EntityBroker;
import gryphon.xml.XmlUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author ET
 */

public abstract class XmlEntityBroker implements EntityBroker {

  private AbstractDatabaseBroker databaseBroker;

  public XmlEntityBroker() {
  }
  /**
   * Сохраняет объект в БД.
   * @param entity объект
   * @throws java.lang.Exception
   */
  public int insert(Entity entity) throws Exception {
    throw new Exception ("XmlEntityBroker.save(..) is not yet implemented. ");
  }
  public Entity select1(Object id) throws Exception {
    throw new Exception("select1 not yet implemented. ");
  }
  /**
   * Выбирает объекты в соответствии с наложенными ограничениями
   * @param entityClassName
   * @param constraints ограничения
   * @return список экземпляров Entity
   * @throws java.lang.Exception
   */
  public List select(Properties constraints) throws Exception {
    List newsList = new ArrayList();
    String sUrl = getSelectSql(constraints);
    URL url = new URL(sUrl);
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    conn.setDoOutput(true);
    // constraints for selecting objects
//    setSelectParams(constraints);

    // платформа Java 1.3.1_08 не понимает POST и setRequestProperty одновременно,
    // поэтому приходится извращаться
    OutputStream out = conn.getOutputStream();
    setParams(out, constraints);
    out.close();

    InputStream in = null;
    try {
      in = conn.getInputStream();
    }
    catch (Exception e) {
      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new Exception("HTTP request failed:\n" + conn.getResponseCode() +
                            " - " + conn.getResponseMessage());
      }
      throw e;
    }

    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
    Element root = doc.getDocumentElement();
    // каждая нода в списке - это Entity.
    String tagName = getEntityTagName();
    NodeList nodes = root.getElementsByTagName(tagName);
    if (nodes.getLength() == 0) {// ничего нет, возможно, есть warning
      nodes = root.getElementsByTagName("warning");
      String warning = "";
      for (int i = 0; i < nodes.getLength(); i++) {
        warning += XmlUtils.getText(nodes.item(i));
      }
      if (!warning.equals("")) {
        throw new Exception("Remote database exception: "+warning);
      }
    }
    for (int i=0; i<nodes.getLength(); i++) {
      Entity e = createEntity( (Element) nodes.item(i));
      newsList.add(e);
    }
    return newsList;
  }
  /**
   * Дополняет при необходимости набор ограничений.
   * @param conn
   * @param constraints
   */
//  protected void setSelectParams(Properties constraints) {
//    // some authorization info
//    constraints.setProperty("d", "123456789");
//    // add params
//    Enumeration keys = constraints.keys();
//    while (keys.hasMoreElements()) {
//      String key = (String)keys.nextElement();
//      if (key.equals("forReplication") && constraints.getProperty(key).equals("true")) {
//        constraints.setProperty("limit", "100");
//      }
//    }
//  }

  protected abstract Entity createEntity(Element element) throws Exception;

  protected abstract String getSelectSql(Properties constraints) throws Exception;

  public void setDatabaseBroker(AbstractDatabaseBroker broker) {
    this.databaseBroker = broker;
  }

  public AbstractDatabaseBroker getDatabaseBroker() {
    return databaseBroker;
  }

  protected abstract String getEntityTagName();

  public int update(Entity entity) throws java.lang.Exception {
    /**@todo Implement this gryphon.permanent.EntityBroker abstract method*/
    throw new Exception("update() not yet implemented.");
  }

  protected void setParams(OutputStream connectionOut,
                                Properties constraints) throws Exception {
    Enumeration keys = constraints.keys();
    int counter = 0;
    while (keys.hasMoreElements()) {
      if (counter >= 1) {
        connectionOut.write("&".getBytes());
      }
      String key = (String) keys.nextElement();
      connectionOut.write( (key + "=" + constraints.getProperty(key)).getBytes());
      counter++;
    }
  }
}