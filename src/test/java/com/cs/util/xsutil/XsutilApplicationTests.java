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
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XsutilApplicationTests {
	@Autowired
	private BookService bookService;

	@Test
	public void contextLoads() {
//		String filePath = "C:\\Users\\Administrator\\Desktop\\黑暗主宰.txt";
		String filePath = "E:\\黑暗主宰.txt";
//		String filePath = "E:\\星灿.txt";
//		String filePath = "E:\\魔法工业帝国.txt";
//		String filePath = "E:\\异世御龙.txt";
		System.out.println("开始测试");
		Book book = null;
		MessageExposer<Book> exposer = bookService.readBook(filePath);
		if(CommonMessage.success.getCode().equals(exposer.getCode())){
			System.out.println("小说读取成功");
			book = exposer.getData();

			//去除小说重复
			System.out.println("开始去除重复章节。。。");
			List<String> removeInfo = bookService.duplicateRemoval(book);
			for (String info : removeInfo){
				System.out.println(info);
			}
			System.out.println("重复章节去除完毕！");

			System.out.println("开始检查章节排序情况。。。");
			List<Chapter> sortInfo = bookService.charpterSortCheck(book);
			System.out.println("开始检查重新排序 异常章节。。。");
			bookService.sort(book,sortInfo);

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
		String c1 = "aa第二十五章";
		String reg = "aa(第(\\S*)章)";
		Pattern p = Pattern.compile(reg);//获取正则表达式中的分组，每一组小括号为一组
		Matcher m = p.matcher(c1);//进行匹配
		if(m.find()){
			int groupCount = m.groupCount();
			//章节序号
			for (int i = 0;i < groupCount+1;i++){
				System.out.println(m.group(i));
			}
		}

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

	@Test
	public void test3(){
		String c1 = "第二十五章";
		String c2 = "第二十六章";

		String c3 = "第25章";
		String c4 = "第26章";
		String c5 = "第26章aaaaaa";
//		System.out.println(c2.compareTo(c1));
//
//		System.out.println(c4.compareTo(c3));

		System.out.println(chineseNums(c2));
		System.out.println(chineseNums(c4));
		System.out.println(chineseNums(c5));

	}

	@Test
	public void test4(){
		String c1 = "二十五";
		String c2 = "二十六";
		String c3 = "一万一千零二十六";
		String c4 = "一万一千一百二十六";

		String c5 = "九千零二十六";
		String c6 = "九百二十六";
		String c7 = "的的达到";
		System.out.println(chineseNumber2Int(c1));

		System.out.println(chineseNumber2Int(c2));
		System.out.println(chineseNumber2Int(c3));
		System.out.println(chineseNumber2Int(c4));
		System.out.println(chineseNumber2Int(c5));
		System.out.println(chineseNumber2Int(c6));
		System.out.println(chineseNumber2Int(c7));

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



	private static int chineseNums(String str) {
		byte b[] = str.getBytes();
		int byteLength = b.length;
		int strLength = str.length();
		return (byteLength - strLength) / 2;
	}


	/**
	 * 中文數字转阿拉伯数组【十万九千零六十  --> 109060】
	 * @author 雪见烟寒
	 * @param chineseNumber
	 * @return
	 */
	@SuppressWarnings("unused")
	private static int chineseNumber2Int(String chineseNumber){
		int result = 0;
		int temp = 1;//存放一个单位的数字如：十万
		int count = 0;//判断是否有chArr
		char[] cnArr = new char[]{'一','二','三','四','五','六','七','八','九'};
		char[] chArr = new char[]{'十','百','千','万','亿'};
		for (int i = 0; i < chineseNumber.length(); i++) {
			boolean b = true;//判断是否是chArr
			char c = chineseNumber.charAt(i);
			for (int j = 0; j < cnArr.length; j++) {//非单位，即数字
				if (c == cnArr[j]) {
					if(0 != count){//添加下一个单位之前，先把上一个单位值添加到结果中
						result += temp;
						temp = 1;
						count = 0;
					}
					// 下标+1，就是对应的值
					temp = j + 1;
					b = false;
					break;
				}
			}
			if(b){//单位{'十','百','千','万','亿'}
				for (int j = 0; j < chArr.length; j++) {
					if (c == chArr[j]) {
						switch (j) {
							case 0:
								temp *= 10;
								break;
							case 1:
								temp *= 100;
								break;
							case 2:
								temp *= 1000;
								break;
							case 3:
								temp *= 10000;
								break;
							case 4:
								temp *= 100000000;
								break;
							default:
								break;
						}
						count++;
					}
				}
			}
			if (i == chineseNumber.length() - 1) {//遍历到最后一个字符
				result += temp;
			}
		}
		return result;
	}
}
