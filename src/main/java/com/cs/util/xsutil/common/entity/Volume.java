package com.cs.util.xsutil.common.entity;


import java.util.ArrayList;
import java.util.List;

public class Volume {

    private int ano;

    //卷号
    private String volume_num;
    //卷名
    private String volume_name;

    //卷内容
    private String volume_content;

    //卷章节列表
    private List<Chapter> chapters;

    public Volume(){
        this.volume_name = "";
        this.volume_num = "";
        this.chapters = new ArrayList<>();
    }
    public Volume(int ano){
        this.ano = ano;
        this.volume_name = "";
        this.volume_num = "";
        this.chapters = new ArrayList<>();
    }

    public String getVolume_content() {
        return volume_content;
    }

    public void setVolume_content(String volume_content) {
        this.volume_content = volume_content;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getVolume_num() {
        return volume_num;
    }

    public void setVolume_num(String volume_num) {
        this.volume_num = volume_num;
    }

    public String getVolume_name() {
        return volume_name;
    }

    public void setVolume_name(String volume_name) {
        this.volume_name = volume_name;
    }
}
