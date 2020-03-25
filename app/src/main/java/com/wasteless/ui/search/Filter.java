package com.wasteless.ui.search;

public class Filter {

        private String filterName;
//        private String literallyNothing;

        public Filter() {

        }

        public Filter(String name) {
            filterName = name;
        }

        public String getFilterName() {
            return filterName;
        }

        public void setFilterName(String filterName) {
            this.filterName = filterName;
        }

}
