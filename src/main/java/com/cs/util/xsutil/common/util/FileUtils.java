package com.cs.util.xsutil.common.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件操作工具类
 *
 * @author 张代浩
 */
public class FileUtils {

    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtend(String filename) {
        return getExtend(filename, "");
    }

    //判断包路径是否存在
    public static void dir_extis(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    //判断包路径是否存在
    public static boolean file_extis(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 获取文件扩展名
     *
     * @param filename
     * @return
     */
    public static String getExtend(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');

            if ((i > 0) && (i < (filename.length() - 1))) {
                return (filename.substring(i + 1)).toLowerCase();
            }
        }
        return defExt.toLowerCase();
    }

    /**
     * 获取文件名称[不含后缀名]
     *
     * @param
     * @return String
     */
    public static String getFilePrefix(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
    }

    /**
     * 获取文件名称[不含后缀名]
     * 不去掉文件目录的空格
     *
     * @param
     * @return String
     */
    public static String getFilePrefix2(String fileName) {
        int splitIndex = fileName.lastIndexOf(".");
        return fileName.substring(0, splitIndex);
    }

    /**
     * 文件复制
     * 方法摘要：这里一句话描述方法的用途
     *
     * @param
     * @return void
     */
    public static void copyFile(String inputFile, String outputFile) throws FileNotFoundException {
        File sFile = new File(inputFile);
        File tFile = new File(outputFile);
        FileInputStream fis = new FileInputStream(sFile);
        FileOutputStream fos = new FileOutputStream(tFile);
        int temp = 0;
        byte[] buf = new byte[10240];
        try {
            while ((temp = fis.read(buf)) != -1) {
                fos.write(buf, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取文件对象，目录不存在自动创建
     * @param filePath
     * @return
     * @throws Exception
     */
    public static File createFileByString(String filePath) throws IOException{
        File file = new File(filePath);

        //判断目标文件所在的目录是否存在
        if(!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            if(!file.getParentFile().mkdirs()) {
                System.err.println("创建目标文件所在目录失败！");
               throw new IOException("创建文件所在目录失败");
            }
        }
        if (!file.exists()){
            file.createNewFile();
        }

        return file;
    }


    /**
     * 删除指定的文件
     *
     * @param strFileName 指定绝对路径的文件名
     * @return 如果删除成功true否则false
     */
    public static boolean delete(String strFileName) {
        File fileDelete = new File(strFileName);

        if (!fileDelete.exists() || !fileDelete.isFile()) {
            return false;
        }

        return fileDelete.delete();
    }
    /**
     * 删除指定的文件
     *
     * @param fileDelete 指定绝对路径的文件名
     * @return 如果删除成功true否则false
     */
    public static boolean delete(File fileDelete) {
        if (!fileDelete.exists() || !fileDelete.isFile()) {
            return false;
        }
        return fileDelete.delete();
    }
    /***删除某个目录下的所有文件*/
    public static void deleteDirAndFile(File file){
        if (file.isDirectory()){
            File[] files=file.listFiles();
            for (File f:files) {
                if (f.isDirectory()) deleteDirAndFile(f);
                else f.delete();
            }
        }
        file.delete();
    }


    public static String getFileMd5(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        byte[] uploadBytes = file.getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(uploadBytes);
        String strmd5 = new BigInteger(1, digest).toString(16);
        return strmd5;
    }
    public static String rename(String filepath,String newfilename){
        File oldfile=new File(filepath);
        if (oldfile.isDirectory()||!oldfile.exists()){
            logger.error(filepath+"有误！");
            return null;
        }else {
            String newfilepath=filepath.substring(0,filepath.lastIndexOf("\\")+1)+newfilename+"."+filepath.substring(filepath.lastIndexOf(".")+1);
            oldfile.renameTo(new File(newfilepath));
            return newfilepath;
        }
    }

    /**
     * 文件更名
     * @param filepath
     * @param newfilepath
     * @return
     */
    public static boolean renameTo(String filepath,String newfilepath){
        File oldfile = null;
        File newfile = null;
        try {
            oldfile = createFileByString(filepath);
            newfile = new File(newfilepath);
            if (newfile.exists()){
                delete(oldfile);
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return oldfile.renameTo(newfile);
    }

    /**
     * 文件拷贝
     * @param s
     * @param t
     */
    public static boolean fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fi!=null){
                    fi.close();
                }
                if(in !=null){
                    in.close();
                }
                if(fo != null) {
                    fo.close();
                }
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getFileMd5(byte[] bytes) throws IOException, NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(bytes);
        String strmd5 = new BigInteger(1, digest).toString(16);
        return strmd5;
    }

    public static String getMd5ByFile(String path){
        File file = new File(path);
        if(!file.exists()){
            return null;
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            return DigestUtils.md5DigestAsHex(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public static String getMd5ByFile(File file){
//        String value = "";
//        FileInputStream in = null;
//        try {
//            in = new FileInputStream(file);
//            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
//            MessageDigest md5 = MessageDigest.getInstance("MD5");
//            md5.update(byteBuffer);
//            BigInteger bi = new BigInteger(1, md5.digest());
//            value = bi.toString(16);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return value;
//        } finally {
//            if(in != null) {
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        return value;
//    }

    /**
     * 字符串写入文件输出流
     * @param outputStream
     * @param str
     * @throws IOException
     */
    public static void writeStringtoOutstream(OutputStream outputStream,String str) throws IOException {
        if(str==null || "".equals(str)){
            return;
        }
        byte[] bytes = str.getBytes();
        outputStream.write(bytes);
    }
    /**
     * 字符串写入文件输出流
     * @param outputStream
     * @param str
     * @throws IOException
     */
    public static void writeStringtoOutstream(OutputStream outputStream,String str,String code) throws IOException {
        if(str==null || "".equals(str)){
            return;
        }
        byte[] bytes = str.getBytes(code);
        outputStream.write(bytes);
    }


    public static int getWordNum(String str){
        int count=0;
        String regEx = "[\u4e00-\u9fa5a-zA-Z]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while(m.find())
        {
            count ++;
        }
        return count;
    }

    /**
     * 文件上传
     *
     * @param inputStream
     * @param filename
     * @return
     * @throws IOException
     */
    public static String uploadfileDmt(InputStream inputStream, String filename, String gcPath) throws IOException {
        //String diskPath = PropertiesUtils.sysConfig_instance.get("diskPath");
        File newfile = null;
        OutputStream fileOut = null;
        try {
            String tempPath = "";
            if (!tempPath.endsWith("/")) {
                tempPath += "/";
            }
            String path = tempPath + filename;
            //获取路劲文件对象
            newfile = createFileByString(gcPath+path);//new File(gcPath + path);

            fileOut = new FileOutputStream(newfile);
            //数据流写入文件
            IOUtils.copy(inputStream, fileOut);

        }catch (IOException e){
            throw e;
        }finally {
            if (inputStream!=null){
                inputStream.close();
            }
            if (fileOut!=null){
                fileOut.close();
            }
        }
        return newfile.getAbsolutePath();
    }


    public static void Copy(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

    public static String getFileParentDir(String filePath)throws FileNotFoundException{
        File file = new File(filePath);
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        return file.getParent();
    }

    /**
     * 这个代码不要删除  xiezh
     * @param commands
     */
    public static void processOracles(String[] commands){
        try {
            Runtime run = Runtime.getRuntime();
            System.out.println(commands.toString());
            Process process = run.exec(commands);
            final InputStream is1 = process.getInputStream();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                    String info;
                    try {
                        while ((info=br.readLine()) != null){
                            System.out.println("info: "+info);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start(); // 启动单独的线程来清空process.getInputStream()的缓冲区
            InputStream is2 = process.getErrorStream();
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2,"GB2312"));
            // 保存输出结果
            StringBuilder buf = new StringBuilder();
            String line = null;
            int i=0;
            while ((line = br2.readLine()) != null){
                // 循环等待ffmpeg进程结束
                System.out.println("info: " +line);
                buf.append(line);
            }
            try {
                if(buf.toString().contains("ORA-")){//&&buf.toString().contains("EXP-")
                    process.destroy();
                }else{
                    i=process.waitFor();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getSysName(){
        Properties props=System.getProperties(); //获得系统属性集
        return props.getProperty("os.name");
    }

    public static boolean isWinSys(){
        if(getSysName().contains("Windows")){
            return true;
        }else
            return false;
    }

    //时长格式:"00:00:10.68" 转 秒
    public static int getTimelen(String timelen){
        int min=0;
        String strs[] = timelen.split(":");
        if (strs[0].compareTo("0") > 0) {
            min+=Integer.valueOf(strs[0])*60*60;//秒
        }
        if(strs[1].compareTo("0")>0){
            min+=Integer.valueOf(strs[1])*60;
        }
        if(strs[2].compareTo("0")>0){
            min+=Math.round(Float.valueOf(strs[2]));
        }
        return min;
    }

    //移除（）括号和[]中括号中的内容
    public static String replaceInfo(String info){
        String result = "";
        result = info.replaceAll("\\(.*?\\)","");
        result = result.replaceAll("\\[.*?\\]","");
        return result;
    }

    //获取文件所有字符串
    public static String getFileString(File file) throws Exception {
        FileInputStream in = null;
        InputStreamReader reader = null;
        StringBuffer result = new StringBuffer();
        BufferedReader br = null;
        String codeFormat = FileUtils.getFilecharset(file);
        try {
            in = new FileInputStream(file);
            reader = new InputStreamReader(in,codeFormat); // 建立一个输入流对象reader
            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            while ((line=br.readLine()) != null) {
                result = result.append("\r\n").append(line); // 一次读入一行数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(br!=null){
                br.close();
            }
            if (reader!=null){
                reader.close();
            }
            if (in !=null){
                in.close();
            }
        }
        return result.toString();
    }

    //判断编码格式方法
    public static  String getFilecharset(File sourceFile) {
        String charset = "GBK";
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1) {
                return charset; //文件编码为 ANSI
            } else if (first3Bytes[0] == (byte) 0xFF
                    && first3Bytes[1] == (byte) 0xFE) {
                charset = "UTF-16LE"; //文件编码为 Unicode
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = "UTF-16BE"; //文件编码为 Unicode big endian
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF-8"; //文件编码为 UTF-8
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80
                            // - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return charset;
    }

    /**
     * 返回单个字符串，若匹配到多个的话就返回第一个，方法与getSubUtil一样
     * @param soap
     * @param rgex
     * @return
     */
    public static String getSubUtilSimple(String soap,String rgex){
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while(m.find()){
            return m.group(0);
        }
        return "";
    }
}
