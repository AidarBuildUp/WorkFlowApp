package service.sql.sqldecorator;

import javax.ejb.Stateless;

@Stateless
public class SortSqlPrepareDecoratorBean implements SortSqlPrepareDecorator{
    private final String SORTED_BY = " SORTED BY ";

    @Override
    public StringBuilder prepareSortSql(StringBuilder sqlRequest, String sortAtr, boolean decs) {
        StringBuilder sortRequest = new StringBuilder(sqlRequest);
        String propertySearchValue = sortAtr.split(";")[0]; //anti hack
        sortRequest.append(SORTED_BY).append(sortAtr);
        if (decs) sortRequest.append(" DECS");
        return sortRequest;
    }
}
