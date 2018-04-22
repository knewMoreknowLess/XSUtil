package com.cs.util.xsutil.common.util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;


public class CsUtil {

	private final static String[] agent = { "Android", "iPhone", "iPod","iPad", "Windows Phone", "MQQBrowser" }; //定义移动端请求的所有可能类型
	public static InputStream getStringStream(String sInputString)throws Exception{  
		
		if(sInputString != null && !sInputString.trim().equals("")){  
			
			ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());  
		  
			return tInputStringStream;   
		}  
		return null;  
		  
	}  
	public static String getStreamString(InputStream tInputStream) throws IOException{
		if (tInputStream != null){  
			
			BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream));
			StringBuffer tStringBuffer = new StringBuffer();  
			  
			String sTempOneLine = tBufferedReader.readLine(); 
			if(sTempOneLine != null){
				tStringBuffer.append(sTempOneLine);
			}
			while ((sTempOneLine = tBufferedReader.readLine())  != null){  
				tStringBuffer.append("\n" + sTempOneLine);
			}
			return tStringBuffer.toString(); 
			
		}
		return null;
	}
	
	public static String getUniqName(String attachmentName){
		String strs[] = attachmentName.split("::");
		if(strs.length > 1){
			int i = Integer.parseInt(strs[0])+1;
			return i+"::"+strs[1];
		}else {
			return 2+"::"+strs[0];
		}
	}


	/**
	 * 判断User-Agent 是不是来自于手机
	 * @param ua
	 * @return
	 */
	public static boolean checkAgentIsMobile(String ua) {
		boolean flag = false;
		if (!ua.contains("Windows NT") || (ua.contains("Windows NT") && ua.contains("compatible; MSIE 9.0;"))) {
// 排除 苹果桌面系统
			if (!ua.contains("Windows NT") && !ua.contains("Macintosh")) {
				for (String item : agent) {
					if (ua.contains(item)) {
						flag = true;
						break;
					}
				}
			}
		}
		return flag;
	}

	/** @param instreams 二进制流
	 * @param imgPath 图片的保存路径
	 * @param imgName 图片的名称
	 * @return
	 *      1：保存正常
	 *      0：保存失败
	 */
	public static int saveToImgByInputStream(InputStream instreams,String imgPath,String imgName){

		int stateInt = 1;
		if(instreams != null){
			try {
				File file=new File(imgPath+imgName);//可以是任何图片格式.jpg,.png等
				FileOutputStream fos=new FileOutputStream(file);

				byte[] b = new byte[1024];
				int nRead = 0;
				while ((nRead = instreams.read(b)) != -1) {
					fos.write(b, 0, nRead);
				}
				fos.flush();
				fos.close();
			} catch (Exception e) {
				stateInt = 0;
				e.printStackTrace();
			} finally {
				try {
					instreams.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return stateInt;
	}

	//时间戳转日期格式字符串
	public static String getStringDate(Date date) {
		if(date==null){
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	//通过value找key
	public static String findMapKey(Map map, String v){
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			String key = (String) it.next();
			String value = (String) map.get(key);
			if(value.equals(v)){
				return key;
			}
		}
		return null;
	}
}
