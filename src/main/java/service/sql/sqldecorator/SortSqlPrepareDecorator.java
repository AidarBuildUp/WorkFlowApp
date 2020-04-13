package service.sql.sqldecorator;

import javax.ejb.Local;

@Local
public interface SortSqlPrepareDecorator {
    StringBuilder prepareSortSql (StringBuilder sqlRequest, String sortArt, boolean decs);
}
