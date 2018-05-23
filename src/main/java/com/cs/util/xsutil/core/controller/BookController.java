package com.cs.util.xsutil.core.controller;

import com.cs.util.xsutil.common.base.BaseController;
import com.cs.util.xsutil.common.enums.EnumMessage;
import com.cs.util.xsutil.common.exposer.MessageExposer;
import com.cs.util.xsutil.core.entity.Book;
import com.cs.util.xsutil.core.entity.Consumer;
import com.cs.util.xsutil.core.service.BookService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/book/")
public class BookController extends BaseController {

    @Autowired
    private BookService bookService;


    @Value("${sys_config.upload_path}")
    private String upload_path;

    @ApiOperation(value="小说上传接口", notes="小说上传接口")
    @RequestMapping(value = "upload",method = {RequestMethod.GET,RequestMethod.POST})
    public MessageExposer upload(MultipartFile file,@ModelAttribute("consumer") Consumer consumer){
        Map map = new HashMap();
        MessageExposer exposer = bookService.uploadBook(file,file.getOriginalFilename(),upload_path);

        if("success".equals(exposer.getCode())){
            Book book = (Book) exposer.getData();
            map.put("book",book);
            //读取图书信息
            MessageExposer m1 = bookService.readBook(book);
            if("success".equals(m1.getCode())){
                book = (Book)m1.getData();
                //书本去重
                List<String> dInfo = bookService.duplicateRemoval(book);
                map.put("removeInfo",dInfo);
                //书本排序
                List<String> sortInfo = bookService.bookSort(book);
                map.put("sortInfo",sortInfo);
                //写出书本
                MessageExposer m2 = bookService.writeToText(book);
                //写入成功更换校对后的书的书名为MD5名
                if("success".equals(m2.getCode())){
                   bookService.updateBookPath(book);
                }
                exposer = m2;
            }else {
                exposer = m1;
            }
            //保存书本信息
            Book b = bookService.save(book);
            //移除书本所有字节
            bookService.bookInfoRemove(book);
            map.put("book",book);
        }
        exposer.setData(map);
        return exposer;
    }

    @ApiOperation(value="小说下载接口", notes="小说下载接口")
    @RequestMapping(value = "download",method = {RequestMethod.GET,RequestMethod.POST})
    public void downloadLocal(HttpServletResponse response,@ModelAttribute("consumer") Consumer consumer,String id) throws FileNotFoundException {
        Book book = bookService.getById(id);
        // 下载本地文件
        // 读到流中
        InputStream inStream = new FileInputStream(book.getBook_abPath());// 文件的存放路径
        // 设置输出的格式
        String book_name = book.getBook_name()+"."+book.getBook_format();
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("bin");
        try {
            response.addHeader("Content-Disposition", "attachment; filename=" + new String(book_name.getBytes(), "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 循环取出流中的数据
        byte[] b = new byte[100];
        int len;
        try {
            while ((len = inStream.read(b)) > 0)
                response.getOutputStream().write(b, 0, len);
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
