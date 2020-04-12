package dao;

import domain.BaseEntity;
import exception.database.NoSuchEntityException;
import exception.validator.EmptyFieldException;

import javax.ejb.Local;
import java.util.List;
import java.util.UUID;

@Local
public interface Dao {
        List<BaseEntity> getAll(Class clazz);

        BaseEntity getById(Class clazz, UUID id) throws NoSuchEntityException;

        UUID put(Class clazz, BaseEntity entity) throws NoSuchEntityException;

        void update(Class clazz, BaseEntity entity);

        boolean delete(Class clazz, UUID id);

         void checkInputParams (BaseEntity entity) throws EmptyFieldException;
}
