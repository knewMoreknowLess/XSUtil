package com.cs.util.xsutil.common.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface BaseDao<E extends BaseEntity> extends JpaRepository<E ,String>,JpaSpecificationExecutor<E> {

}
