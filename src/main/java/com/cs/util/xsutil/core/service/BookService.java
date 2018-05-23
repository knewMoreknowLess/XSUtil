package com.cs.util.xsutil.core.service;

import com.cs.util.xsutil.common.base.BaseService;
import com.cs.util.xsutil.common.enums.EnumMessage;
import com.cs.util.xsutil.core.dao.BookDao;
import com.cs.util.xsutil.core.entity.Book;
import com.cs.util.xsutil.core.entity.Chapter;
import com.cs.util.xsutil.core.entity.Volume;
import com.cs.util.xsutil.core.other.CommonMessage;
import com.cs.util.xsutil.common.enums.TipEnum;
import com.cs.util.xsutil.common.exposer.MessageExposer;
import com.cs.util.xsutil.common.util.FileUtils;
import com.cs.util.xsutil.common.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BookService extends BaseService<Book,BookDao,String> {

    private int checkDepth = 10;
    /**
     * 小说的读取
     * @param book_path
     * @return
     */

    /**
     * 上传多媒体档案文件，不会对文件进行换
     * @param file name
     * @return MessageExposer
     */
    public MessageExposer uploadBook(MultipartFile file, String name, String gcPath) {
        try {
            String kzm = name.substring(name.lastIndexOf(".")+1);
            String bookName = name.substring(0,name.lastIndexOf("."));
            String md5 = FileUtils.getFileMd5(file);
            String fileName = md5 + "."+ kzm;
            String path = FileUtils.uploadfileDmt(file.getInputStream(), fileName, gcPath);
            Book book = new Book();
            book.setBook_name(bookName);
            book.setBook_format(kzm);
            book.setBook_abPath(path);
            book.setBook_uploadPath(path);
            book.setMd5(md5);
            return new MessageExposer(EnumMessage.success,book);
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageExposer(EnumMessage.error, EnumMessage.error);
        }
    }

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
        book.setBook_name(file.getName());
        //文件大小
        book.setBoox_size((int)file.length());
        //文件绝对地址
        book.setBook_abPath(file.getAbsolutePath());
        //文件父目录地址
//        book.setBook_parentPath(file.getParent());
        //文件格式编码
        book.setBook_encode(FileUtils.getFilecharset(file));
        String getFileString = "";
        //获取文件字符串
        System.out.println("正在读取小说...");
        try {
             getFileString = FileUtils.getFileString(file);
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageExposer(CommonMessage.error.code, TipEnum.fileReadError.message);
        }
        book.setBook_content(getFileString);
        //解析字符串
        System.out.println("小说读取完毕！");
        System.out.println("开始解析小说...");
        Map map = getChapterListbyString(getFileString);
        book.setBook_head((String)map.get("head"));
        book.setVolumes((List<Volume>) map.get("volumes"));
        if (book.getVolumes().size() > 1)
            book.setIs_volume(true);
        System.out.println("小说解析完毕！");

        return new MessageExposer(CommonMessage.success.code,CommonMessage.success.message,book);
    }

    public MessageExposer readBook(Book book){
        MessageExposer exposer = readBook(book.getBook_abPath());
        if("success".equals(exposer.getCode())){
            Book read = (Book) exposer.getData();
            //设置书名
            read.setBook_name(book.getBook_name());
            //设置书MD5
            read.setMd5(book.getMd5());
            //设置书格式
            read.setBook_format(book.getBook_format());
            exposer.setData(read);
            return exposer;
        }
        exposer.setData(book);
        return exposer;
    }


        /**
         * 将书本对象 输出到文件
         * @param book
         * @return
         */
    public MessageExposer writeToText(Book book) {
        String path = null;
        if(book==null) return new MessageExposer(EnumMessage.error,null);
        FileOutputStream out = null;
        File file = null;
        try {
            path = FileUtils.getFileParentDir(book.getBook_abPath());
            //创建文件
            file = FileUtils.createFileByString(path+File.separator+book.getBook_name()+"."+book.getBook_format());
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
            return new MessageExposer(CommonMessage.error.code,TipEnum.bookOutError.message);
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        book.setBook_abPath(file.getAbsolutePath());
        return new MessageExposer(CommonMessage.success.code,CommonMessage.success.message);
    }

    /**
     * 书本校对后修改书本信息
     * @param book
     */
    public void updateBookPath(Book book){
        try {
//            File file = FileUtils.createFileByString(book.getBook_abPath());
            //更换存储文件文件名
            String md5 = FileUtils.getMd5ByFile(book.getBook_abPath());
//            String md5 = "awdadwadw";
            book.setCheckMd5(md5);
            if (md5==null || book.getMd5().equals(md5)){
                return;
            }
            String fileNewName = md5+"."+book.getBook_format();
            //获取文件新的名字和路径
            String parent = FileUtils.getFileParentDir(book.getBook_abPath());
            String fileNewPath = parent+File.separator+fileNewName;

            //修改文件名
            boolean success = FileUtils.renameTo(book.getBook_abPath(),fileNewPath);
            if(success){
                book.setBook_abPath(fileNewPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bookInfoRemove(Book book){
        book.setBook_head(null);
        book.setBook_tail(null);
        book.setBook_content(null);
        book.setVolumes(null);
    }

    /**
     * 小说对象去重复
     * @param book
     * @return 去重复信息集合
     */
    public List<String> duplicateRemoval(Book book){
        List<Volume> volumes = book.getVolumes();
        List<String> removeInfo = new ArrayList<>();
        //循环解析卷
        for (Volume volume : volumes){
            List<Chapter> chapters = volume.getChapters();
            int i;
            for (i = 0;i<chapters.size();i++){
                Chapter chapter1 = chapters.get(i);
                for(int j=i+1;j<chapters.size()-i-1;j++){
                    if(j>=chapters.size()){
                        continue;
                    }
                    Chapter chapter2 = chapters.get(j);
                    if(chapter1.equals(chapter2)){
                        if(chapter2.getWordNum() > chapter1.getWordNum()){
                            chapters.set(i,chapter2);
                            chapters.set(j,chapter1);
                        }
                        chapters.remove(j);
                        removeInfo.add(chapter1.getChapter_num()+" "+chapter1.getChapter_name() + " 出现重复，已移除重复章节");
                    }
                }
            }
            volume.setChapters(chapters);
        }
        if(removeInfo.size()<1){
            removeInfo.add("未发现重复章节！");
        }
        return removeInfo;
    }

    /**
     * 简单排序，返回排序信息
     * @param book
     * @return
     */
    public List<String> bookSort(Book book){
        List<Chapter> sortInfo = charpterSortCheck(book);
        return sort(book,sortInfo);
    }

    /**
     * 检查排序异常章节（该章前checkDepth章以内）
     * @param book
     * @return
     */
    public List<Chapter> charpterSortCheck(Book book){
        List<Chapter> msg = new ArrayList<>();
        for (Volume volume : book.getVolumes()){
            List<Chapter> chapters = volume.getChapters();
            for (int i = 0;i<chapters.size();i++){
                Chapter currentChapter = chapters.get(i);
                //检测当前章节上五章重复情况
                boolean isErrorSort = checkOneChapter(chapters,currentChapter);
                if(isErrorSort){
                    msg.add(currentChapter);
                }
            }
        }
        return msg;
    }

    public List<String> sort(Book book,List<Chapter> errChapter){
        List<String> info = new ArrayList<>();
        if(errChapter.isEmpty()){
            info.add("没有排序异常的章节");
            return info;
        }
        for (Chapter chapter : errChapter){
            String s = chapter.getChapter_num()+" "+chapter.getChapter_name()+" 排序异常，正在排序。。。";
            System.out.println(s);
            //获取排序异常的章节所在的卷章节列表
            for(Volume volume : book.getVolumes()){
                List<Chapter> chapters =volume.getChapters();
                if(chapters.indexOf(chapter) == -1 || chapters.indexOf(chapter) == 0){
                    continue;
                }
                //调用排序算法，将该章重新排序
                sortOneChapter(chapters,chapter);
            }
            String s1 = chapter.getChapter_num()+" "+chapter.getChapter_name()+" 排序完成！";
            System.out.println(s1);
        }
        return info;
    }

    /**
     * 递归将该章位置调换为正确位置
     * @param chapters
     * @param chapter
     */
    private void sortOneChapter(List<Chapter> chapters,Chapter chapter){
        boolean isErr = checkOneChapter(chapters,chapter);
        if(isErr){//如果排序异常,则调换该章和前一章的位置
            int index = chapters.indexOf(chapter);
            if(index==-1 || index==0) return;
            //获取问题章的前一章
            Chapter c = chapters.get(index-1);
            chapters.set(index,c);
            chapters.set(index-1,chapter);
            //递归调用该排序方法 直到该章排序不异常
            sortOneChapter(chapters,chapter);
        }
    }

    /**
     * 检查一章是否排序异常
     * @param chapters
     * @param currentChapter
     * @return 排序异常 则ture
     */
    private boolean checkOneChapter(List<Chapter> chapters,Chapter currentChapter){
        boolean isErrorSort = false;
        int errorSortCount = 0;
        int i = chapters.indexOf(currentChapter);
        int c_num = 0;
        try {
            c_num = StringUtil.chToint(currentChapter.getCh_ano());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int j = 1;j<checkDepth+1;j++){
            if((i-j)<=0){
                break;
            }
            Chapter c = chapters.get(i-j);
            int f_num = 0;
            try {
                f_num = StringUtil.chToint(c.getCh_ano());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(c_num < f_num){
                if(j==1){//如果该章和前一章节排序不对，该章排序标识排序异常
                    isErrorSort = true;
                }
                errorSortCount++;//和前面章节排序不对加一
            }
        }
        //如果该章和前面checkDepth数量的章节排序都不对，则 排除异常（有可能分卷解析失败，所以不定为异常）
        if(isErrorSort && (errorSortCount<checkDepth)){
//            Map m = new HashMap();
//            String s = currentChapter.getChapter_num()+" "+currentChapter.getChapter_name()+" 排序异常";
//            m.put("chapter",currentChapter);
//            m.put("msg",s);
            //排除章节命名错误情况
            for(int k= 0;k<i;k++){
                Chapter c = chapters.get(k);
                if(c.getChapter_num().equals(currentChapter.getChapter_num())){
                    //如果之前存在该章节号，则该章不定为排序异常
                    return false;
                }
            }
            //如果当前章和下一章连续,不定为异常
            Chapter lastChapter = chapters.get(++i);
            if(isSerial(currentChapter,lastChapter)){
                return false;
            }

            return true;
        }
        return false;
    }

    private boolean isSerial(Chapter one,Chapter twe){
        int o = FileUtils.getWordNum(one.getCh_ano());
        int t = FileUtils.getWordNum(twe.getCh_ano());
        return (t-o)==1;
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
                            chapter.setWordNum(StringUtil.chineseNums(chapter.getChapter_context()));
                            //将该章节加入章节列表
                            chapters.add(chapter);
                            isFirstVolume=false;
                        }
                       isbegin=false;
                    }else {
                        chapter.setChapter_context(buffer.toString());
                        buffer = new StringBuffer();
                        chapter.setWordNum(StringUtil.chineseNums(chapter.getChapter_context()));
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
                        if(reg.contains("序")){
                            if(groupCount==2){
                                String g2 = m.group(2);
                                chapter.setChapter_name(g2);
                            }
                        }else {
                            //章节名
                            String g2 = m.group(2);
                            chapter.setCh_ano(g2==null?"0":g2);
                            if(groupCount>=3) {
                                String g3 = m.group(3);
                                chapter.setChapter_name(g3);
                            }
                        }
                    }
                }
            }else {//该行为卷，找到对应的正则
                //保存上一卷的信息
                //将当前章加入章节集合
                chapter.setChapter_context(buffer.toString());
                buffer = new StringBuffer();
                chapter.setWordNum(StringUtil.chineseNums(chapter.getChapter_context()));
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
        chapter.setWordNum(StringUtil.chineseNums(chapter.getChapter_context()));
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

    //解析卷字符串为章
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

            }else {//该行为章，找到对应的正则
                //将该章加入章集合
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
                    if(chapterReg.contains("序")){
                        if(groupCount==2){
                            String g2 = m.group(2);
                            chapter.setChapter_name(g2);
                        }
                        chapter.setCh_ano("0");
                    }else {
                        //章节名
                        String g2 = m.group(2);
                        chapter.setCh_ano(g2==null?"0":g2);
                        if(groupCount>=3) {
                            String g3 = m.group(3);
                            chapter.setChapter_name(g3);
                        }
                    }
                }
            }
        }
        //将未加入的章和卷 加入
        chapters.add(chapter);
        return chapters;
    }

    private String getReg(String line,List<String> regs){
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

        regs.add("[ 　]*(第([\\u4e00-\\u9fa50-9]{1,7})章)[ 　]*");
        regs.add("[ 　]*(第([\\u4e00-\\u9fa50-9]{1,7})章)[ 　]{0,2}(\\S{1,25})[ 　]*");

        regs.add("[ 　]*(第([\\u4e00-\\u9fa50-9]{1,7})节)[ 　]*");
        regs.add("[ 　]*(第([\\u4e00-\\u9fa50-9]{1,7})节)[ 　]{0,2}(\\S{1,25})[ 　]*");
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
