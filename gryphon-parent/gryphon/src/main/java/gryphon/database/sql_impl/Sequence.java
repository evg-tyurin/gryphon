package gryphon.database.sql_impl;

import gryphon.domain.SimpleEntity;

public class Sequence extends SimpleEntity
{
	public Integer getIntId()
	{
		return (Integer) getId();
	}

	public void setIntId(Integer id)
	{
		setId(id);
	}
}
