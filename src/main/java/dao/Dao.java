package dao;

import domain.BaseEntity;
import exception.database.NoSuchEntityException;
import exception.validator.EmptyFieldException;

import javax.ejb.Local;
import java.util.List;
import java.util.UUID;

@Local
public interface Dao {
        List<BaseEntity> getAll(BaseEntity entity);

        BaseEntity getById(BaseEntity entity, UUID id) throws NoSuchEntityException;

        UUID put(BaseEntity entity) throws NoSuchEntityException;

        void update(BaseEntity entity);

        boolean delete(BaseEntity entity, UUID id);

         void checkInputParams (BaseEntity entity) throws EmptyFieldException;
}
