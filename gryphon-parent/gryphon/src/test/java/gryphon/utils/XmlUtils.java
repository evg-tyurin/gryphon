package gryphon.utils;

import org.w3c.dom.*;

/**
 * @author ET
 */

public class XmlUtils {
  public XmlUtils() {
  }

  /**
   * ��������� ����� �� xml-���� (��������, ����).
   * @param node xml-��� (����)
   * @return �����, ������������ ������ ����
   */
  public static String getText(Node node) {
    String text = "";
    NodeList children = node.getChildNodes();
    for (int i=0; i<children.getLength(); i++) {
      Node child = children.item(i);
      if (child.getNodeType() == Node.TEXT_NODE) {
        text += child.getNodeValue() + " ";
      }
    }
    return text;
  }
}