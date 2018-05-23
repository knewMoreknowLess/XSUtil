package com.cs.util.xsutil.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by xzh on 2016/7/30.
 */
public class StringUtil {

    public static List<String> stringToList(String str, String cutBy) {
        return Arrays.asList(str.split(cutBy));
    }

    /**
     * 首字母小写
     */
    public static String firstToLowercase(String value) {
        String frist = value.substring(0, 1);
        return value.replaceFirst(frist, frist.toLowerCase());
    }

    /**
     * 判断第二个字符是否是大写
     */
    public static boolean secendIsBig(String value) {
        String frist = value.substring(1, 2);
        return frist.equals(frist.toUpperCase());
    }


    /**
     * 判断字符串是否包含h5标签
     *
     * @param str
     * @return
     */
    public static Boolean haveH5(String str) {
        String pattern = ".*<.*>.*";
        if (isEmpty(str)) {
            return false;
        }
        return Pattern.matches(pattern, str);
    }

    public static Boolean haveH5(List<String> list) {
        if (CollectionUtil.isNotBlank(list)) {
            for (String s : list) {
                if (haveH5(s)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 元转换成分
     *
     * @param amount
     * @return
     */
    public static String getMoney(String amount) {
        if (amount == null) {
            return "";
        }
        // 金额转化为分为单位
        String currency = amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0L;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return amLong.toString();
    }

    public static void main(String[] args) {
        System.out.println(getMoney("1.13"));
    }

    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";



    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }

    public static String two(double f) {//大于0会有一个+号
        BigDecimal b = new BigDecimal(f);
        double v = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        if (v > 0) {
            return String.valueOf("+" + v);
        }
        return String.valueOf(v);
    }

    public static double twoDouble(double f) {
        BigDecimal b = new BigDecimal(f);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double toDouble(int newScale, double f) {
        BigDecimal b = new BigDecimal(f);
        return b.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String toPercent(double rate) {
        rate=twoDouble(rate);
        NumberFormat nFromat = NumberFormat.getPercentInstance();
        return nFromat.format(rate);
    }


    /**
     * before:[
     * after: ]
     * 例子:   [3] 获取3
     *
     * @return
     */
    public static String getBetWeen(String resoure, String before, String after) {
        int a = resoure.indexOf(before);
        int b = resoure.indexOf(after, a);
        return resoure.substring(a + before.length(), b);
    }

    public static String unicodeUrl(String url) throws UnsupportedEncodingException {

    return java.net.URLDecoder.decode(url,"utf-8");
    }

    /**
     * 提取数字
     * @param str
     */
    public static String getNum(String str) {
        String temp = "";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                temp += str.charAt(i);
            }
        }
        return temp;
    }

    public static boolean isBlank(String src){
        if(src == null || "".equals(src)){
            return true;
        }else
            return false;
    }

    public static int chineseNums(String str) {
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
    public static int chineseNumber2Int(String chineseNumber){
        int result = 0;
        try {
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
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 将字符（阿拉伯或者中文）数字转为int数值
     * @param ch
     * @return
     */
    public static int chToint(String ch) throws Exception{
        int num=0;
        try {
            num = Integer.parseInt(ch);
        }catch (Exception e){
            num = chineseNumber2Int(ch);
        }
        return num;
    }

    public static String getRandomStr(int length){
        //产生随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //循环length次
        for(int i=0; i<length; i++){
            //产生0-2个随机数，既与a-z，A-Z，0-9三种可能
            int number=random.nextInt(3);
            long result=0;
            switch(number){
                //如果number产生的是数字0；
                case 0:
                    //产生A-Z的ASCII码
                    result=Math.round(Math.random()*25+65);
                    //将ASCII码转换成字符
                    sb.append(String.valueOf((char)result));
                    break;
                case 1:
                    //产生a-z的ASCII码
                    result=Math.round(Math.random()*25+97);
                    sb.append(String.valueOf((char)result));
                    break;
                case 2:
                    //产生0-9的数字
                    sb.append(String.valueOf
                            (new Random().nextInt(10)));
                    break;
            }
        }
        return sb.toString();
    }
}
