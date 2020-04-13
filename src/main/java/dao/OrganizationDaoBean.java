package dao;

import domain.BaseEntity;
import domain.Employee;
import domain.Organization;
import exception.database.NoManagerForOrganization;
import exception.database.NoSuchEntityException;
import exception.validator.EmptyFieldException;

import javax.ejb.Stateless;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

@Stateless(name = "OrganizationDaoBean")
public class OrganizationDaoBean extends DaoBean implements Dao {

    private final String [] TABLE_COLUMN_NAMES = {"id", "name", "physicaladdress", "legaladdress", "manager_id"};

    @Override
    protected BaseEntity setEntityFields(UUID id, ResultSet resultSet) throws SQLException {
        logger.info("started >>");
        Organization organization = new Organization();

        organization.setId(id);
        organization.setName(resultSet.getString(TABLE_COLUMN_NAMES[1]));
        organization.setPhysicalAddress(resultSet.getString(TABLE_COLUMN_NAMES[2]));
        organization.setLegalAddress(resultSet.getString(TABLE_COLUMN_NAMES[3]));

        UUID manager_id = UUID.fromString(resultSet.getString(TABLE_COLUMN_NAMES[4]));

        if ( manager_id != UUID_NULL) {

            try {
                Employee manager = (Employee) getById( new Employee(),manager_id );

                organization.setManager(manager);

            } catch (NoSuchEntityException e) {

                logger.error("No employee with such id",e);
            }
        }

        logger.info("<< successed");

        return organization;
    }

    @Override
    protected void fillPrepareStatementPut(BaseEntity entity, PreparedStatement preparedStatement) throws SQLException {
        logger.info("started >>");
        Organization organization = (Organization) entity;

        preparedStatement.setString(2, organization.getName());
        preparedStatement.setString(3, organization.getPhysicalAddress());
        preparedStatement.setString(4, organization.getLegalAddress());

        if (organization.getManager() != null) {
            preparedStatement.setObject(5, organization.getManager().getId(), Types.OTHER);
        }

        checkManagerId(organization.getManager().getId());

        logger.info("<< successed");
    }

    @Override
    protected void fillPrepareStatementUpdate(BaseEntity entity, PreparedStatement preparedStatement) throws SQLException {
        Organization organization = (Organization) entity;

        preparedStatement.setString(1, organization.getName());
        preparedStatement.setString(2, organization.getPhysicalAddress());
        preparedStatement.setString(3, organization.getLegalAddress());

        if (organization.getManager() != null) {
            preparedStatement.setObject(4, organization.getManager().getId(), Types.OTHER);
        }

        checkManagerId(organization.getManager().getId());

        preparedStatement.setObject(5, organization.getId(), Types.OTHER);
    }

    private void checkManagerId (UUID managerId) throws NoManagerForOrganization {
        try {
            getById(new Employee(), managerId);

        } catch (NoSuchEntityException e) {
            logger.error("No manager id in employee table");

            throw new NoManagerForOrganization();
        }
    }
}
