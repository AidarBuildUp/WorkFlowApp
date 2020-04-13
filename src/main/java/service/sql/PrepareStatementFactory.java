package service.sql;

import javax.ejb.Local;

@Local
public interface PrepareStatementFactory {
    String prepareStatement(String sqlRequest, ViewSettingsBean viewSettingsBean);
}
