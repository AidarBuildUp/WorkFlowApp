package dao.util;

import org.apache.log4j.Logger;
import service.repository.utils.checkConnection.RepoConnectionChecker;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Stateless
@Local(ConnectorToRepo.class)
public class ConnectorToRepoImpl implements ConnectorToRepo {

    @EJB
    RepoConnectionChecker repoConnectionChecker;

    private static final Logger logger = Logger.getLogger(ConnectorToRepoImpl.class);

    private DataSource connectionPool;

    @Override
    public Connection getConnection() throws SQLException {
        if (connectionPool == null) {
            repoConnectionChecker.checkRepoAvailability();
        }
        return connectionPool.getConnection();
    }
}
