package dao;

import domain.BaseEntity;
import domain.Employee;
import exception.validator.EmptyFieldException;

import javax.ejb.Stateless;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

@Stateless(name = "EmployeeDaoBean")
public class EmployeeDaoBean extends DaoBean implements Dao{

    private final String [] TABLE_COLUMN_NAMES = {"id", "firstName", "secondName", "patronymicName", "function"};

    @Override
    protected BaseEntity setEntityFields(UUID id, ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();

        logger.info("started >>");

        employee.setId(id);
        logger.info("Employee id " + employee.getId());

        employee.setFirstName(resultSet.getString(TABLE_COLUMN_NAMES[1]));
        logger.info("Employee FirstName " + employee.getFirstName());

        employee.setSecondName(resultSet.getString(TABLE_COLUMN_NAMES[2]));
        logger.info("Employee SecondName " + employee.getSecondName());

        employee.setPatronymicName(resultSet.getString(TABLE_COLUMN_NAMES[3]));
        logger.info("Employee patronymicName " + employee.getPatronymicName());

        employee.setFunction(resultSet.getString(TABLE_COLUMN_NAMES[4]));
        logger.info("Employee function " + employee.getFunction());

        logger.info("<< successed");

        return employee;
    }

    @Override
    protected void fillPrepareStatementPut(BaseEntity entity, PreparedStatement preparedStatement) throws SQLException {
        logger.info("started >>");

        Employee employee = (Employee) entity;

        preparedStatement.setString(2, employee.getFirstName());
        preparedStatement.setString(3, employee.getSecondName());
        preparedStatement.setString(4, employee.getPatronymicName());
        preparedStatement.setString(5, employee.getFunction());

        logger.info("<< successed");
    }

    @Override
    protected void fillPrepareStatementUpdate(BaseEntity entity, PreparedStatement preparedStatement) throws SQLException {
        logger.info("started >>");

        Employee employee = (Employee) entity;

        preparedStatement.setString(1, employee.getFirstName());
        preparedStatement.setString(2, employee.getSecondName());
        preparedStatement.setString(3, employee.getPatronymicName());
        preparedStatement.setString(4, employee.getFunction());
        preparedStatement.setObject(5, entity.getId(), Types.OTHER);

        logger.info("<< successed");
    }

    @Override
    public void checkInputParams(BaseEntity entity) throws EmptyFieldException {
        Employee employee = (Employee) entity;

            if ( (employee != null) && (employee.getFirstName().isEmpty()) ||
                    (employee.getSecondName().isEmpty()) || (employee.getPatronymicName().isEmpty()) ||
                    (employee.getFunction().isEmpty())) {
                throw new EmptyFieldException("Empty fields in required fields");
            }
    }
}
