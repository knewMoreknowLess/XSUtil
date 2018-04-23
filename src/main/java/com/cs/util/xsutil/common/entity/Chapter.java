package com.cs.util.xsutil.common.entity;

public class Chapter {
    //序号
    private int ano;

    //章节
    private String chapter_num;

    //章节名
    private String chapter_name;

    //章节内容
    private String chapter_context;

    //章节字数
    private int wordNum;

    //章节名中文序号
    private String ch_ano;



    public int getWordNum() {
        return wordNum;
    }

    public void setWordNum(int wordNum) {
        this.wordNum = wordNum;
    }

    public String getCh_ano() {
        return ch_ano;
    }

    public void setCh_ano(String ch_ano) {
        this.ch_ano = ch_ano;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
    public Chapter(){
        this.chapter_context = "";
        this.chapter_name = "";
        this.chapter_num = "";
        this.ch_ano = "零";
    }

    public Chapter(int ano){
        this.ano = ano;
        this.chapter_context = "";
        this.chapter_name = "";
        this.chapter_num = "";
    }

    public String getChapter_num() {
        return chapter_num;
    }

    public void setChapter_num(String chapter_num) {
        this.chapter_num = chapter_num;
    }

    public String getChapter_name() {
        return chapter_name;
    }

    public void setChapter_name(String chapter_name) {
        this.chapter_name = chapter_name;
    }

    public String getChapter_context() {
        return chapter_context;
    }

    public void setChapter_context(String chapter_context) {
        this.chapter_context = chapter_context;
    }

    public boolean equals(Chapter chapter){
        if(chapter==null){
            return false;
        }
        if(this.getChapter_num().equals(chapter.getChapter_num()) && this.getChapter_name().equals(chapter.getChapter_name())){
            return true;
        }else
            return false;
    }
}
