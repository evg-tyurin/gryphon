package gryphon.common;

import java.util.List;

import gryphon.*;

/**
 * @author Evgueni Tiourine
 */

public interface ListView extends View
{
    List getList() throws Exception;

    void setList(List list) throws Exception;

    /**
     * ���������� ��������� ������. ���� ������� �����, �� ������������ ������
     * ���������. ������� ������� �� ������������� � ������-�������������� �����
     * ������������ ����� �������.
     * 
     * @return ��������� ������ ��� ������ ��������� �� ������ ���������
     * @throws java.lang.Exception
     */
    Entity getSelectedEntity() throws Exception;

    /**
     * ���������� ������ ��������� ��������.
     * 
     * @return ������ ��������� ��������
     * @throws java.lang.Exception
     */
    Entity[] getSelection() throws Exception;
}