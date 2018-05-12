package com.cs.util.xsutil.common.base;

import com.cs.util.xsutil.common.util.StringUtil;
import com.cs.util.xsutil.core.entity.Consumer;
import com.cs.util.xsutil.core.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {
    @Autowired
    private ConsumerService consumerService;

    @ModelAttribute("consumer")
    public Consumer addUser(ParamsDto paramsDto) {
        if (StringUtil.isEmpty(paramsDto.getTarget())) return null;
        Consumer consumer = consumerService.getById(paramsDto.getTarget());
        return consumer;
    }
}
