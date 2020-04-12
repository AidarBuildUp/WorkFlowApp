package exception.database;

import java.sql.SQLException;

public class NoSuchEntityException extends Exception {
    public NoSuchEntityException(String reason) {
        super(reason);
    }
}
