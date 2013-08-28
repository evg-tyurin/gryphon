package gryphon.database;

import gryphon.Entity;

import java.util.List;
import java.util.Properties;

/**
 * Брокер (посредник) для выполнения датабазных операций с определенным типом объектов.
 * Вызывается и управляется датабазным брокером, соответствующим какому-либо хранилищу.
 * @author ET
 */
public interface EntityBroker
{
    public void setDatabaseBroker(DatabaseBroker broker);

    /**
     * Добавляет (insert) объект в хранилище.
     * 
     * @param entity объект, который надо добавить в хранилище
     * @return количество добавленных объектов
     * @throws java.lang.Exception
     */
    public int insert(Entity entity) throws Exception;

    /**
     * Обновляет (update) объект в БД.
     * 
     * @param entity объект, который нужно обновить в хранилище
     * @throws java.lang.Exception
     */
    public int update(Entity entity) throws Exception;

    /**
     * Выбирает объекты в соответствии с указанными параметрами поиска 
     * 
     * @param entityClassName
     * @param params параметры поиска
     * @return список экземпляров Entity
     * @throws java.lang.Exception
     */
    public List select(Properties params) throws Exception;
    /**
     * Выбирает один объект из хранилища согласно указанному уникальному идентификатору.
     * @param id идентификатор
     * @return хранимый объект
     * @throws Exception
     */
    public Entity select1(Object id) throws Exception;
    /**
     * Удаляет объект из хранилища.
     * @param id идентификатор
     * @return количество удаленных объектов
     * @throws Exception
     */
    public int delete(Object id) throws Exception;

}