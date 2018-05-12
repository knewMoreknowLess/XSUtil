package com.cs.util.xsutil.core.controller;

import com.cs.util.xsutil.common.base.BaseController;
import com.cs.util.xsutil.common.enums.EnumMessage;
import com.cs.util.xsutil.common.exposer.MessageExposer;
import com.cs.util.xsutil.core.entity.Consumer;
import com.cs.util.xsutil.core.other.ConsumerDto;
import com.cs.util.xsutil.core.service.ConsumerService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/consumer")
public class ConsumerController extends BaseController {

    @Value("${sys_config.account_length}")
    private int account_length;

    @Resource
    private ConsumerService consumerService;

    @PostMapping
    @ApiOperation(value="新增用户信息", notes="新增用户信息")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public MessageExposer add(Consumer consumer) {
        Consumer res = consumerService.save(consumer);
        return new MessageExposer(EnumMessage.success.code,EnumMessage.success.message,res);
    }

    @PostMapping
    @ApiOperation(value="查询用户列表", notes="查询用户列表")
    @RequestMapping(value = "/pageList",method = RequestMethod.POST)
    public MessageExposer<Consumer> pageList(ConsumerDto consumerDto) {
        Page<Consumer> page = consumerService.findAll(consumerDto.getPageable());
        return new MessageExposer(EnumMessage.success.code,EnumMessage.success.message,page);
    }

    @PostMapping
    @ApiOperation(value="查询用户信息", notes="查询用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="consumer_id",dataType="String",required=true,value="用户id"),
    })
    @RequestMapping(value = "/info",method = RequestMethod.POST)
    public MessageExposer<Consumer> info(ConsumerDto consumerDto) {
        return new MessageExposer(EnumMessage.success.code,EnumMessage.success.message,consumerService.getOne(consumerDto.getConsumer_id()));
    }

    @PostMapping
    @ApiOperation(value="删除用户", notes="删除用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name="consumer_id",dataType="String",required=true,value="用户id"),
    })
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public MessageExposer<Consumer> delete(ConsumerDto consumerDto) {
        consumerService.delete(consumerDto.getConsumer_id());
        return new MessageExposer(EnumMessage.success.code,EnumMessage.success.message);
    }

    @PostMapping
    @ApiOperation(value="test", notes="test")
    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public MessageExposer test(ConsumerDto consumerDto, @ModelAttribute("consumer") Consumer consumer) {
//        consumerService.delete(consumerDto.getConsumer_id());
        return new MessageExposer(EnumMessage.success.code,EnumMessage.success.message,"aaaaaa");
    }
}
