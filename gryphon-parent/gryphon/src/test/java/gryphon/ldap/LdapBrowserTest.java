package gryphon.ldap;

import gryphon.common.Logger;
import gryphon.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import javax.naming.NamingException;

import junit.framework.TestCase;

public class LdapBrowserTest extends TestCase
{

	public void testGetAllUsers() throws NamingException, IOException
	{
		Map<String, HashMap<String, Object>> users = new LdapBrowser("/my-ldap.properties").getAllUsers();
		File expected = new File("test-data/users.txt");
		File actual = new File("test-data/users-actual.txt");
		PrintStream ps = new PrintStream(actual);
		TreeSet<String> keySet = new TreeSet<String>(users.keySet());
		for (String uid : keySet) {
			String s = uid+" => "+toString(users.get(uid));
			Logger.debug(s);
			ps.println(s);
		}
		ps.close();
		assertEquals(expected.length(), actual.length());
		String strExpected = FileUtil.read(expected);
		String strActual = FileUtil.read(actual);
		assertEquals(strExpected, strActual);
	}

	private String toString(HashMap<String, Object> hashMap)
	{
		String s = "{";
		for (String key : hashMap.keySet()) {
			Object value = hashMap.get(key);
			if (s.length()>1)
				s += ", ";
			if (value instanceof String[]){
				String[] str = (String[]) value;
				s += key+"=[";
				String strarr = "";
				for (String val : str) {
					if (strarr.length()>0)
						strarr +=", ";
					strarr += val;
				}
				s += strarr+"]";
			}
			else {
				s += key+"="+value;
			}
		}
		s += "}";
		return s;
	}

}
