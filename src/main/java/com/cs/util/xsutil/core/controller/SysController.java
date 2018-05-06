package com.cs.util.xsutil.core.controller;

import com.cs.util.xsutil.common.enums.EnumMessage;
import com.cs.util.xsutil.common.exposer.MessageExposer;
import com.cs.util.xsutil.common.util.StringUtil;
import com.cs.util.xsutil.core.entity.Consumer;
import com.cs.util.xsutil.core.service.ConsumerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SysController {

    @Resource
    private ConsumerService consumerService;

    @RequestMapping(value = "hello",method = RequestMethod.GET)
    public String sysHello(String name){
        return "hello "+name;
    }

    @PostMapping
    @ApiOperation(value="自动注册", notes="自动注册")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public MessageExposer register() {
        //获取唯一账号
        String account = consumerService.getOnlyAccount();
        //保存用户 默认密码123456
        Consumer consumer = new Consumer(account,"123456");
        consumerService.save(consumer);
        return new MessageExposer(EnumMessage.success.code,EnumMessage.success.message,consumer);
    }
}
