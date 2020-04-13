package dao;

import domain.BaseEntity;
import exception.database.NoSuchEntityException;
import service.sql.ViewSettingsBean;

import javax.ejb.Local;
import java.util.List;
import java.util.UUID;

@Local
public interface Dao {
        List<BaseEntity> getAll(BaseEntity entity, ViewSettingsBean viewSettings);

        BaseEntity getById(BaseEntity entity, UUID id) throws NoSuchEntityException;

        UUID put(BaseEntity entity) throws NoSuchEntityException;

        void update(BaseEntity entity);

        boolean delete(BaseEntity entity, UUID id);
}
