package dao;

import dao.sql.SqlRequestType;
import dao.sql.SqlRequestUtil;
import dao.util.DbConnector;
import domain.BaseEntity;
import exception.database.NoSuchEntityException;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import java.sql.*;
import java.util.*;

public abstract class DaoBean implements Dao {

    protected static final Logger logger = Logger.getLogger(DaoBean.class);

    protected final String [] TABLE_COLUMN_NAMES = {"id"};

    protected final UUID UUID_NULL = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @EJB (beanName = "DbConnectorBean")
    DbConnector dbConnector;

    @EJB (beanName = "SqlRequestUtil")
    SqlRequestUtil sqlRequestUtil;

    @Override
    public List<BaseEntity> getAll(BaseEntity entity){
        logger.info("start");

        List<BaseEntity> entityList = new ArrayList<>();

        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(sqlRequestUtil.getSqlRequest(entity.getClass(), SqlRequestType.GET_ALL_QUERY));

            while (resultSet.next()) {

                BaseEntity baseEntity = setEntityFields(UUID.fromString(resultSet.getString(TABLE_COLUMN_NAMES[0])) , resultSet);

                entityList.add(baseEntity);
            }

            logger.info("success");
            return entityList;

        } catch (SQLException e) {

            logger.error("error during fulfilling organization entity",e);

            return null;
        }
    }

    @Override
    public BaseEntity getById(BaseEntity entity, UUID id) throws NoSuchEntityException {
        logger.info("start");

        try (Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestUtil.getSqlRequest(entity.getClass(), SqlRequestType.GET_BY_ID))){

            preparedStatement.setObject(1, id, Types.OTHER);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (!resultSet.next()) throw new NoSuchEntityException("No entity by id = " + id);

            entity = setEntityFields(id, resultSet);

            logger.info("success");
            return entity;

        } catch (SQLException e) {

            logger.error("error during fulfilling organization entity",e);

            return null;
        }
    }

    @Override
    public UUID put(BaseEntity entity) {
        logger.info("start");

        UUID id = UUID.randomUUID();

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestUtil.getSqlRequest(entity.getClass(), SqlRequestType.PUT_QUERY))){

            preparedStatement.setObject(1, id, Types.OTHER);
            fillPrepareStatementPut(entity, preparedStatement);

            preparedStatement.execute();

        } catch (SQLException e) {
            logger.error("error during saving organization entity", e);
            return null;
        }

        try {
            getById(entity, id);

        } catch (NoSuchEntityException e) {
            logger.error("Unknown error. Entity is not saved", e);

            return null;
        }
        return id;
    }



    @Override
    public void update(BaseEntity entity) {
        logger.info("start");

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestUtil.getSqlRequest(entity.getClass(), SqlRequestType.POST_QUERY))){

            fillPrepareStatementUpdate(entity, preparedStatement);

            preparedStatement.execute();

        } catch (SQLException e) {
            logger.error("error during saving employee entity", e);
        }

        try {
            getById(entity, entity.getId());

        } catch (NoSuchEntityException e) {
            logger.error("Unknown error. Entity is not saved", e);


        }
    }

    @Override
    public boolean delete(BaseEntity entity, UUID id) {
        logger.info("start");

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestUtil.getSqlRequest(entity.getClass(), SqlRequestType.DELETE_QUERY))){

            preparedStatement.setObject(1, id, Types.OTHER);
            preparedStatement.execute();
            return true;

        } catch (SQLException e) {

            logger.error("error deleting entity", e);
            return false;
        }
    }

    protected abstract BaseEntity setEntityFields(UUID id, ResultSet resultSet) throws SQLException;

    protected abstract void fillPrepareStatementPut(BaseEntity entity, PreparedStatement preparedStatement) throws SQLException;

    protected abstract void fillPrepareStatementUpdate(BaseEntity entity, PreparedStatement preparedStatement) throws SQLException;


}
