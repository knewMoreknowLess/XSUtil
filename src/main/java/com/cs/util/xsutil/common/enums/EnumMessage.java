package com.cs.util.xsutil.common.enums;

public enum EnumMessage {

    success("success","操作成功！"),
    error("error","操作失败！"),

    playgame("playgame","开始游戏指令"),
    timeout("timeout","锁定时间结束通知"),


    gameServerError("gameServerError","game 服务不可用"),
    cardServerError("cardServerError","card 服务不可用"),
    terminalRemoteError("terminalRemoteError","terminal 远程调用出错"),
    remoteError("remoteError","远程调用出错"),
    tokenDataError("tokenDataError","token数据错误"),
    coinNotEnough("coinNotEnough","金币不足"),
    wxServerError("wxServiceError","wx服务错误"),
    paramsIsNULL("paramsIsNULL","参数为空"),
    noFunctionError("noFunctionError","暂无该功能"),
    consumerPlatfromError("consumerPlatfromError","用户平台信息错误"),
    consumerCardException("consumerCardException","用户卡卷信息异常"),
    updateCoinError("updateCoinError","更新金币信息错误，请联系管理员"),
    nettyServerError("nettyServerError","netty 服务不可用"),
    moneyFormatError("moneyFormatError","金额格式有误" ),



    //卡卷使用限制
    merchants("merchants","指定商户使用" ),
    goods("goods","指定商品使用" ),
    time("time","指定时段使用" ),
    none("none","使用无限制" );


    EnumMessage(String code, String message) {
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
