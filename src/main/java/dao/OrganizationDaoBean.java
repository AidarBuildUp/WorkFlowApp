package dao;

import domain.BaseEntity;
import domain.Employee;
import domain.Organization;
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
    protected BaseEntity setEntityFieldsExceptId(BaseEntity entity, ResultSet resultSet) throws SQLException {
        Organization organization = (Organization) entity;

        logger.info("started >>");

        organization.setName(resultSet.getString(TABLE_COLUMN_NAMES[1]));
        organization.setPhysicalAddress(resultSet.getString(TABLE_COLUMN_NAMES[2]));
        organization.setLegalAddress(resultSet.getString(TABLE_COLUMN_NAMES[3]));

        if (resultSet.getString(TABLE_COLUMN_NAMES[4]) != null) {

            try {
                Employee manager = (Employee) getById( new Employee().getClass(),UUID.fromString(resultSet.getString(TABLE_COLUMN_NAMES[4])) );

                organization.setManager(manager);

            } catch (NoSuchEntityException e) {

                logger.error("No employee with such id",e);
            }
        }

        logger.info("<< successed");

        return organization;
    }

    @Override
    protected void fillPrepareStatementExceptId(BaseEntity entity, PreparedStatement preparedStatement) throws SQLException {
        Organization organization = (Organization) entity;

        preparedStatement.setString(2, organization.getName());
        preparedStatement.setString(3, organization.getPhysicalAddress());
        preparedStatement.setString(4, organization.getLegalAddress());

        if (organization.getManager() != null) {
            preparedStatement.setObject(5, organization.getManager().getId(), Types.OTHER);

        } else {
            preparedStatement.setString(5, null); //manager deleted or was empty
        }
    }

    @Override
    public void checkInputParams(BaseEntity entity) throws EmptyFieldException {
        Organization organization = (Organization) entity;

            if ( (organization != null) && (organization.getName().isEmpty()) ||
                    (organization.getPhysicalAddress().isEmpty()) || (organization.getLegalAddress().isEmpty()) ) {
                throw new EmptyFieldException("Empty fields in required fields");
            }
    }
}
