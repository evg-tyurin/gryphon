package gryphon.web;

import gryphon.Entity;
import gryphon.common.DescriptorEntry;
import gryphon.common.ListView;

import java.util.List;

public class HtmlTableView extends AbstractWebView implements ListView {

  private List list;

  public HtmlTableView() {
  }
  /**
   * Формирует текстовый фрагмент, который представляет собой представление объекта
   * и который мжет получен затем через getText().
   * @throws java.lang.Exception
   */
  public void updateView() throws Exception {
    setText(null);
    String s = "<table>";
    for (int j=0; j<getList().size(); j++) {
      Entity e = (Entity)getList().get(j);
      s += "<tr><td><input type=radio name=id value='"+e.getId()+"'></td>";
      for (int i = 0; i < getDescriptor().size(); i++) {
        DescriptorEntry entry = getDescriptor().getEntry(i);
        s += "<td>" + e.getAttribute(entry.getAttribute()) + "</td>";
      }
      s += "</tr>";
    }
    s += "</table>";
    setText(s);
  }
  public List getList() throws java.lang.Exception {
    return list;
  }
  public void setList(List list) throws java.lang.Exception {
    this.list = list;
  }
  public Entity getSelectedEntity() throws java.lang.Exception {
    /**@todo Implement this gryphon.gui.ListView method*/
    throw new java.lang.UnsupportedOperationException("Method getSelectedEntity() not yet implemented.");
  }

  public Entity[] getSelection() throws java.lang.Exception {
    /**@todo Implement this gryphon.gui.ListView method*/
    throw new java.lang.UnsupportedOperationException("Method getSelection() not yet implemented.");
  }

}