package service.repository.utils.initialize;

import exception.database.RepoInitializeException;
import org.apache.log4j.Logger;
import service.repository.utils.checkConnection.RepoConnectionChecker;
import service.repository.utils.checkConnection.RepoConnectionCheckerImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RepoInitializerIml implements RepoInitializer {

    private static final Logger logger = Logger.getLogger(RepoInitializerIml.class);
    private final String CREATE = "create";
    private final String[] TABLES = {"Organization", "Department", "Employee", "Task"};

    @Override
    public boolean initialize() {

        logger.info("start");
        RepoConnectionChecker connectionChecker = new RepoConnectionCheckerImpl();

        try (Connection connection = connectionChecker.checkRepoAvailability();){
            if (connection != null) { //connection available
                RepoInitProperty initProperty = new RepoInitPropertyImpl();

                if (initProperty != null) { // repository initialize properties available

                    if (((RepoInitPropertyImpl) initProperty)
                            .getInitializeProperties()
                            .getStartup()
                            .toLowerCase()
                            .equals(CREATE)) { //initialize property set to create at start of application

                            dropAllTables(connection);

                            logger.info("all tables droped");

                            return true;
                    }
                }
            }
        } catch (RepoInitializeException e) {

            logger.error(e.getStackTrace());

        } catch (SQLException e) {

            logger.error(e.getStackTrace());
        }
        return false;
    }

    private void dropAllTables(Connection connection) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("DROP TABLE ? IF EXISTS ");

        for (String table: TABLES
             ) {
            preparedStatement.setString(1, table);
        }
    }
}
