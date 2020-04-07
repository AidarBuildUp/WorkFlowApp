package dao.util;

import org.apache.log4j.Logger;
import service.repository.utils.checkConnection.RepoConnectionChecker;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.sql.Connection;
import java.sql.SQLException;

@Stateless
public class ConnectorToRepoImpl implements ConnectorToRepo {

    @EJB
    RepoConnectionChecker repoConnectionChecker;

    private static final Logger logger = Logger.getLogger(ConnectorToRepoImpl.class);

    @Override
    public Connection getConnection() {
        return repoConnectionChecker.checkRepoAvailability();
    }

    @Override
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e.getStackTrace());
            }
        }
    }
}
