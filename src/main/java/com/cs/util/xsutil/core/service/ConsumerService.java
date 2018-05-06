package com.cs.util.xsutil.core.service;

import com.cs.util.xsutil.common.base.BaseService;
import com.cs.util.xsutil.common.util.StringUtil;
import com.cs.util.xsutil.core.dao.ConsumerDao;
import com.cs.util.xsutil.core.entity.Consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService extends BaseService<Consumer,ConsumerDao,String> {
    @Value("${sys_config.account_length}")
    private int account_length;

    public int findComsumerCount(String account){
        return getDao().findComsumerCount(account);
    }

    public String getOnlyAccount(){
        String account = StringUtil.getRandomStr(account_length);
        int i = findComsumerCount(account);
        if(i>0){
            return getOnlyAccount();
        }else
            return account;
    }
}
