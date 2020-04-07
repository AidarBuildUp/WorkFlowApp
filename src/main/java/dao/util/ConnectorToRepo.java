package dao.util;

import java.sql.Connection;

public interface ConnectorToRepo {
    Connection getConnection();
    void closeConnection(Connection connection);
}
