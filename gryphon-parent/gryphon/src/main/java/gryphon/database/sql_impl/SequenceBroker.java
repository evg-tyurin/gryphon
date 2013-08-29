package gryphon.database.sql_impl;

import gryphon.Entity;
import gryphon.database.sql_impl.SqlEntityBroker;

import java.sql.PreparedStatement;

public class SequenceBroker extends SqlEntityBroker
{
	protected String getJavaType()
	{
		return Sequence.class.getName();
	}

	@Override
	protected void setUpdateParameters(PreparedStatement ps, Entity entity)
			throws Exception
	{
		Sequence seq = (Sequence) entity;
		// the first param is new id for SET clause
		ps.setObject(1, seq.getIntId());
		// the second param is current id for WHERE clause
		ps.setObject(2, seq.getIntId()-1);
	}

}
