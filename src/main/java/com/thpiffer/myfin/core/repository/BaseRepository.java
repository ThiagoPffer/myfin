package com.thpiffer.myfin.core.repository;

import com.thpiffer.myfin.core.filter.ODataFilter;
import com.thpiffer.myfin.core.filter.ODataFilterService;
import com.thpiffer.myfin.core.filter.ODataFilterSpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    Class<T> getEntityClass();

    default Slice<T> findAll(String filter, PageRequest page) {
        ODataFilterService filterService = new ODataFilterService();
        ODataFilter oDataFilter = filterService.parseFilter(filter, getEntityClass());
        return findAll(ODataFilterSpecificationBuilder.buildSpecification(oDataFilter), page);
    }

}
