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
 * Базовая реализация формы графического интерфейса на основе AWT.
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
    // тул-бар
    toolBar.setLayout(new GridLayout(1,10));
//    toolBar.setFloatable(false);
    add(toolBar, BorderLayout.BEFORE_FIRST_LINE);
    // контент
    contentArea.setLayout(new BorderLayout());
    add(contentArea, BorderLayout.CENTER);
  }
  /**
   * Создает кнопки тул-бара и раскладывает все визуальные компоненты по местам.
   * @throws java.lang.Exception
   */
  public final void prepare() throws Exception {
    // создать кнопки
    createButtons();
    // создать вьюшки и покласть их на форму
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
   * Потомки должны переопределить этот метод.
   * В нем они создают все кнопки тул-бара, которые им нужны.
   * @throws java.lang.Exception
   */
  protected abstract void createButtons() throws Exception;
  /**
   * Создает кнопку тул-бара.
   * Метод инкапсулирует длинную цепочку вызовов и потому удобен.
   * @param actionKey идентификатор экшена
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
   * Создает вьюшку и кладет ее на форму.
   * @param viewClassName имя класса
   * @param viewDescriptor дескриптор вьюшки
   * @param location положение вьюшки на форме
   * @return созданная вьюшка
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
   * Потомки должны реализовать этот метод.
   * В нем они создают все визуальные компоненты (вьюшки, метки и пр.), которые им нужны.
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
