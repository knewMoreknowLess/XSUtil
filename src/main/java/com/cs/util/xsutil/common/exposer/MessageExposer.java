package com.cs.util.xsutil.common.exposer;

import com.cs.util.xsutil.common.enums.EnumMessage;

/**
 * 请求输出的最外层
 */
public class MessageExposer<T> {

    /*
     状态码
     */
    private String code;

    /*
     信息
     */
    private String msg;

    /*
     具体的内容
     */
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public MessageExposer(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public MessageExposer(EnumMessage enumMessage,T data){
        this.code = enumMessage.code;
        this.msg = enumMessage.message;
        this.data = data;
    }
    public MessageExposer(EnumMessage enumMessage){
        this.code = enumMessage.code;
        this.msg = enumMessage.message;
        this.data=null;
    }


    public MessageExposer(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public MessageExposer() {
    }
}
