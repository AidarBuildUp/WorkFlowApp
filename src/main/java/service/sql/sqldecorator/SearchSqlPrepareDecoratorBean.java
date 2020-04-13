package service.sql.sqldecorator;

import javax.ejb.Stateless;

@Stateless
public class SearchSqlPrepareDecoratorBean implements SearchSqlPrepareDecorator {

    @Override
    public StringBuilder prepareSearchSql(StringBuilder sqlRequest, String searchAtr, String searchValue) {
        StringBuilder searchRequest = new StringBuilder(sqlRequest);
        String propertySearchValue = searchValue.split(";")[0]; //anti hack
        searchRequest.append(" WHERE ").append(searchAtr).append("=").append(propertySearchValue);
        return searchRequest;
    }

}
