package com.rxtx.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sms.util.Convert;

/***
 * 发送pdu格式短信工具
 */
public class SendUtil {
	static String regEx = "[\u4e00-\u9fa5]";//中文
	static Pattern pat = Pattern.compile(regEx);
	public static void main(String[] args) throws Exception {
		String text =encodeHex("test");
		System.out.print(text);
	}
	/**
	 * 使用PDU方式发送消息 
	 * @param phone
	 * @param msg
	 * @return AT指令
	 */
	public static final String PDUEncoder(String phone,String msg){
		String sendcontent ="";
		String len="";
		if(isContainsChinese(msg)){ //短信包含中文
			sendcontent= "0011000D91" + reverserNumber(phone) + "000801" + PDUUSC2ContentEncoder(msg) ;
		}else{
			sendcontent= "0011000D91" + reverserNumber(phone) + "000001" + PDU7BitContentEncoder(msg) ;
		}
		len = (sendcontent.length() - Integer.parseInt(sendcontent.substring(0, 2), 16) * 2 - 2) / 2 +"";  //计算长度
		if(len.length()<2) len="0"+len;
//		return "AT+CMGS=" + len + "\r" + sendcontent + String.valueOf(26);  //26 Ctrl+Z ascii码
		return len+","+sendcontent;
	}
	/**
	 * 获取短信内容的字节数
	 * @param txt
	 * @return
	 */
	private static String getLength(String txt)
	{
		int i = 0;
		String s = "";
		i = txt.length() * 2;
		i += 15;
		s = i+"";
		return s;
	}
	/**
	 * 将手机号码转换为内存编码
	 * @param phone
	 * @return
	 */
	private static String reverserNumber(String phone)
	{
		String str = "";
		//检查手机号码是否按照标准格式写，如果不是则补上
		if (phone.substring(0, 2) != "86")
		{
			phone = "86"+phone;
		}
		char[] c = getChar(phone);
		for (int i = 0; i <= c.length - 2; i += 2)
		{
			str += c[i + 1]+"" + c[i];
		}
		return str;
	}

	private static char[] getChar(String phone)
	{
		if (phone.length() % 2 == 0)
		{
			return phone.toCharArray();
		}
		else
		{
			return (phone + "F").toCharArray();
		}
	}

	private static boolean isContainsChinese(String str)
	{
		Matcher matcher = pat.matcher(str);
		boolean flg = false;
		if (matcher.find())    {
			flg = true;
		}
		return flg;
	}
	/**
	 * 使用Sms 的 SendSms()方法发送短信时,调用此方法将其短信内容转换成十六进制
	 * @param msg 短信内容
	 * @return 转换后的十六进制短信
	 */
	public static final String encodeHex(String msg) {
		byte[] bytes = null;
		try {
			bytes = msg.getBytes("GBK");
		} catch (java.io.UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuffer buff = new StringBuffer(bytes.length * 4);
		String b = "";
		char a;
		int n = 0;
		int m = 0;
		for (int i = 0; i < bytes.length; i++) {
			try{b = Integer.toHexString(bytes[i]);}catch (Exception e) {}
			if (bytes[i] > 0) {
				buff.append("00");
				buff.append(b);
				n = n + 1;
			} else {
				a = msg.charAt((i - n) / 2 + n);
				m = a;
				try{b = Integer.toHexString(m);}catch (Exception e) {}
				buff.append(b.substring(0, 4));

				i = i + 1;
			}
		}
		return buff.toString();
	}
	/**
	 * 中文短信内容USC2编码
	 * @param userData
	 * @return
	 */
	private static String PDUUSC2ContentEncoder(String userData){
		String contentEncoding = encodeHex(userData);
		String length = Integer.toHexString(contentEncoding.length() / 2);//把value的值除以2并转化为十六进制字符串
		while(length.length()<2) length="0"+length;
		return length+contentEncoding.toUpperCase();
		
	}
	/**
	 * 英文短信内容7Bit编码
	 * @param userData
	 */
	public static String PDU7BitContentEncoder(String userData) {
		String result = "";
		String length = Integer.toHexString(userData.length());
		while(length.length()<2) length="0"+length;//7bit编码 用户数据长度：源字符串长度

		byte[] Bytes = userData.getBytes();

		String temp = "";                                     //存储中间字符串 二进制串
		String tmp;
		for (int i = userData.length(); i > 0; i--) {
			tmp = Convert.conver2HexStr(Bytes[i-1]);
			while (tmp.length() < 7) {
				tmp = "0" + tmp;
			}
			temp += tmp;
		}
		for (int i = temp.length() ; i > 0; i -= 8) {                   //每8位取位一个字符 即完成编码 同时高位取为低位
			if (i > 8) {
				String aa = Integer.toHexString(Integer.parseInt(temp.substring(i-8, i), 2));
				while(aa.length()<2) aa="0"+aa;
				result += aa;
			} else {
				String aa = Integer.toHexString(Integer.parseInt(temp.substring(0, i), 2));
				while(aa.length()<2) aa="0"+aa;
				result += aa;
			}
		}
		return length+result.toUpperCase();
	}
}