package gryphon.database;

import java.util.HashMap;
/**
 * ��� ��������� Java-��������� �� ������� SQL-������.
 * @author ET
 *
 */
public class MapCache
{
	private static MapCache instance = new MapCache();
	
	private HashMap all = new HashMap();
	
	private MapCache()
	{
		
	}
	
	public static MapCache getInstance()
	{
		return instance;
	}

	public HashMap getMapping(String string)
	{
		return (HashMap) all.get(string);
	}

	public void putMapping(String key, HashMap mapping)
	{
		all.put(key, mapping);
	}

}
