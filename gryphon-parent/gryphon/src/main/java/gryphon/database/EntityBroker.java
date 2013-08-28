package gryphon.database;

import gryphon.Entity;

import java.util.List;
import java.util.Properties;

/**
 * ������ (���������) ��� ���������� ���������� �������� � ������������ ����� ��������.
 * ���������� � ����������� ���������� ��������, ��������������� ������-���� ���������.
 * @author ET
 */
public interface EntityBroker
{
    public void setDatabaseBroker(DatabaseBroker broker);

    /**
     * ��������� (insert) ������ � ���������.
     * 
     * @param entity ������, ������� ���� �������� � ���������
     * @return ���������� ����������� ��������
     * @throws java.lang.Exception
     */
    public int insert(Entity entity) throws Exception;

    /**
     * ��������� (update) ������ � ��.
     * 
     * @param entity ������, ������� ����� �������� � ���������
     * @throws java.lang.Exception
     */
    public int update(Entity entity) throws Exception;

    /**
     * �������� ������� � ������������ � ���������� ����������� ������ 
     * 
     * @param entityClassName
     * @param params ��������� ������
     * @return ������ ����������� Entity
     * @throws java.lang.Exception
     */
    public List select(Properties params) throws Exception;
    /**
     * �������� ���� ������ �� ��������� �������� ���������� ����������� ��������������.
     * @param id �������������
     * @return �������� ������
     * @throws Exception
     */
    public Entity select1(Object id) throws Exception;
    /**
     * ������� ������ �� ���������.
     * @param id �������������
     * @return ���������� ��������� ��������
     * @throws Exception
     */
    public int delete(Object id) throws Exception;

}