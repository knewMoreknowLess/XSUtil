package com.cs.util.xsutil.common.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @描述：请完善类的描述
 * @作者：Administrator
 * @时间：2017/9/27
 */
public class HttpRequest {

    private static String filePath = "D:\\fileDownLoad";
    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求并生成图像
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        InputStream in = null;
        String result = "error";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");

            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            String contentType = conn.getContentType();
            in = conn.getInputStream();
            result = getResult(in,contentType);

        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{

            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 通过相应的内容属性，判断是生成文件还是直接返回字符串
     * @param inputStream
     * @param contentType
     * @return 返回结果字符串
     */
    public static String getResult(InputStream inputStream,String contentType){
        String result = "error";
        if(StringUtil.isNotEmpty(contentType)){
            //判断需要生成文件的内容属性
            if(contentType.contains("image")||contentType.contains("msword")||contentType.contains("octet-stream")||contentType.contains("plain")){
                String fileFormat = getFileFormat(contentType);
                result = getFileResult(inputStream,null,fileFormat);
            }else
               result = getStrResult(inputStream);
        }
        return result;
    }

    public static String getStrResult(InputStream inputStream){
        StringBuilder result = new StringBuilder();
        try {
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                result = result.append(new String(buf));
            }
        }catch (Exception e){
            result = new StringBuilder("error");
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 通过连接的输出流，生成对应的文件，并保存在对应的地方
     * @param inputStream
     * @param fileName
     * @return 成功返回true
     */
    public static String getFileResult(InputStream inputStream,String fileName,String fileFormat){
        String name = "error";
        OutputStream outputStream = null;
        if(fileName == null){//不给默认文件名，自动生成带时间戳文件名
            name = System.currentTimeMillis() + "." + fileFormat;
            fileName = filePath + File.separator + name;
        }else
            name = fileName + "." + fileFormat;
            fileName = filePath + File.separator + name;
        try {
            File file = new File(fileName);
            //判断目标文件所在的目录是否存在
            if(!file.getParentFile().exists()) {
                //如果目标文件所在的目录不存在，则创建父目录
                if(!file.getParentFile().mkdirs()) {
                    System.err.println("创建目标文件所在目录失败！");
                    return "error";
                }
            }
            LOG.info(fileName);
            if (!file.exists()){
                file.createNewFile();
            }
            outputStream = new FileOutputStream(file);
            int len = 0;
            byte[] buf = new byte[1024];
            LOG.info("开始下载文件。。。");
            while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                outputStream.write(buf, 0, len);
            }
            LOG.info("文件下载完成！");
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return name;
    }

    public static String getFileFormat(String contentType){
        if(contentType.contains("jpeg"))
            return  "jpg";
        else if(contentType.contains("gif"))
            return  "gif";
        else if(contentType.contains("png"))
            return  "png";
        else if(contentType.contains("msword"))
            return  "docx";
        else
            return  "txt";
    }

}