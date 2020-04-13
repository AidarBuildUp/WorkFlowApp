package service.sql;

import service.sql.sqldecorator.*;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class PrepareStatementFactoryBean implements PrepareStatementFactory{

    @EJB
    SearchSqlPrepareDecorator searchSqlPrepareDecorator;

    @EJB
    SortSqlPrepareDecorator sortSqlPrepareDecorator;

    @EJB
    PageSqlPrepareDecorator pageSqlPrepareDecorator;

    @Override
    public String prepareStatement(String sqlRequest, ViewSettingsBean viewSettingsBean) {
        StringBuilder preparedSqlRequest = new StringBuilder(sqlRequest);
       if (viewSettingsBean.getSearch() != null ) {
           searchSqlPrepareDecorator.prepareSearchSql(preparedSqlRequest, viewSettingsBean.getSearch().getSearchAtr(),
                                                                          viewSettingsBean.getSearch().getSearchValue());
       }

       if (viewSettingsBean.getSort() != null) {
           sortSqlPrepareDecorator.prepareSortSql(preparedSqlRequest, viewSettingsBean.getSort().getSortAtr(),
                                                                      viewSettingsBean.getSort().isSortDecs());
       }

       pageSqlPrepareDecorator.preparePaginationSql(preparedSqlRequest, viewSettingsBean.getPagination().getLimit(),
                                                                        viewSettingsBean.getPagination().getOffset());

       return preparedSqlRequest.toString();
    }
}
