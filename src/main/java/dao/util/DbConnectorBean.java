package dao.util;

import org.apache.log4j.Logger;

import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Singleton
@Local(DbConnector.class)
public class DbConnectorBean implements DbConnector {

    private static final Logger logger = Logger.getLogger(DbConnectorBean.class);

    private DataSource connectionPool;

    @Override
    public Connection getConnection() {

        logger.info("start");
        logger.info("Connection pool is " + connectionPool);

        try{
            if (connectionPool == null) {
                connectionPool = setConnection();
            }

            logger.info("success");

            return connectionPool.getConnection();

        } catch (NamingException | SQLException e) {

        logger.error("DataSourcePool connection error");
        logger.error(e);

        return null;
    }

    }

    private DataSource setConnection() throws SQLException, NamingException {
        logger.info("start");

        InitialContext initialContext = new InitialContext();
        DataSource ds = (DataSource) initialContext.lookup("jdbc/SQLserver");

        try (Connection connection = ds.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeQuery("SELECT * FROM Organization");
        }

        logger.info("success");

        return ds;

    }
}
