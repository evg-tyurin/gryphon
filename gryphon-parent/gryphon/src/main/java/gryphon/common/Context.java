package gryphon.common;

import java.util.Enumeration;

/**
 * @author Evgueni Tiourine
 */

public interface Context
{
  void setProperty(Object key, Object value);
  Object getProperty(Object key) throws Exception;
  Enumeration getPropertyNames();
}