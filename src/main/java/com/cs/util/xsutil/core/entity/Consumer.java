package com.cs.util.xsutil.core.entity;


import com.cs.util.xsutil.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;

/**
 * 用户类
 * @author xzh
 */
@ApiModel("Consumer(用户信息)")
@Entity
public class Consumer extends BaseEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("头像地址")
    private String head_url;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("微信唯一id")
    private String unionid;

    @ApiModelProperty("手机号")
    private String phone_number;

    @ApiModelProperty("密码")
    private String passward;

    @ApiModelProperty("token标识")
    private String token;

    public Consumer() {
        super();
    }

    public Consumer(String id) {
        super(id);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassward() {
        return passward;
    }

    public void setPassward(String passward) {
        this.passward = passward;
    }
}

