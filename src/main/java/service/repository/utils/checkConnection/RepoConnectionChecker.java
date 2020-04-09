package service.repository.utils.checkConnection;

import javax.sql.DataSource;

public interface RepoConnectionChecker {
    DataSource checkRepoAvailability ();
}
