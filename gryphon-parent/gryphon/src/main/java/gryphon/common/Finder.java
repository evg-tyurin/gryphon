package gryphon.common;

import gryphon.Entity;

import java.util.ArrayList;
import java.util.List;

public class Finder
{
    /**
     * @param <T>
     * @param objects
     * @param attrName
     * @param attrValue
     * @return Возвращает первый найденный элемент
     * @throws Exception
     */
    public static <T extends Entity> T find(List<T> objects, String attrName, Object attrValue) throws Exception {
        for (int i = 0; i < objects.size(); i++) {
            T obj = objects.get(i);
            Object value = obj.getAttribute(attrName);
			if (value != null) {
	            if (value.equals(attrValue)) {
	                return obj;
	            }
            }
        }
        return null;
    }

    public static <T extends Entity> List<T> findAll(List<T> objects, String attrName, Object attrValue) throws Exception
    {
        List<T> found = new ArrayList<T>();
        for (int i=0; i<objects.size(); i++) 
        {
            T obj = objects.get(i);
            Object value = obj.getAttribute(attrName);
			if (value != null) {
                if (value.equals(attrValue)) {
                    found.add(obj);
                }
            }
        }   
        return found;
    }

}
