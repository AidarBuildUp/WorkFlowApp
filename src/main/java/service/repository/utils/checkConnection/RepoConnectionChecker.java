package service.repository.utils.checkConnection;

import java.sql.Connection;

public interface RepoConnectionChecker {
    Connection checkRepoAvailability ();
}
