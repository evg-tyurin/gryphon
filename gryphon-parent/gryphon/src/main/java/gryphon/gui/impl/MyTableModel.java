package gryphon.gui.impl;

import gryphon.Entity;
import gryphon.common.Descriptor;
import gryphon.common.DescriptorEntry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * @author Evgeny Tyurin
 */
public class MyTableModel extends AbstractTableModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";
	/**
	 * Дескриптор
	 */
	private Descriptor descriptor;
	/**
	 * Списковый объект - источник данных для таблицы
	 */
	private List list;

	/** Количество строк данных */
	private int rowCount;

	/**
	 * Промежуточное хранилище данных, используемое таблицей
	 */
	private Object[][] data;
  /**
	 * Конструирует модель данных на основе дескриптора. Данные представлены
	 * списком экземпляров Entity
	 * 
	 * @param list
	 *            список экземпляров Entity
	 * @param descriptor
	 *            дескриптор
	 * @throws java.lang.Exception
	 */
  public MyTableModel(List list, Descriptor descriptor) throws Exception
  {
    this.descriptor = descriptor;
    this.list = list;
    //  сколько дополнительно строк нужно (итоги)
    int addRows = 0;
    int totalTypes = 1;
    for (int i=0; i<getDescriptor().size(); i++) {
      DescriptorEntry e = getDescriptor().getEntry(i);
      if (e.getBooleanValue(JTableListView.NEED_TOTAL)) {
        int totalType = e.getIntValue(JTableListView.TOTAL_TYPE);
        if ((totalType & totalTypes) == 0) {
          // такого типа итогов у предыдущих столбиков еще не было
          addRows++;
          totalTypes |= totalType;
        }
      }
    }
    this.rowCount = getList().size()+addRows;
    data = new Object[getRowCount()][getColumnCount()];

    for (int i=0; i<getList().size(); i++) {
      Entity e = (Entity)getList().get(i);
      for (int k=0; k<getColumnCount(); k++) {
        Object value = e.getAttribute( descriptor.getEntry(k).getAttribute() );
        if (value instanceof Date){
        	String format = descriptor.getValue("format");
        	if (format==null)
        		format = DEFAULT_DATE_FORMAT;
        	value = new SimpleDateFormat(format).format((Date)value);
        }
		data [i][k] = value;
      }// for k
    }// for ( rows )
    // теперь итоговые значения
    // нужен COUNT ?
    if ((totalTypes & JTableListView.TOTAL_COUNT) == JTableListView.TOTAL_COUNT) {
      for (int k=0; k<getColumnCount(); k++) {
        if (k==0) {
          data[getList().size()][k] = "Итого:";
        }
        else if (k==getColumnCount()-1) {
          data[getList().size()][k] = new Integer(getList().size());
        }
        else {
          data[getList().size()][k] = "";
        }
      }// for k
    }
    // нужен SUM ?
    if ((totalTypes & JTableListView.TOTAL_SUM) == JTableListView.TOTAL_SUM) {
      // какой столбик суммировать?
      int columnIndex = -1;
      for (int i=0; i<getDescriptor().size(); i++) {
        DescriptorEntry e = getDescriptor().getEntry(i);
        if (e.getBooleanValue(JTableListView.NEED_TOTAL)
            && e.getIntValue(JTableListView.TOTAL_TYPE) == JTableListView.TOTAL_SUM) {
          columnIndex = i;
          break;
        }
      }
      for (int k=0; k<getColumnCount(); k++) {
        if (k==0) {
          data[getList().size()][k] = "Итого:";
        }
        else if (k==columnIndex) {
          int sum = 0;
          for (int i=0; i<getList().size(); i++) {
            Entity e = (Entity)getList().get(i);
            Number value = (Number) e.getAttribute(getDescriptor().getEntry(
                columnIndex).getAttribute());
            sum += value.intValue();
          }
          data[getList().size()][k] = new Integer(sum);
        }
      }// for kb
    }
  }
    public int getRowCount()
    {
        return rowCount;
    }

    public int getColumnCount()
    {
        return getDescriptor().size();
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return data[rowIndex][columnIndex];
    }

    public List getList()
    {
        return list;
    }

    public Descriptor getDescriptor()
    {
        return descriptor;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        try
        {
            return getDescriptor().getEntry(columnIndex).isEditable();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        this.data[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }
    
  

}