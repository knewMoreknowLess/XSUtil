package com.cs.util.xsutil;

import com.cs.util.xsutil.common.entity.Book;
import com.cs.util.xsutil.common.entity.Chapter;
import com.cs.util.xsutil.common.enums.CommonMessage;
import com.cs.util.xsutil.common.exposer.MessageExposer;
import com.cs.util.xsutil.common.util.FileUtils;
import com.cs.util.xsutil.core.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XsutilApplicationTests {
	@Autowired
	private BookService bookService;

	@Test
	public void contextLoads() {
//		String filePath = "C:\\Users\\Administrator\\Desktop\\黑暗主宰.txt";
//		String filePath = "E:\\黑暗主宰.txt";
//		String filePath = "E:\\魔法工业帝国.txt";
		String filePath = "E:\\异世御龙.txt";
		System.out.println("开始测试");
		Book book = null;
		MessageExposer<Book> exposer = bookService.readBook(filePath);
		if(CommonMessage.success.getCode().equals(exposer.getCode())){
			System.out.println("小说读取成功");
			book = exposer.getData();

			//打印小数到指定位置
			System.out.println("打印小数到指定位置");
			String basepath = "C:\\Users\\Administrator\\Desktop\\b";
			bookService.writeToText(book,basepath);
			System.out.println("成功打印小说至："+basepath+ File.separator +book.getBoox_name());

		}

		System.out.println("测试结束");
	}

	@Test
	public void test2(){
		String filePath = "E:\\黑暗主宰.txt";
		int count = FileUtils.getWordNum(filePath);
		System.out.println(count);

	}

	private int getAllChapterWord(List<Chapter> chapters){
		int a = 0;
		for (Chapter c : chapters){
			a = a + c.getWordNum();
		}
		return a;
	}

	@Test
	public void test1(){
		String s1 = "　第7章 ";
		String s2 = "　　第1章 重生异界 ";
		String s3 = "  第七章 ";
		String s4 = "  第十七章 我是大神";
		String s5 = "第十七章 我是大神,d ";
		String s6 = "第十七章 我是大神,东湖耳朵啊！！";
		String s7 = "  <<第十七章 我是大神,东湖耳朵啊！！)";
		String s8 = "  第十七章 <<我是大神,东湖耳朵啊！！>>";

		System.out.println(getReg(s1));
		System.out.println(getReg(s2));
		System.out.println(getReg(s3));
		System.out.println(getReg(s4));
		System.out.println(getReg(s5));
		System.out.println(getReg(s6));
		System.out.println(getReg(s7));
		System.out.println(getReg(s8));
		System.out.println("测试结束");

	}

	public String getReg(String line){
		List<String> regs = getRegList();
		for (String reg : regs){
			if(line.matches(reg)){
				return reg;
			}
		}
		return null;
	}

	private List<String> getRegList(){
		List<String> regs = new ArrayList<>();
		regs.add("[ 　]*(第[\\u4e00-\\u9fa50-9]{1,7}章)\\s*");
		regs.add("\\s*(第[\\u4e00-\\u9fa50-9]{1,7}章)\\s*");
		regs.add("\\s*[(（<《]{0,2}(第[\\u4e00-\\u9fa50-9]{1,7}章)\\s(\\S{1,25})\\s*");

		regs.add("\\s*(第[\\u4e00-\\u9fa50-9]{1,7}节)\\s*");
		regs.add("\\s*(第[\\u4e00-\\u9fa50-9]{1,7}节)\\s(\\S{1,25})\\s*");
		return regs;
	}

}
