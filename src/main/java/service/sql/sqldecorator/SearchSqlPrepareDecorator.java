package service.sql.sqldecorator;

import javax.ejb.Local;

@Local
public interface SearchSqlPrepareDecorator {
    StringBuilder prepareSearchSql (StringBuilder sqlRequest, String searchAtr, String searchValue);
}
