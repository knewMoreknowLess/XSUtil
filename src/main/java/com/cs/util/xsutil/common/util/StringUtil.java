package com.cs.util.xsutil.common.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
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
}
