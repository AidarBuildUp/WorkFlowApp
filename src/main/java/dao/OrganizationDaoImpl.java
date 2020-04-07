package dao;

import dao.util.ConnectorToRepo;
import domain.Organization;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Stateless
public class OrganizationDaoImpl implements OrganizationDao {

    private static final Logger logger = Logger.getLogger(OrganizationDaoImpl.class);

    private final String GET_QUERY = "SELECT id, name, physicalAddress, legalAddress " + //TODO add manager
            "FROM Organization";
    private final String [] COLUMN_NAMES = {"id", "name", "physicalAddress", "legalAddress"};

    @EJB
    ConnectorToRepo connectorToRepo;

    @Override
    public Organization getOrganization() {
        logger.info("start");

        Connection connection = connectorToRepo.getConnection();
        Organization organization = new Organization();

        try (Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(GET_QUERY);

            resultSet.next();
            organization.setId(resultSet.getInt(COLUMN_NAMES[0]));

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
    public Integer putOrganization() {
        return null;
    }

    @Override
    public boolean changeOrganization() {
        return false;
    }

    @Override
    public boolean deleteOrganization() {
        return false;
    }

}
