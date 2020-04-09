package dao.employee;

import dao.organization.OrganizationDao;
import dao.util.ConnectorToRepo;
import domain.Employee;
import domain.Organization;
import exception.database.EntityPersistException;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Stateless ()
@Local(EmployeeDao.class)
public class EmployeeDaoImpl implements EmployeeDao {

    private static final Logger logger = Logger.getLogger(EmployeeDaoImpl.class);

    private final String GET_ALL_QUERY = "SELECT id, firstName, secondName, patronymicName, organization_id " + //TODO department and function
            "FROM Employee";

    private final String GET_BY_ID = "SELECT firstName, secondName, patronymicName, organization_id " + //TODO department and function
            "FROM Employee WHERE id=?";

    private final String PUT_QUERY = "INSERT INTO Employee (id, firstName, secondName, patronymicName, organization_id)" +
            " VALUES (?, ?, ?, ?, ?)";

    private final String DELETE_QUERY = "DELETE FROM Employee WHERE id= ?";

    private final String POST_QUERY = "UPDATE Employee SET firstName=?, secondName=?, patronymicName=?, " +
            "organization_id=? WHERE id=?";

    private final String [] COLUMN_NAMES = {"id", "firstName", "secondName", "patronymicName", "organization_id"};

    @EJB
    ConnectorToRepo connectorToRepo;

    @EJB
    OrganizationDao organizationDao;


    @Override
    public Set<Employee> getAll() {
        logger.info("start");

        Set<Employee> employees = new HashSet<>();

        try (Connection connection = connectorToRepo.getConnection();
             Statement statement = connection.createStatement()){

            ResultSet resultSet = statement.executeQuery(GET_ALL_QUERY);


            while (resultSet.next()){
                Employee employee = new Employee();
                logger.info("Employee fullfilling started >>");

                employee.setId( UUID.fromString(resultSet.getString(COLUMN_NAMES[0])) );

                logger.info("   >> Employee " + COLUMN_NAMES[0] + " >" + resultSet.getString(COLUMN_NAMES[0]) );

                employee.setFirstName(resultSet.getString(COLUMN_NAMES[1]));

                logger.info("   >> Employee " + COLUMN_NAMES[1] + " >" + resultSet.getString(COLUMN_NAMES[1]) );

                employee.setSecondName(resultSet.getString(COLUMN_NAMES[2]));

                logger.info("   >> Employee " + COLUMN_NAMES[2] + " >" + resultSet.getString(COLUMN_NAMES[2]) );

                employee.setPatronymicName(resultSet.getString(COLUMN_NAMES[3]));

                logger.info("   >> Employee " + COLUMN_NAMES[3] + " >" + resultSet.getString(COLUMN_NAMES[3]) );

                employee.setOrganization(organizationDao.get());

                employees.add(employee);
            }
            return employees;

        } catch (SQLException e) {

            logger.error("error during fulfilling organization entity",e);
            logger.error(e.getStackTrace());

            return null;
        }
    }

    @Override
    public Employee getById(UUID id) {
        logger.info("start");

        Employee employee = new Employee();

        try (Connection connection = connectorToRepo.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID)){

            preparedStatement.setObject(1, id, Types.OTHER);
            ResultSet resultSet = preparedStatement.getResultSet();

            resultSet.next();

            employee.setId(id);
            employee.setFirstName(resultSet.getString(COLUMN_NAMES[1]));
            employee.setSecondName(resultSet.getString(COLUMN_NAMES[2]));
            employee.setPatronymicName(resultSet.getString(COLUMN_NAMES[3]));
            employee.setOrganization(organizationDao.get());

            logger.info("success");
            return employee;

        } catch (SQLException e) {

            logger.error("error during fulfilling organization entity",e);
            logger.error(e.getStackTrace());

            return null;
        }
    }

    @Override
    public UUID put(Employee employee) {
        logger.info("start");

        try (Connection connection = connectorToRepo.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(PUT_QUERY)){

            UUID id = UUID.randomUUID();

            preparedStatement.setObject(1, id, Types.OTHER);
            preparedStatement.setString(2, employee.getFirstName());
            preparedStatement.setString(3, employee.getSecondName());
            preparedStatement.setString(4, employee.getPatronymicName());
            preparedStatement.setObject(5, employee.getOrganization().getId(), Types.OTHER);

            preparedStatement.execute();
            getById(id);

            return id;

        } catch (SQLException e) {
            logger.error("error during saving organization entity", e);
            return null;
        }
    }

    @Override
    public boolean update(Employee employee) {
        logger.info("start");

        try (Connection connection = connectorToRepo.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(POST_QUERY)){

            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getSecondName());
            preparedStatement.setString(3, employee.getPatronymicName());
            preparedStatement.setObject(4, employee.getOrganization().getId(), Types.OTHER);
            preparedStatement.setObject(5, employee.getId(), Types.OTHER);
            preparedStatement.execute();
            return true;

        } catch (SQLException e) {
            logger.error("error during saving employee entity", e);
            return false;
        }
    }

    @Override
    public boolean delete(UUID id) {
        logger.info("start");

        try (Connection connection = connectorToRepo.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)){

            preparedStatement.setObject(1, id, Types.OTHER);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {

            logger.error("error deleting entity", e);
            logger.error(e.getStackTrace());
            return false;
        }
    }

}
