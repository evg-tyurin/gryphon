package gryphon.gui.impl;


import gryphon.Entity;
import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;
import gryphon.common.ListView;
import gryphon.common.Logger;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
/**
 * Табличная вьюшка для просмотра списковых объектов.
 * Поддерживаются:
 * + стандартные кнопки
 * + контекстные меню
 * 
 * @author Evgueni Tiourine
 */

public class JTableListView extends JPanelView implements ListView
{
	private static final long serialVersionUID = 1L;

	private List buttonActions = new ArrayList();

  public static final String HEADER = "header";

  public static final String NEED_TOTAL = "needTotal";

  public static final String TOTAL_TYPE = "totalType";
  /**
   * Тип итогового значения для толбика таблицы.
   * Количество строк.
   */
  public static final int TOTAL_COUNT = 2;
  /**
   * Тип итогового значения для толбика таблицы.
   * Сумма значений по строкам.
   */
  public static final int TOTAL_SUM = 4;
  /**
   * Тип итогового значения для толбика таблицы.
   * Среднее значение по строкам.
   */
  public static final int TOTAL_AVG = 8;


  private List list;

  private JTable table;

  public JTableListView()
  {
  }
  public void updateView() throws Exception {
      // запомнить перемотку для просмотра предыдущего состояния
      Rectangle rect = null;
      int index = -1;
      if (getTable()!=null)
      {
          rect = getTable().getVisibleRect();
          int[] selection = getTable().getSelectedRows();
          if (selection.length>0)
              index = selection[0];
      }
    // сохраняем ширины столбиков, если запрещена перестановка столбиков
    int[] widths = null;
    if (getTable() != null &&
        !getTable().getTableHeader().getReorderingAllowed())
    {
      widths = new int[getTable().getColumnCount()];
      for (int i=0; i<widths.length; i++) {
        widths[i] = getTable().getColumnModel().getColumn(i).getWidth();
      }
    }
    // обычное обновление
    removeAll();
    setLayout(new BorderLayout());

    table = new JTable(new MyTableModel(getList(), getDescriptor()),
                              getTableColumnModel(getDescriptor()));

    JScrollPane scroll = new JScrollPane();
    scroll.setViewportView( table );
    scroll.setColumnHeaderView( table.getTableHeader() );

    add(scroll, BorderLayout.CENTER);
    String header = getDescriptor().getValue(HEADER);
    if (header != null) {
      add(new JLabel(header), BorderLayout.NORTH);
    }
    // standard buttons
    JToolBar buttonPane = new JToolBar();
    for (int i=0; i<buttonActions.size(); i++) {
      buttonPane.add((Action)buttonActions.get(i));
    }

    if (buttonPane.getComponentCount()>0) {
      add(buttonPane, java.awt.BorderLayout.SOUTH);
    }
    // восстанавливаем ширины столбиков
    getTable().getTableHeader().setReorderingAllowed(false);
    if (widths != null) {
      for (int i=0; i<widths.length; i++) {
        getTable().getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
      }
    }
    // показыватель контекстных менюшек, если надо
    if (getDescriptor().getValue(SHOW_POPUP) != null) {
      getTable().addMouseListener(popupEmitter);
    }
    // перемотка для просмотра предыдущего состояния
    revalidate();
    if (rect!=null)
        getTable().scrollRectToVisible(rect);
    if (index>-1 && getTable().getRowCount()>index)
        getTable().addRowSelectionInterval(index,index);
    getTable().requestFocus();
  }
  private TableColumnModel getTableColumnModel(Descriptor d) throws Exception {
    DefaultTableColumnModel model = new DefaultTableColumnModel();
    for (int i=0; i<d.size(); i++) {
      TableColumn column = new TableColumn(i);
      column.setHeaderValue(d.getEntry(i).getLabel());
      if (d.getEntry(i).getType()==DescriptorEntry.TYPE_CHECKBOX)
      {
    	  if (d.getEntry(i).getValue(DescriptorEntry.RENDERER)==null)
    		  column.setCellRenderer(new BooleanRenderer());
    	  if (d.getEntry(i).getValue(DescriptorEntry.EDITOR)==null)
    		  column.setCellEditor(new BooleanEditor());
      }
      Object renderer = d.getEntry(i).getValue(DescriptorEntry.RENDERER);
      if (renderer!=null)
      {
    	  if (renderer instanceof TableCellRenderer)
    		  column.setCellRenderer((TableCellRenderer) renderer);
    	  else
    		  column.setCellRenderer((TableCellRenderer) Class.forName((String) renderer).newInstance());
      }
      Object editor = d.getEntry(i).getValue(DescriptorEntry.EDITOR);
      if (editor!=null)
      {
    	  if (editor instanceof TableCellEditor)
    		  column.setCellEditor((TableCellEditor) editor);
    	  else
    		  column.setCellEditor((TableCellEditor) Class.forName((String) editor).newInstance());
      }
      model.addColumn(column);
    }// for
    return model;
  }
  public List getList() throws Exception {
    return list;
  }
  public void setList(List list) throws Exception {
    this.list = list;
  }
  public Entity getSelectedEntity() throws java.lang.Exception {
    int index = getTable().getSelectedRow();
    if (index >= 0) {
      return (Entity)getList().get(index);
    }
    return null;
  }
  public JTable getTable() {
    return table;
  }

  public Entity[] getSelection() throws Exception {
    int[] indices = getTable().getSelectedRows();
    Entity[] selection = new Entity[indices.length];
    for (int i= 0; i<indices.length; i++) {
      selection[i] = (Entity)getList().get(indices[i]);
    }
    return selection;
  }
  public void addButton(Action action) throws java.lang.Exception {
    buttonActions.add(action);
  }
    public void updateEntity() throws Exception
    {
        TableModel tm = getTable().getModel();
        Descriptor d = getDescriptor();
        for (int i=0; i<getList().size(); i++)
        {
            Entity e = (Entity) getList().get(i);
            for (int j=0; j<d.size(); j++)
            {
                try
                {
                	if (d.getEntry(j).isEditable())
                		e.setAttribute(d.getEntry(j).getAttribute(),tm.getValueAt(i,j));
                }
                catch (Exception e1)
                {
                    Logger.logThrowable(e1);
                }
            }
        }
    }
  
}