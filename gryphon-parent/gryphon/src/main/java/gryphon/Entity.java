package gryphon;

import java.io.Serializable;

/**
 * Basic interface for all data objects.
 * 
 * @author Evgueni Tiourine
 */

public interface Entity extends Serializable
{
    Object getAttribute(String name) throws Exception;

    void setAttribute(String name, Object value) throws Exception;

    Object getId();

    void setId(Object id);
}