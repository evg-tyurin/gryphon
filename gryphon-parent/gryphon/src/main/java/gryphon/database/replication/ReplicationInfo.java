package gryphon.database.replication;

import gryphon.domain.SimpleEntity;

/**
 * @author ET
 */

public class ReplicationInfo extends SimpleEntity
{
    private int maxId;

    private String entityClassName;

    public ReplicationInfo()
    {
    }

    public int getMaxId()
    {
        return maxId;
    }

    public void setMaxId(int maxId)
    {
        this.maxId = maxId;
    }

    public String getEntityClassName()
    {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName)
    {
        this.entityClassName = entityClassName;
    }

}