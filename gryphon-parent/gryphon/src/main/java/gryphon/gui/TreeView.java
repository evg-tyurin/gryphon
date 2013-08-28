package gryphon.gui;

import gryphon.Entity;
import gryphon.View;

/**
 * @author ET
 */

public interface TreeView extends View
{
  Entity getSelectedEntity() throws Exception;
}