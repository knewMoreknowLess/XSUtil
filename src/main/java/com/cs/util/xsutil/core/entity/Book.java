package com.cs.util.xsutil.core.entity;

import com.cs.util.xsutil.common.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

@Entity
public class Book extends BaseEntity{
    //小说名
    private String book_name;

    //小说存储名（MD5+后缀）
    private String book_md5Name;

    //小说字数
    private String book_word_num;

    //小说类型
    private String book_type;

    //小说关键字
    private String book_key;

    //小说大小
    private int boox_size;

    //小说作者
    private String book_author_name;

    //小说图片
    private String book_image_url;

    //小说简介
    private String book_introduce;

    //小说md5
    private String md5;

    //小说校对后MD5
    private String CheckMd5;

    //小说内容(卷)
    @Transient
    private List<Volume> volumes;

    //小说存储地址
    private String book_abPath;

    //小说上传地址
    private String book_uploadPath;

    //小说文件格式
    private String book_format;

    //小说格式编码
    private String book_encode;

    //小说文件字符串
    @Transient
    private String book_content;

    //小说头部分
    @Transient
    private String book_head;

    //小说尾部分
    @Transient
    private String book_tail;

    //是否分卷
    private boolean is_volume;

    //小说章节解析正则
    private String book_reg;

    public String getCheckMd5() {
        return CheckMd5;
    }

    public void setCheckMd5(String checkMd5) {
        CheckMd5 = checkMd5;
    }

    public String getBook_uploadPath() {
        return book_uploadPath;
    }

    public void setBook_uploadPath(String book_uploadPath) {
        this.book_uploadPath = book_uploadPath;
    }

    public String getBook_reg() {
        return book_reg;
    }

    public void setBook_reg(String book_reg) {
        this.book_reg = book_reg;
    }

    public boolean isIs_volume() {
        return is_volume;
    }

    public String getBook_md5Name() {
        return book_md5Name;
    }

    public void setBook_md5Name(String book_md5Name) {
        this.book_md5Name = book_md5Name;
    }

    public void setIs_volume(boolean is_volume) {
        this.is_volume = is_volume;
    }

    public String getBook_encode() {
        return book_encode;
    }

    public void setBook_encode(String book_encode) {
        this.book_encode = book_encode;
    }

    public String getBook_head() {
        return book_head;
    }

    public void setBook_head(String book_head) {
        this.book_head = book_head;
    }

    public String getBook_tail() {
        return book_tail;
    }

    public void setBook_tail(String book_tail) {
        this.book_tail = book_tail;
    }

    public String getBook_content() {
        return book_content;
    }

    public void setBook_content(String book_content) {
        this.book_content = book_content;
    }

    public String getBook_format() {
        return book_format;
    }

    public void setBook_format(String book_format) {
        this.book_format = book_format;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_word_num() {
        return book_word_num;
    }

    public void setBook_word_num(String book_word_num) {
        this.book_word_num = book_word_num;
    }

    public String getBook_type() {
        return book_type;
    }

    public void setBook_type(String book_type) {
        this.book_type = book_type;
    }

    public String getBook_key() {
        return book_key;
    }

    public void setBook_key(String book_key) {
        this.book_key = book_key;
    }

    public int getBoox_size() {
        return boox_size;
    }

    public void setBoox_size(int boox_size) {
        this.boox_size = boox_size;
    }

    public String getBook_author_name() {
        return book_author_name;
    }

    public void setBook_author_name(String book_author_name) {
        this.book_author_name = book_author_name;
    }

    public String getBook_image_url() {
        return book_image_url;
    }

    public void setBook_image_url(String book_image_url) {
        this.book_image_url = book_image_url;
    }

    public String getBook_introduce() {
        return book_introduce;
    }

    public void setBook_introduce(String book_introduce) {
        this.book_introduce = book_introduce;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public List<Volume> getVolumes() {
        return volumes;
    }

    public void setVolumes(List<Volume> volumes) {
        this.volumes = volumes;
    }

    public String getBook_abPath() {
        return book_abPath;
    }

    public void setBook_abPath(String book_abPath) {
        this.book_abPath = book_abPath;
    }
}
