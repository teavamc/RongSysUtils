package com.sms.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * 说明：常用工具
 */
public class Tools {

	/**
	 * 随机生成六位数验证码 
	 * @return
	 */
	public static int getRandomNum(){
		Random r = new Random();
		return r.nextInt(900000)+100000;//(Math.random()*(999999-100000)+100000)
	}

	/**
	 * 检测字符串是否不为空(null,"","null")
	 * @param s
	 * @return 不为空则返回true，否则返回false
	 */
	public static boolean notEmpty(String s){
		return s!=null && !"".equals(s) && !"null".equals(s);
	}

	/**
	 * 检测字符串是否为空(null,"","null")
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s){
		return s==null || "".equals(s) || "null".equals(s);
	}

	/**
	 * 字符串转换为字符串数组
	 * @param str 字符串
	 * @param splitRegex 分隔符
	 * @return
	 */
	public static String[] str2StrArray(String str,String splitRegex){
		if(isEmpty(str)){
			return null;
		}
		return str.split(splitRegex);
	}

	/**
	 * 用默认的分隔符(,)将字符串转换为字符串数组
	 * @param str	字符串
	 * @return
	 */
	public static String[] str2StrArray(String str){
		return str2StrArray(str,",\\s*");
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 * @param date
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date){
		return date2Str(date,"yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
	 * @param date
	 * @return
	 */
	public static Date str2Date(String date){
		if(notEmpty(date)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return new Date();
		}else{
			return null;
		}
	}

	/**
	 * 按照参数format的格式，日期转字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2Str(Date date,String format){
		if(date!=null){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		}else{
			return "";
		}
	}

	/**
	 * 把时间根据时、分、秒转换为时间段
	 * @param StrDate
	 */
	public static String getTimes(String StrDate){
		String resultTimes = "";

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date now;

		try {
			now = new Date();
			java.util.Date date=df.parse(StrDate);
			long times = now.getTime()-date.getTime();
			long day  =  times/(24*60*60*1000);
			long hour = (times/(60*60*1000)-day*24);
			long min  = ((times/(60*1000))-day*24*60-hour*60);
			long sec  = (times/1000-day*24*60*60-hour*60*60-min*60);

			StringBuffer sb = new StringBuffer();
			//sb.append("发表于：");
			if(hour>0 ){
				sb.append(hour+"小时前");
			} else if(min>0){
				sb.append(min+"分钟前");
			} else{
				sb.append(sec+"秒前");
			}

			resultTimes = sb.toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return resultTimes;
	}

	/**
	 * 写txt里的单行内容
	 * @param filePath  文件路径
	 * @param content  写入的内容
	 */
	public static void writeFile(String fileP,String content){
		String filePath = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""))+"../../";	//项目路径
		filePath = (filePath.trim() + fileP.trim()).substring(6).trim();
		if(filePath.indexOf(":") != 1){
			filePath = File.separator + filePath;
		}
		try {
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(filePath),"utf-8");      
			BufferedWriter writer=new BufferedWriter(write);          
			writer.write(content);      
			writer.close(); 


		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 验证邮箱
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email){
		boolean flag = false;
		try{
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证手机号码
	 * @param mobiles
	 * @return
	 */
	public static boolean checkMobileNumber(String mobileNumber){
		boolean flag = false;
		try{
			Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
			Matcher matcher = regex.matcher(mobileNumber);
			flag = matcher.matches();
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}

	/**
	 * 根据Key读取Properties中Value
	 * @Title: GetValueByKey 
	 * @param @param fileP
	 * @param @param key
	 * @return String
	 */
	public static String GetValueByKey(String fileP, String key) {
			    	  String filePath = new File("").getAbsolutePath() + File.separator+  fileP;
		Properties pps = new Properties();
		try {
			InputStream in = new BufferedInputStream (new FileInputStream(filePath));  
			pps.load(in);
			String value = pps.getProperty(key);
			//	             System.out.println(key + " = " + value);
			return new String(value.getBytes("ISO-8859-1"),"UTF-8");

		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读取Properties的全部信息
	 * @Title: GetAllProperties 
	 * @param @param fileP
	 * @param @throws IOException 
	 * @return void
	 */
	public static  Map<String,String> GetAllProperties(String fileP) throws IOException {
		Properties pps = new Properties();
		String filePath = new File("").getAbsolutePath() + File.separator+  fileP;
		InputStream in = new BufferedInputStream(new FileInputStream(filePath));
		pps.load(in);
		Enumeration en = pps.propertyNames(); //得到配置文件的名字
		Map<String,String> map = new HashMap<String,String>();
		while(en.hasMoreElements()) {
			String strKey = (String) en.nextElement();
			String strValue = pps.getProperty(strKey);
			map.put(strKey, strValue.trim());
			//	             System.out.println(strKey + "=" + strValue);
		}
		return map;
	}
	/**  
	 * 程序中访问http数据接口  
	 */  
	public static String getURLContent(String urlStr) {             
		/** 网络的url地址 */      
		URL url = null;            
		/** http连接 */  
		HttpURLConnection httpConn = null;          
		/**//** 输入流 */ 
		BufferedReader in = null; 
		StringBuffer sb = new StringBuffer(); 
		try{   
			url = new URL(urlStr);   
			in = new BufferedReader( new InputStreamReader(url.openStream(),"UTF-8") ); 
			String str = null;  
			while((str = in.readLine()) != null) {  
				sb.append( str );   
			}   
		} catch (Exception ex) { 

		} finally{  
			try{           
				if(in!=null) {
					in.close();   
				}   
			}catch(IOException ex) {    
			}   
		}   
		String result =sb.toString();   
		System.out.println(result);   
		return result;  
	}
	/**
	 * post方式请求http服务
	 * @param urlStr
	 * @param params   name=yxd&age=25
	 * @return
	 * @throws Exception
	 */
	public static String getURLByPost(String urlStr,String params)throws Exception{
		URL url=new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
		printWriter.write(params);
		printWriter.flush();        
		BufferedReader in = null; 
		StringBuilder sb = new StringBuilder(); 
		try{   
			in = new BufferedReader( new InputStreamReader(conn.getInputStream(),"UTF-8") ); 
			String str = null;  
			while((str = in.readLine()) != null) {  
				sb.append( str );   
			}   
		} catch (Exception ex) { 
			throw ex; 
		} finally{  
			try{ 
				conn.disconnect();
				if(in!=null){
					in.close();
				}
				if(printWriter!=null){
					printWriter.close();
				}
			}catch(IOException ex) {   
				throw ex; 
			}   
		}   
		return sb.toString(); 
	}

	public static void main(String[] args) {
		System.out.println(getRandomNum());
	}
}
