package gryphon;

import gryphon.common.Descriptor;

/**
 * Basic interface for all representations of data objects (instances of
 * Entity). View allows user to view/edit attributes of data object rendered by
 * this View.
 * 
 * @author Evgueni Tiourine
 */

public interface View
{
    void setEntity(Entity entity) throws Exception;

    Entity getEntity() throws Exception;

    void updateView() throws Exception;

    void updateEntity() throws Exception;

    Descriptor getDescriptor() throws Exception;

    void setDescriptor(Descriptor descriptor) throws Exception;

    // Component getComponent() throws Exception;
    void init() throws Exception;

}
