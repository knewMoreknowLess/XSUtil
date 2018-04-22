package com.cs.util.xsutil.common.util;

/**
 * @描述：请完善类的描述
 * @作者：Administrator
 * @时间：2017/10/9
 */
public class LOG {

    public static void info(String info){
        System.out.println(info);
    }

    public static void error(String error,Exception e){
        System.out.println(error);
        e.printStackTrace();
    }
}
