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
     * Возвращает выбранный объект. Если таковых много, то возвращается первый
     * найденный. Порядок выборки не оговаривается и классы-имплементаторы могут
     * использовать любой порядок.
     * 
     * @return выбранный объект или первый найденный из списка выбранных
     * @throws java.lang.Exception
     */
    Entity getSelectedEntity() throws Exception;

    /**
     * Возвращает массив выбранных объектов.
     * 
     * @return массив выбранных объектов
     * @throws java.lang.Exception
     */
    Entity[] getSelection() throws Exception;
}