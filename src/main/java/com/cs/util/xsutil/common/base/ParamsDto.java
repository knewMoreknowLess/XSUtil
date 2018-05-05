package com.cs.util.xsutil.common.base;

import org.springframework.data.domain.PageRequest;

public class ParamsDto {

    private String token;

    private String platform;

    private int pageNumber;

    private int pageSize;

    public PageRequest getPageable(){
        if(pageNumber!=0){
            int startindex = (pageNumber==1?0:pageNumber) * pageSize;
            int endindex = pageNumber * pageSize;
            return PageRequest.of(startindex,endindex);
        }
        return PageRequest.of(0,25);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
