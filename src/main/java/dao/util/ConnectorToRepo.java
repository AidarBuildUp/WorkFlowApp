package dao.util;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectorToRepo {
    Connection getConnection() throws SQLException;
}
