package gryphon.samples.web;

import gryphon.common.Logger;
import gryphon.web.WebAction;

import org.junit.Test;

public class SampleActionsTest
{

	@Test
	public void test() throws Exception
	{
		SampleActions sampleActions = new SampleActions();
		sampleActions.initActions();
		WebAction hello = (WebAction) sampleActions.getUserActionByCommand("hello");
		Logger.debug(hello.toString());
	}

}
