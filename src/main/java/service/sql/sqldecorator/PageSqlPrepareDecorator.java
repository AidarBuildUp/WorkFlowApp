package service.sql.sqldecorator;

import javax.ejb.Local;

@Local
public interface PageSqlPrepareDecorator {
    StringBuilder preparePaginationSql (StringBuilder sqlRequest, int limit, int offset);
}
