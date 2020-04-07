package exception.database;

import java.sql.SQLException;

public class RepoInitializeException extends SQLException {
    public RepoInitializeException(String reason) {
        super(reason);
    }
}
