package gryphon.database.remote;

import junit.framework.TestCase;

public class MessageTest extends TestCase
{

	public void testSetFlag()
	{
		Message m = new Message();
		assertTrue(m.isFlag(Message.ALL_IN_MEMORY));
		
		m.setFlag(32, true);
		assertEquals(m.getFlags(), 33);
		
		m.setFlag(16, true);
		assertEquals(m.getFlags(), 49);
		
		m.setFlag(16, true);
		assertEquals(m.getFlags(), 49);
		
		m.setFlag(16, false);
		assertEquals(m.getFlags(), 33);

		m.setFlag(32, false);
		assertEquals(m.getFlags(), 1);

		m.setFlag(Message.ALL_IN_MEMORY, false);
		assertEquals(m.getFlags(), 0);

		m.setFlag(Message.ALL_IN_MEMORY, false);
		assertEquals(m.getFlags(), 0);
		
	}

}
