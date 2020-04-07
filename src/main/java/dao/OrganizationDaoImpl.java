package dao;

import dao.util.ConnectorToRepo;
import domain.Organization;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.UUID;

@Stateless
public class OrganizationDaoImpl implements OrganizationDao {

    private static final Logger logger = Logger.getLogger(OrganizationDaoImpl.class);

    private final String GET_QUERY = "SELECT id, name, physicalAddress, legalAddress " + //TODO add manager
            "FROM Organization";

    private final String PUT_QUERY = "INSERT INTO Organization (id, name, physicalAddress, legalAddress) VALUES (?, ?, ?, ?)";

    private final String DELETE_QUERY = "DELETE FROM Organization WHERE id= ?";

    private final String POST_QUERY = "INSERT INTO Organization (name, physicalAddress, legalAddress) VALUES (?, ?, ?) WHERE id=?";

    private final String [] COLUMN_NAMES = {"id", "name", "physicalAddress", "legalAddress"};

    @EJB
    ConnectorToRepo connectorToRepo;


    @Override
    public Organization get() {
        logger.info("start");

        Connection connection = connectorToRepo.getConnection();
        Organization organization = new Organization();

        try (Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(GET_QUERY);

            resultSet.next();
            organization.setId( UUID.fromString(resultSet.getString(COLUMN_NAMES[0])) );

            resultSet.next();
            organization.setName(resultSet.getString(COLUMN_NAMES[1]));

            resultSet.next();
            organization.setPhysicalAddress(resultSet.getString(COLUMN_NAMES[2]));

            resultSet.next();
            organization.setLegalAddress(resultSet.getString(COLUMN_NAMES[3]));

        } catch (SQLException e) {

            logger.error("error during fulfilling organization entity");
            logger.error(e.getStackTrace());

        } finally {
            connectorToRepo.closeConnection(connection);
        }
        return organization;
    }

    @Override
    public UUID put(Organization organization) {
        logger.info("start");
        Connection connection = connectorToRepo.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(PUT_QUERY)){

            UUID id = UUID.randomUUID();

            preparedStatement.setObject(1, id, Types.OTHER);
            preparedStatement.setString(2, COLUMN_NAMES[1]);
            preparedStatement.setString(3, COLUMN_NAMES[2]);
            preparedStatement.setString(4, COLUMN_NAMES[3]);

            preparedStatement.execute();
            return id;

        } catch (SQLException e) {
            logger.error("error during saving organization entity");
            return null;

        } finally {
            connectorToRepo.closeConnection(connection);
        }
    }

    @Override
    public boolean update(Organization organization) {
        logger.info("start");

        Connection connection = connectorToRepo.getConnection();

        try (PreparedStatement preparedStatement = connection.prepareStatement(POST_QUERY)){
            preparedStatement.setString(1, COLUMN_NAMES[1]);
            preparedStatement.setString(2, COLUMN_NAMES[2]);
            preparedStatement.setString(3, COLUMN_NAMES[3]);
            preparedStatement.setString(4, COLUMN_NAMES[0]);
            preparedStatement.execute();
            return true;

        } catch (SQLException e) {
            logger.error("error during saving organization entity");
            return false;
        } finally {
            connectorToRepo.closeConnection(connection);
        }
    }

    @Override
    public boolean delete(Integer id) {
        logger.info("start");

        Connection connection = connectorToRepo.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)){
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {

            logger.error("error deleting entity");
            logger.error(e.getStackTrace());
            return false;

        } finally {
            connectorToRepo.closeConnection(connection);
        }
    }

}
