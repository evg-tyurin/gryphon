package gryphon.gui.awt_impl;

import gryphon.Form;
import gryphon.UserAction;
import gryphon.View;
import gryphon.common.Descriptor;
import gryphon.gui.FormContainer;
import gryphon.gui.GuiApplication;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Panel;
import java.util.HashMap;


/**
 * <p>
 * ������� ���������� ����� ������������ ���������� �� ������ AWT.
 * </p>
 * @author Evgueni Tiourine
 */

public abstract class AwtForm extends Panel implements Form
{
  public static final String NORTH = BorderLayout.NORTH;
  public static final String CENTER = BorderLayout.CENTER;
  public static final String SOUTH = BorderLayout.SOUTH;
  public static final String WEST = BorderLayout.WEST;
  public static final String EAST = BorderLayout.EAST;

  private FormContainer container;

  private Panel toolBar = new Panel();

  private Panel contentArea = new Panel();

  private HashMap views = new HashMap(6);
  private String[] viewKeys = new String[] {NORTH, CENTER, SOUTH, WEST, EAST};

  public AwtForm() {
    setLayout(new BorderLayout());
    // ���-���
    toolBar.setLayout(new GridLayout(1,10));
//    toolBar.setFloatable(false);
    add(toolBar, BorderLayout.BEFORE_FIRST_LINE);
    // �������
    contentArea.setLayout(new BorderLayout());
    add(contentArea, BorderLayout.CENTER);
  }
  /**
   * ������� ������ ���-���� � ������������ ��� ���������� ���������� �� ������.
   * @throws java.lang.Exception
   */
  public final void prepare() throws Exception {
    // ������� ������
    createButtons();
    // ������� ������ � �������� �� �� �����
    createComponents();
  }
  public void start() throws Exception {
  }
  public void pause() throws Exception {
  }
  public void resume() throws Exception {
  }
  public Object getComponent() {
    return this;
  }
  /**
   * ������� ������ �������������� ���� �����.
   * � ��� ��� ������� ��� ������ ���-����, ������� �� �����.
   * @throws java.lang.Exception
   */
  protected abstract void createButtons() throws Exception;
  /**
   * ������� ������ ���-����.
   * ����� ������������� ������� ������� ������� � ������ ������.
   * @param actionKey ������������� ������
   * @throws java.lang.Exception
   */
  protected Button createButton(Object actionKey) throws Exception {
    GuiApplication app = getContainer().getApplication();
    Button b = new Button();
    b.addActionListener((AwtActionController)app.getActionController());
    b.setLabel((String)app.getActionContainer().getUserAction(actionKey).getValue(UserAction.NAME));
    b.setActionCommand(actionKey.toString());
    return (Button)getToolBar().add(b);
  }
  protected void createView(AwtView view, String location) throws Exception {
    getContentArea().add(view.getComponent(), location);
  }
  /**
   * ������� ������ � ������ �� �� �����.
   * @param viewClassName ��� ������
   * @param viewDescriptor ���������� ������
   * @param location ��������� ������ �� �����
   * @return ��������� ������
   * @throws java.lang.Exception
   */
  protected View createView(String viewClassName, Descriptor viewDescriptor, String location) throws Exception {
    AwtView view = (AwtView)Class.forName(viewClassName).newInstance();
    if (viewDescriptor != null) {
      view.setDescriptor(viewDescriptor);
    }
    else {
      view.init();
    }
    getContentArea().add(view.getComponent(), location);
    return view;
  }
  protected View createView(Class viewClass, Descriptor viewDescriptor, String location) throws Exception {
    return createView(viewClass.getName(), viewDescriptor, location);
  }
  /**
   * ������� ������ ����������� ���� �����.
   * � ��� ��� ������� ��� ���������� ���������� (������, ����� � ��.), ������� �� �����.
   * @throws java.lang.Exception
   */
  protected abstract void createComponents() throws Exception;

  public Panel getContentArea()
  {
    return contentArea;
  }
  public Panel getToolBar()
  {
    return toolBar;
  }
  public FormContainer getContainer()
  {
    return container;
  }
  public void setContainer(FormContainer container)
  {
    this.container = container;
  }
  protected Object getProperty(String appPropertyName) throws Exception
  {
    return getContainer().getApplication().getProperty(appPropertyName);
  }

}
