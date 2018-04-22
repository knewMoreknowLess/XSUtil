package com.cs.util.xsutil.core;

import com.cs.util.xsutil.common.entity.Book;
import com.cs.util.xsutil.common.entity.Chapter;
import com.cs.util.xsutil.common.entity.Volume;
import com.cs.util.xsutil.common.enums.CommonMessage;
import com.cs.util.xsutil.common.enums.TipEnum;
import com.cs.util.xsutil.common.exposer.MessageExposer;
import com.cs.util.xsutil.common.util.FileUtils;
import com.cs.util.xsutil.common.util.StringUtil;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookService {

    /**
     * 小说的读取
     * @param book_path
     * @return
     */
    public MessageExposer readBook(String book_path){
        Book book = new Book();
        //获取小说格式
        String format = getFromat(book_path);
        //读取文件对象
        File file = new File(book_path);
        if(file== null || !file.exists()){
            return new MessageExposer(CommonMessage.success.code, TipEnum.fileNoExists.message);
        }
        //文件名
        book.setBoox_name(file.getName());
        //文件大小
        book.setBoox_size((int)file.length());
        //文件格式编码
        book.setBook_encode(FileUtils.getFilecharset(file));
        String getFileString = "";
        //获取文件字符串
        System.out.println("正在读取小说...");
        try {
             getFileString = FileUtils.getFileString(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        book.setBook_content(getFileString);
        //解析字符串
        System.out.println("小说读取完毕！");
        System.out.println("开始解析小说...");
        Map map = getChapterListbyString(getFileString);
        book.setBook_head((String)map.get("head"));
        book.setVolumes((List<Volume>) map.get("volumes"));
        System.out.println("小说解析完毕！");
        //去除小说重复
        System.out.println("开始去除重复章节。。。");
        List<String> removeInfo = duplicateRemoval(book);
        for (String info : removeInfo){
            System.out.println(info);
        }
        System.out.println("重复章节去除完毕！");
        return new MessageExposer(CommonMessage.success.code,CommonMessage.success.message,book);
    }


    /**
     * 将书本对象 输出到文件
     * @param book
     * @param path
     * @return
     */
    public MessageExposer writeToText(Book book,String path) {
        FileOutputStream out = null;
        try {
            //创建文件
            File file = FileUtils.createFileByString(path+File.separator+book.getBoox_name());
            //创建输出流
            out = new FileOutputStream(file);
            //写入书本头
//            FileUtils.writeStringtoOutstream(out,book.getBook_head(),book.getBook_encode());

            //获取小说所有卷
            List<Volume> volumes = book.getVolumes();
            //循环写入卷
            for (Volume volume : volumes){
                //写入
                //如果卷为第0卷（卷号和卷名都为空，判断为第0卷，如果解析到有卷号和卷名，肯定不会是第0卷）
                if("".equals(volume.getVolume_num()) && "".equals(volume.getVolume_name())){
                    //不写入卷号和卷名(就是为了不加入两个换行)
                }else {
                    //写入卷号和卷名
                    FileUtils.writeStringtoOutstream(out,"\r\n\r\n"+volume.getVolume_num()+" "+volume.getVolume_name()+"\r\n",book.getBook_encode());
                }
                //获取卷章节集合
                List<Chapter> chapters = volume.getChapters();
                //循环写入章节
                for (Chapter chapter : chapters){
                    if("".equals(chapter.getChapter_num()) && "".equals(chapter.getChapter_name())){
                        //不写入章节序号和名字(就是为了不加入两个换行)
                    }else {
                        //写入章节序号和名字
                        FileUtils.writeStringtoOutstream(out,"\r\n" + chapter.getChapter_num()+" "+chapter.getChapter_name()+"\r\n",book.getBook_encode());
                    }
                    //写入章节内容
                    FileUtils.writeStringtoOutstream(out,chapter.getChapter_context(),book.getBook_encode());
                }

            }
            //写入书结尾
            FileUtils.writeStringtoOutstream(out,book.getBook_tail(),book.getBook_encode());
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageExposer(CommonMessage.error.code,e.getMessage());
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new MessageExposer(CommonMessage.success.code,CommonMessage.success.message);
    }

    //去重复
    public List<String> duplicateRemoval(Book book){
        List<Volume> volumes = book.getVolumes();
        List<String> removeInfo = new ArrayList<>();
        //循环解析卷
        for (Volume volume : volumes){
            List<Chapter> chapters = volume.getChapters();
            List<Chapter> newChapters = new ArrayList<>();
            int i;
            for (i = 0;i<chapters.size();i++){
                Chapter chapter1 = chapters.get(i);
                for(int j=i+1;j<chapters.size()-i-1;j++){
                    Chapter chapter2 = chapters.get(j);
                    if(chapter1.equals(chapter2)){
                        chapters.remove(j);
                        removeInfo.add(chapter1.getChapter_num()+" "+chapter1.getChapter_name() + " 出现重复，已移除重复章节");
                    }
                }
                newChapters.add(chapter1);
            }
            volume.setChapters(newChapters);
        }
        if(removeInfo.size()<1){
            removeInfo.add("未发现重复章节！");
        }
        return removeInfo;
    }


    private String getFromat(String name){
        return "txt";
    }

    //解析字符串为章节
    public Map getChapterListbyString(String content){
        Map map = new HashMap();

        if(StringUtil.isBlank(content)){
            return map;
        }
        String[] lines = content.split("\r\n");
        StringBuffer head = new StringBuffer();
        //保存卷集合(如果小说不划分卷，则第0卷所含章节为小说全章节，如果小说划分了卷，则第零卷为小说开始到第一卷开始的头部部分)
        List<Volume> volumes = new ArrayList<>();
        //保存卷章节集合
        List<Chapter> chapters = new ArrayList<>();
        //保存卷对象
        Volume volume = new Volume();
        volume.setAno(0);
        //保存章节对象
        Chapter chapter = new Chapter();
        //序号从0开始第0章为小说的开头，不是真正的第一章
        chapter.setAno(0);
        StringBuffer buffer = new StringBuffer();
        boolean isFirstVolume = true;
        boolean isbegin = true;
        for (String line : lines){
            //该行为空继续
            if("null".equals(line)) continue;
            //首先判断是否为卷
            String volueReg = getReg(line,getVolumeReg());
            //如果不为卷
            if(volueReg == null){
                //通过该方法判断改行是否为新章节
                String reg = getReg(line,getRegList());
                if(reg == null){//为空不是章节
                     buffer = buffer.append("\r\n"+line);
                }else {//该行为新章节
                    //保存上一章的信息
                    if(isbegin){
                        if(isFirstVolume){
                            chapter.setChapter_context(buffer.toString());
                            buffer = new StringBuffer();
                            chapter.setWordNum(chapter.getWordNum());
                            //将该章节加入章节列表
                            chapters.add(chapter);
                            isFirstVolume=false;
                        }
                       isbegin=false;
                    }else {
                        chapter.setChapter_context(buffer.toString());
                        buffer = new StringBuffer();
                        chapter.setWordNum(chapter.getWordNum());
                        //将该章节加入章节列表
                        chapters.add(chapter);
                    }
                    int ano = chapter.getAno();
                    //初始化新章节
                    chapter = new Chapter();
                    chapter.setAno(++ano);
                    //获取章节信息
                    Pattern p = Pattern.compile(reg);//获取正则表达式中的分组，每一组小括号为一组
                    Matcher m = p.matcher(line);//进行匹配
                    if(m.find()){
                        int groupCount = m.groupCount();
                        //章节序号
                        chapter.setChapter_num(m.group(1));
                        if(groupCount>=2){
                            //章节名
                            String g2 = m.group(2);
                            chapter.setChapter_name(g2);
                        }
                    }
                }
            }else {//该行为卷，找到对应的正则
                //保存上一卷的信息
                //将当前章加入章节集合
                chapter.setChapter_context(buffer.toString());
                buffer = new StringBuffer();
                chapter.setWordNum(chapter.getWordNum());
                chapters.add(chapter);
                //将当前章节集合加入第一卷
                volume.setChapters(chapters);
                //将该卷加入卷集合
                volumes.add(volume);
                int volumeAno = volume.getAno();
                //初始化新卷
                volume = new Volume(++volumeAno);
                isbegin = true;
                //初始化新的章节集合
                chapters = new ArrayList<>();
                int a = chapter.getAno();
                chapter = new Chapter(a+1);
                //获取新卷信息
                Pattern p = Pattern.compile(volueReg);//获取正则表达式中的分组，每一组小括号为一组
                Matcher m = p.matcher(line);//进行匹配
                if(m.find()){
                    int groupCount = m.groupCount();
                    //卷序号
                    volume.setVolume_num(m.group(1));
                    if(groupCount>=2){
                        //卷名
                        String g2 = m.group(2);
                        volume.setVolume_name(g2);
                    }
                }
            }
        }
        //将未加入的章和卷 加入
        chapter.setChapter_context(buffer.toString());
        chapter.setWordNum(chapter.getWordNum());
        chapters.add(chapter);
        volume.setChapters(chapters);
        volumes.add(volume);
        map.put("head",volumes.get(0).getChapters().get(0).getChapter_context());
        map.put("volumes",volumes);
        return map;
    }

    //解析书本为卷
    public Map getVolumesByString(String book_content){
        Map map = new HashMap();

        if(StringUtil.isBlank(book_content)){
            return map;
        }
        String[] lines = book_content.split("\r\n");
        StringBuffer head = new StringBuffer();
        //保存卷集合(如果小说不划分卷，则第0卷所含章节为小说全章节，如果小说划分了卷，则第零卷为小说开始到第一卷开始的头部部分)
        List<Volume> volumes = new ArrayList<>();
        //保存卷对象
        Volume volume = new Volume();
        volume.setAno(0);

        boolean isHead = true;
        for (String line : lines){
            //该行为空继续
            if(line==null||"".equals(line)||"null".equals(line)) continue;
            //首先判断是否为卷
            String volueReg = getReg(line,getVolumeReg());
            //如果不为卷
            if(volueReg == null){
                //通过该方法判断改行是否为新章节
                if(isHead){
                    if(head.length()<1){
                        head = head.append(line);
                    }else
                        head = head.append("\r\n"+line);
                }else
                    volume.setVolume_content(volume.getVolume_content()+"\r\n"+line);

            }else {//该行为卷，找到对应的正则
                //将该卷加入卷集合
                volumes.add(volume);
                int volumeAno = volume.getAno();
                //初始化新卷
                volume = new Volume(++volumeAno);
                //初始化新的章节集合
                //获取新卷信息
                Pattern p = Pattern.compile(volueReg);//获取正则表达式中的分组，每一组小括号为一组
                Matcher m = p.matcher(line);//进行匹配
                if(m.find()){
                    int groupCount = m.groupCount();
                    //卷序号
                    volume.setVolume_num(m.group(1));
                    if(groupCount>=2){
                        //卷名
                        String g2 = m.group(2);
                        volume.setVolume_name(g2);
                    }
                }
            }
        }
        //将未加入的章和卷 加入
        volumes.add(volume);
        map.put("head",head.toString());
        map.put("volumes",volumes);
        return map;
    }

    public List<Chapter> getChaptersByString(String volumes_content){
        List<Chapter> chapters = new ArrayList<>();
        if(StringUtil.isBlank(volumes_content)){
            return chapters;
        }
        String[] lines = volumes_content.split("\r\n");
        //保存章对象
        Chapter chapter = new Chapter();
        //序号从0开始第0章为小说的开头，不是真正的第一章
        chapter.setAno(0);
        boolean isHead = true;
        for (String line : lines){
            //该行为空继续
            if(line==null||"".equals(line)||"null".equals(line)) continue;
            //首先判断是否为章
            String chapterReg = getReg(line,getRegList());
            //如果不为章
            if(chapterReg == null){
                //通过该方法判断改行是否为新章节
                chapter.setChapter_context(chapter.getChapter_context()+"\r\n"+line);

            }else {//该行为卷，找到对应的正则
                //将该卷加入卷集合
                chapter.setWordNum(chapter.getWordNum());
                chapters.add(chapter);
                int ano = chapter.getAno();
                //初始化新章
                chapter = new Chapter(++ano);
                //获取新章信息
                Pattern p = Pattern.compile(chapterReg);//获取正则表达式中的分组，每一组小括号为一组
                Matcher m = p.matcher(line);//进行匹配
                if(m.find()){
                    int groupCount = m.groupCount();
                    //卷序号
                    chapter.setChapter_num(m.group(1));
                    if(groupCount>=2){
                        //卷名
                        String g2 = m.group(2);
                        chapter.setChapter_name(g2);
                    }
                }
            }
        }
        //将未加入的章和卷 加入
        chapters.add(chapter);
        return chapters;
    }

    public String getReg(String line,List<String> regs){
        for (String reg : regs){
            if(line.matches(reg)){
                return reg;
            }
        }
        return null;
    }

    //章节名标示正则
    private List<String> getRegList(){
        List<String> regs = new ArrayList<>();
        regs.add("[ 　]*(序章)[ 　]*");
        regs.add("[ 　]*(序章)[ 　]{0,2}(\\S{1,25})[ 　]*");

        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}章)[ 　]*");
        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}章)[ 　]{0,2}(\\S{1,25})[ 　]*");

        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}节)[ 　]*");
        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}节)[ 　]{0,2}(\\S{1,25})[ 　]*");
        return regs;
    }
    //卷名匹配正则
    private List<String> getVolumeReg(){
        List<String> regs = new ArrayList<>();
        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}集)[ 　]*");
        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}集)[ 　]{0,2}(\\S{1,25})[ 　]*");

        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}篇)[ 　]*");
        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}篇)[ 　]{0,2}(\\S{1,25})[ 　]*");

        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}卷)[ 　]*");
        regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}卷)[ 　]{0,2}(\\S{1,25})[ 　]*");

        regs.add("[ 　]*(终卷)[ 　]*");
        regs.add("[ 　]*(终卷)[ 　]{0,2}(\\S{1,25})[ 　]*");
        return regs;
    }
}
