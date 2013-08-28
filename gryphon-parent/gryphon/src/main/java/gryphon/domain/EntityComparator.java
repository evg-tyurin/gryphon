package gryphon.domain;

import java.util.Comparator;
import gryphon.*;

/**
 * @author Evgueni Tiourine
 */

public class EntityComparator implements Comparator
{

	public static final int ASC = 1;

	public static final int DESC = -1;
	/**
	 * Имя атрибута, по которому сортировать.
	 */
	private String attributeName;
	/**
	 * Метод сортировки: по убыванию или по возарстанию. По умолчанию - по
	 * возрастанию.
	 */
	private int method = ASC;

	public EntityComparator(String attributeName) {
		this.attributeName = attributeName;
	}

	public EntityComparator(String attributeName, int method) {
		this(attributeName);
		this.method = method;
	}

	/**
	 * Сравнивает два экземпляра класса Entity посредством вызова их методов,
	 * названия которых заданы при инициализации компаратора. Сортирует по
	 * убыванию.
	 * 
	 * @param o1
	 * @param o2
	 * @return
	 * @throws java.lang.ClassCastException
	 */
	public int compare(Object o1, Object o2) throws ClassCastException
	{
		try {
			Object v1 = ((Entity) o1).getAttribute(getAttributeName());
			Object v2 = ((Entity) o2).getAttribute(getAttributeName());
			if (v1 == null && v2 == null)
				return 0;
			if (v1 == null)
				return 1;
			if (v2 == null)
				return -1;
			if (v1 instanceof Comparable && v2 instanceof Comparable) {
				Comparable c1 = (Comparable) v1;
				Comparable c2 = (Comparable) v2;
				int diff = c1.compareTo(c2);
				if (method == DESC)
					diff *= method;
				return diff;
			}
			String s1 = String.valueOf(v1);
			String s2 = String.valueOf(v2);
			int diff = s1.compareTo(s2);
			if (method == DESC)
				diff *= method;
			return diff;
		} catch (Exception e) {
			if (e instanceof ClassCastException) {
				throw (ClassCastException) e;
			}
			throw new ClassCastException(e.getClass() + ": " + e.getMessage());
		}

	}

	public boolean equals(Object obj)
	{
		return obj instanceof EntityComparator;
	}

	public String getAttributeName()
	{
		return attributeName;
	}

	public int getMethod()
	{
		return method;
	}

}