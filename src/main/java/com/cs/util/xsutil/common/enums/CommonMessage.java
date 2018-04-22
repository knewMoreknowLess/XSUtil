package com.cs.util.xsutil.common.enums;

public enum CommonMessage {

    success("success","操作成功！"),
    error("error","操作失败！");


    CommonMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code;

    public String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
