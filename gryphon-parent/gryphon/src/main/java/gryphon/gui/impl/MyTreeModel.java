package gryphon.gui.impl;

import gryphon.Entity;
import gryphon.common.Descriptor;
import gryphon.common.Logger;

import java.util.List;
import java.util.Vector;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
/**
 * @author ET
 */

public class MyTreeModel implements TreeModel
{
  public static final String NODE_CLASS_NAME = "nodeClassName";
  public static final String GET_LIST_METHOD = "getListMethod";
  public static final String CONVERTER = "converter";
  /**
   * Список сущностей, каждая из которых может содержать вложенные сущности
   */
  private Entity rootEntity;

  private Descriptor descriptor;

  private Vector treeModelListeners = new Vector();

  private Class nodeClass;

  public MyTreeModel(Entity root, Descriptor descriptor) throws Exception
  {
    this.rootEntity = root;
    this.descriptor = descriptor;
    // init()
    nodeClass = Class.forName(descriptor.getValue(NODE_CLASS_NAME));
  }
  public Object getRoot()
  {
    return rootEntity;
  }
  public Object getChild(Object parent, int index)
  {
    try {
      List children = (List) ( (Entity) parent).getAttribute(getDescriptor().
          getValue(GET_LIST_METHOD));
      return children.get(index);
    }
    catch (Exception ex) {
      Logger.logThrowable(ex);
      return "<Error>";
    }
  }
  public int getChildCount(Object parent)
  {
    try {
      List children = (List) ( (Entity) parent).getAttribute(getDescriptor().
          getValue(GET_LIST_METHOD));
      return children.size();
    }
    catch (Exception ex) {
      Logger.logThrowable(ex);
      return -1;
    }
  }
  public boolean isLeaf(Object node)
  {
//    Logger.log("cn="+node.getClass().getName());
    return !nodeClass.isAssignableFrom(node.getClass());
  }
  public void valueForPathChanged(TreePath path, Object newValue)
  {
//    Logger.log("*** valueForPathChanged : "+ path + " --> " + newValue);
  }
  public int getIndexOfChild(Object parent, Object child)
  {
    try {
      List children = (List) ( (Entity) parent).getAttribute(getDescriptor().
          getValue(GET_LIST_METHOD));
      return children.indexOf(child);
    }
    catch (Exception ex) {
      Logger.logThrowable(ex);
      return -1;
    }
  }
  public void addTreeModelListener(TreeModelListener l)
  {
    treeModelListeners.add(l);
  }
  public void removeTreeModelListener(TreeModelListener l)
  {
    treeModelListeners.remove(l);
  }
  public Entity getRootEntity()
  {
    return rootEntity;
  }
  public Descriptor getDescriptor()
  {
    return descriptor;
  }

}