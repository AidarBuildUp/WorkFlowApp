package dao;

import dao.sql.SqlRequestType;
import dao.sql.SqlRequestUtil;
import dao.util.DbConnector;
import dao.util.DbConnectorBean;
import domain.BaseEntity;
import exception.database.NoSuchEntityException;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.*;

public abstract class DaoBean implements Dao {

    protected static final Logger logger = Logger.getLogger(DaoBean.class);

    protected final String [] TABLE_COLUMN_NAMES = {"id"};

    @EJB (beanName = "DbConnectorBean")
    DbConnector dbConnector;

    @EJB (beanName = "SqlRequestUtil")
    SqlRequestUtil sqlRequestUtil;

    @Override
    public List<BaseEntity> getAll(Class clazz){
        logger.info("start");

        List<BaseEntity> entityList = new ArrayList<>();

        try (Connection connection = dbConnector.getConnection();
             Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(sqlRequestUtil.getSqlRequest(clazz, SqlRequestType.GET_ALL_QUERY));
            resultSet.next();

            while (resultSet.next()) {
                BaseEntity entity = new BaseEntity();
                entity.setId( UUID.fromString(resultSet.getString(TABLE_COLUMN_NAMES[0])) );
                setEntityFieldsExceptId(entity, resultSet);
                entityList.add(entity);
            }

            logger.info("success");
            return entityList;

        } catch (SQLException e) {

            logger.error("error during fulfilling organization entity",e);

            return null;
        }
    }

    @Override
    public BaseEntity getById(Class clazz, UUID id) throws NoSuchEntityException {
        logger.info("start");
        BaseEntity entity = new BaseEntity();

        try (Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestUtil.getSqlRequest(clazz, SqlRequestType.GET_BY_ID))){

            preparedStatement.setObject(1, id, Types.OTHER);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            if (!resultSet.next()) throw new NoSuchEntityException("No entity by id = " + id);

            entity.setId(id);
            setEntityFieldsExceptId(entity, resultSet);

            logger.info("success");
            return entity;

        } catch (SQLException e) {

            logger.error("error during fulfilling organization entity",e);

            return null;
        }
    }

    @Override
    public UUID put(Class clazz, BaseEntity entity) {
        logger.info("start");

        UUID id = UUID.randomUUID();

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestUtil.getSqlRequest(clazz, SqlRequestType.PUT_QUERY))){

            preparedStatement.setObject(1, id, Types.OTHER);
            fillPrepareStatementExceptId(entity, preparedStatement);

            preparedStatement.execute();

        } catch (SQLException e) {
            logger.error("error during saving organization entity", e);
            return null;
        }

        try {
            getById(clazz, id);

        } catch (NoSuchEntityException e) {
            logger.error("Unknown error. Entity is not saved", e);

            return null;
        }
        return id;
    }



    @Override
    public void update(Class clazz, BaseEntity entity) {
        logger.info("start");

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestUtil.getSqlRequest(clazz, SqlRequestType.POST_QUERY))){

            fillPrepareStatementExceptId(entity, preparedStatement);
            preparedStatement.setObject(6, entity.getId(), Types.OTHER);

            preparedStatement.execute();

        } catch (SQLException e) {
            logger.error("error during saving employee entity", e);
        }
    }

    @Override
    public boolean delete(Class clazz, UUID id) {
        logger.info("start");

        try (Connection connection = dbConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlRequestUtil.getSqlRequest(clazz, SqlRequestType.DELETE_QUERY))){

            preparedStatement.setObject(1, id, Types.OTHER);
            preparedStatement.execute();
            return true;

        } catch (SQLException e) {

            logger.error("error deleting entity", e);
            return false;
        }
    }

    protected abstract BaseEntity setEntityFieldsExceptId(BaseEntity entity, ResultSet resultSet) throws SQLException;

    protected abstract void fillPrepareStatementExceptId(BaseEntity entity, PreparedStatement preparedStatement) throws SQLException;


}
