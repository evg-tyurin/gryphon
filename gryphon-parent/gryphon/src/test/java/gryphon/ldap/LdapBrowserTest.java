package gryphon.ldap;

import gryphon.common.Logger;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import junit.framework.TestCase;

public class LdapBrowserTest extends TestCase
{

	public void testGetAllUsers() throws NamingException
	{
		Map<String, HashMap<String, Object>> users = new LdapBrowser().getAllUsers();
		for (String uid : users.keySet()) {
			Logger.debug(uid+" => "+users.get(uid));
		}
	}

}
