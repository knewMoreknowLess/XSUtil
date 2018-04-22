package com.cs.util.xsutil.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SysController {


    @RequestMapping("hello")
    public String sysHello(String name){
        return "hello "+name;
    }

}
