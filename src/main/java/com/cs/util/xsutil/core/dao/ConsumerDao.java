package com.cs.util.xsutil.core.dao;

import com.cs.util.xsutil.common.base.BaseDao;
import com.cs.util.xsutil.core.entity.Consumer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerDao extends BaseDao<Consumer> {
    @Query("select count(t) from #{#entityName} t where t.account= ?1")
    public int findComsumerCount(String account);
}
