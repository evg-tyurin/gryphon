package gryphon.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;

/**
 * ����� ������� ��� ������ � XML-�������, DOM-����������� � XPath-�����������. 
 * @author ET
 */
public class XmlUtils
{
	/**
	 * ��������� ����� �� xml-���� (��������, ����).
	 * @param node xml-��� (����)
	 * @return �����, ������������ ������ ����
	 */
	public static String getText(Node node)
	{
		String text = "";
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				text += child.getNodeValue() + " ";
			}
		}
		return text;
	}
	/**
	 * ��������� XML-��������.
	 * @param xmlFile ��� �����
	 * @return DOM-��������
	 * @throws Exception
	 */
	public static Document getDomDocument(File xmlFile) throws Exception
	{
		InputStream in = new URL("file:///"+xmlFile.getAbsolutePath()).openStream();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document xmlDoc = docBuilder.parse(in);
        in.close();
		return xmlDoc;
	}
	/**
	 * ��: ���� ����� ��������� �� Utils � �� ����� ����� �� {@link #getDomDocument(File)}
	 * @param file xml ����
	 * @return �������� DOM
	 * @throws Exception
	 */
	public static Document getXML(File file) throws Exception {
		InputStream in = new FileInputStream(file);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document xml = docBuilder.parse(in);
        in.close();
		return xml;
	}
	/**
	 * ��������� ���������������� ��������.
	 * ������� ��������:
	 * <li>/foo/bar/@id 	- ������� �������� ��������
	 * <li>/foo/bar/text()  - ������� ��������� ��������
	 * @param xml DOM-��������
	 * @param xPath ������ XPath
	 * @return ��������
	 * @throws Exception
	 */
	public static String getConfigParam(Document xml, String xPath) throws Exception {
		//        NodeList nodeList = XPathAPI.selectNodeList(xml, xPath);
		NodeList nodeList = selectNodeList(xml, xPath);
        if(nodeList.getLength()==0)
        	return null;
	    return nodeList.item(0).getNodeValue();
	}
	public static NodeList selectNodeList(Node xml, String xPath) throws Exception{
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile(xPath);
		NodeList nodeList = (NodeList) expr.evaluate(xml, XPathConstants.NODESET);
		return nodeList;
	}
    public static String xmlize(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i=0; i<s.length(); ++i) {
            char c = s.charAt(i);
            switch (c) {
                case '&': sb.append("&amp;"); break;
                case '<': sb.append("&laquo;"); break;
                case '>': sb.append("&raquo;"); break;
                case '"': sb.append("&quot;"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }
}