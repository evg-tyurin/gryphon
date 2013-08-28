package gryphon.ldap;

import gryphon.common.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.LdapName;
/**
 *  ласс дл€ св€зи с деревом каталогов LDAP дл€ работы с реестром пользователей.
 * @author etyurin
 *
 */
public class LdapBrowser
{
	private Properties conf = new Properties();

	public LdapBrowser() {
		this("ldap.properties");
	}
	public LdapBrowser(String resourceName){
		try {
			conf.load(getClass().getResourceAsStream(resourceName));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}
	/**
	 * @param args
	 * @throws NamingException
	 */
	public static void main(String[] args) throws NamingException
	{
		new LdapBrowser().getAllUsers();
	}
	/**
	 * ѕрочитать всех пользователей из LDAP.
	 * @return карта пользователей, где ключ - uid пользовател€, а значение - карта атрибутов пользователей (uid,sn,givenName,mail).
	 * @throws NamingException
	 */
	public Map<String, HashMap<String, Object>> getAllUsers() throws NamingException
	{
		DirContext ctx = null;
		try {
			Hashtable<String, String> environment = new Hashtable<String, String>();
			environment.put(LdapContext.CONTROL_FACTORIES,   "com.sun.jndi.ldap.ControlFactory");
			environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			environment.put(Context.PROVIDER_URL, conf.getProperty("ldap.url"));
			environment.put(Context.SECURITY_AUTHENTICATION, "simple");
			environment.put(Context.SECURITY_PRINCIPAL, conf.getProperty("ldap.user"));
			environment.put(Context.SECURITY_CREDENTIALS, conf.getProperty("ldap.password"));
			environment.put(Context.STATE_FACTORIES, "PersonStateFactory");
			environment.put(Context.OBJECT_FACTORIES, "PersonObjectFactory");

			// connect to LDAP
			ctx = new InitialDirContext(environment);
			
			Map<String,HashMap<String,Object>> groups = getAllGroups(ctx); 

			// Specify the search filter
			String filter = conf.getProperty("ldap.search.filter"); 

			// limit returned attributes to those we care about
			String[] attrIDs = { "sAMAccountName", "sn", "givenName", "mail", "memberOf" };

			SearchControls ctls = new SearchControls();
			ctls.setReturningAttributes(attrIDs);
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			Name searchBase = new LdapName(conf.getProperty("ldap.searchbase"));
			// Search for objects using filter and controls
			NamingEnumeration<SearchResult> answer = ctx.search(searchBase, filter, ctls);

			Map<String,HashMap<String,Object>> users = new HashMap<String, HashMap<String,Object>>(); 
			while (answer.hasMore()) {
				SearchResult sr = answer.next();
				Attributes attrs = sr.getAttributes();
				String uid = get(attrs,"sAMAccountName");
				String surName = get(attrs,"sn");
				String givenName = get(attrs,"givenName");
				String mail = get(attrs,"mail");
				List<String> memberOf = getAll(attrs,"memberOf");
				addNestedGroups(memberOf, groups);
//			System.out.println(uid+": "+surName + " " + givenName+ ", "+mail);
				HashMap<String, Object> user = new HashMap<String, Object>();
				user.put("uid", uid);
				user.put("sn", surName);
				user.put("givenName", givenName);
				user.put("mail", mail);
				user.put("memberOf", memberOf.toArray(new String[]{}));
				users.put(uid, user);
			}
			return users;
		} finally {
			if (ctx!=null)
				ctx.close();
		}
	}
	/** ƒобавить к группам первого уровн€ все вложенные */
	private void addNestedGroups(List<String> memberOf, Map<String, HashMap<String, Object>> groups)
	{
		ArrayList<String> newGroups = new ArrayList<String>(); 
		for (String groupName : memberOf) {
			HashMap<String, Object> groupInfo = groups.get(groupName);
			if (groupInfo==null)
				continue;
			String[] children = (String[]) groupInfo.get("memberOf");
			if (children==null)
				continue;
			for (String name : children) {
				if (!memberOf.contains(name) && !newGroups.contains(name))
					newGroups.add(name);
			}
		}
		memberOf.addAll(newGroups);
	}
	private Map<String, HashMap<String, Object>> getAllGroups(DirContext ctx) throws NamingException
	{
		// Specify the search filter
		String filter = conf.getProperty("ldap.search.filter"); 

		// limit returned attributes to those we care about
		String[] attrIDs = { "sAMAccountName", "cn", "memberOf" };

		SearchControls ctls = new SearchControls();
		ctls.setReturningAttributes(attrIDs);
		ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		Name searchBase = new LdapName(conf.getProperty("ldap.groups.searchbase"));
		// Search for objects using filter and controls
		NamingEnumeration<SearchResult> answer = ctx.search(searchBase, filter, ctls);

		Map<String,HashMap<String,Object>> groups = new HashMap<String, HashMap<String,Object>>(); 
		while (answer.hasMore()) {
			SearchResult sr = answer.next();
			Attributes attrs = sr.getAttributes();
			String uid = get(attrs,"cn");//sAMAccountName returns shortened name
			String name = get(attrs,"cn");
			String[] memberOf = getAll(attrs,"memberOf").toArray(new String[]{});
//		System.out.println(uid+": "+surName + " " + givenName+ ", "+mail);
			HashMap<String, Object> group = new HashMap<String, Object>();
			group.put("uid", uid);
			group.put("cn", name);
			group.put("memberOf", memberOf);
			groups.put(uid, group);
		}
		Logger.debug(groups+"");
		return groups;
	}
	private List<String> getAll(Attributes attrs, String attrId) throws NamingException
	{
		Attribute s = attrs.get(attrId);
		if (s==null)
			return new ArrayList<String>();
		ArrayList<String> val = new ArrayList<String>();
		for (int i = 0; i < s.size(); i++) {
			String v = ""+s.get(i);
			if (v.indexOf("CN=")>=0){
				int end = v.indexOf(',');
				if (end>=0)
					v = v.substring("CN=".length(),end);
				else
					v = v.substring("CN=".length());
			}
			val.add(v);
		}
		return val;
	}
	private String get(Attributes attrs, String attrId) throws NamingException
	{
		Attribute s = attrs.get(attrId);
		if (s==null)
			return "";
		return ""+s.get();
	}

}
