package service.sql;

import javax.ejb.Stateless;

@Stateless
public class ViewSettingsBean {

    private Search search;
    private Sort sort;
    private Pagination pagination;

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public class Search {
        private String searchAtr;
        private String searchValue;

        public void setSearchAtr(String searchAtr) {
            this.searchAtr = searchAtr;
        }

        public void setSearchValue(String searchValue) {
            this.searchValue = searchValue;
        }

        public String getSearchAtr() {
            return searchAtr;
        }

        public String getSearchValue() {
            return searchValue;
        }
    }

    public class Sort {
        private String sortAtr;
        private boolean sortDecs;

        public void setSortAtr(String sortAtr) {
            this.sortAtr = sortAtr;
        }

        public void setSortDecs(boolean sortDecs) {
            this.sortDecs = sortDecs;
        }

        public String getSortAtr() {
            return sortAtr;
        }

        public boolean isSortDecs() {
            return sortDecs;
        }
    }

    public class Pagination {
        private int limit;
        private int offset;

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getLimit() {
            return limit;
        }

        public int getOffset() {
            return offset;
        }
    }

}
