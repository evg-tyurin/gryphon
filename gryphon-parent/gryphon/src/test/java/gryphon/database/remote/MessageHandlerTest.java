package gryphon.database.remote;

import gryphon.common.Logger;
import gryphon.database.Command;
import gryphon.domain.SimpleEntity;

import java.util.LinkedList;

import junit.framework.TestCase;

public class MessageHandlerTest extends TestCase
{
	private String serverUrl = "http://hive-test:9081/wps/PA_1_0_341/IcmConn";

	public void testSend() throws Exception
	{
		MessageHandler mh = MessageHandler.getInstance(serverUrl,180);
		LinkedList<String> list = new LinkedList<String>();
		for (int i = 0; i < 1000000; i++)
		{
			list.add(Math.random()+"\r\n");
		}
		Command cmd = new Command("test", SimpleEntity.class.getName(), list);
		Message m = new Message(cmd);
		Message response = mh.send(m);
		Logger.debug(response.getCommand().getOperation());
		Logger.debug(response.getCommand().getObjectName());
		Logger.debug(response.getCommand().getAttachment()+"");
	}

}
