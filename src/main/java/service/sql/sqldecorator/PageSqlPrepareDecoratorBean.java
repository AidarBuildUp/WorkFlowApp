package service.sql.sqldecorator;

import javax.ejb.Stateless;

@Stateless
public class PageSqlPrepareDecoratorBean implements PageSqlPrepareDecorator {

    private final String LIMIT_OFFSET = " LIMIT ? OFFSET ?";

    @Override
    public StringBuilder preparePaginationSql (StringBuilder sqlRequest, int limit, int offset) {
        StringBuilder paginatedSqlRequest = new StringBuilder(sqlRequest);
        paginatedSqlRequest.append(LIMIT_OFFSET);
        return paginatedSqlRequest;
    }
}


