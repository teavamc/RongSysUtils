package com.socket.socket;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import com.socket.entity.SocketInfo;
import com.socket.socket.SocketServer;
import com.socket.util.Convert;
import com.socket.util.Logger;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
	String address = "";//连接的客户端地址
//	String myimei= null;//已连接的IMEI
	final static String GBK = "GBK";
	// 消息的中止判断符
	public String EndChar = "cc";
	// 消息的开始判断符
	public String BeginChar = "aa";
	protected Logger infologger = Logger.getLogger("info");//正常日志
	protected Logger debuglogger = Logger.getLogger("debug");//跟踪日志
	protected Logger errorlogger = Logger.getLogger("error");//错误日志
	protected SenderData mydata ;
	protected int senderlenth = 510;
	long starttime;
	ByteBuf encoded;

	public NettyServerHandler(SenderData mydata){
		this.mydata = mydata;
	}
	/*
	 * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
	 * */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		debuglogger.info("跟踪日志  记录：完成连接: "+ctx.channel().remoteAddress());
		SocketServer.socketChannelList.add(ctx);
//		infologger.info("socketChannelList num："+SocketServer.socketChannelList.size());
		SocketInfo si = new SocketInfo();
		si.setChannelAddress(ctx.channel().remoteAddress().toString());
		SocketServer.clients.add(si);
		super.channelActive(ctx);
	}
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf result = (ByteBuf) msg;
		byte[] content = new byte[result.readableBytes()];
		result.readBytes(content);
		result.release();//释放
//		ReferenceCountUtil.release(result);
		address = ctx.channel().remoteAddress().toString();
		SocketServer.executorService.execute(new Runnable(){
			@Override
			public void run() {
				String acceptData="";
				if(content.length>0){
					if(Convert.byteToHexString(content[0]).equals(BeginChar)){
						String byteType = (int)content[1]+"";
//						System.out.println("终端握手类型："+byteType);
						int byteLength = (int)content[2]+(int)content[3];
						String byteOrder = (int)content[4]+"";
						String fileoffset = "";//16进制文件续传偏移量
						//接收数据
						byte[] acceptbyte=null;
						try {
							if(byteType.equals("9")){
								fileoffset= Convert.byteToHexString(content[8])+Convert.byteToHexString(content[7])+Convert.byteToHexString(content[6])+Convert.byteToHexString(content[5]);
								acceptbyte = new byte[content.length-11];
								System.arraycopy(content, 9, acceptbyte, 0, content.length-11);
								acceptData = new String(acceptbyte, GBK);
							}else{
								acceptbyte = new byte[content.length-7];
								System.arraycopy(content, 5, acceptbyte, 0, content.length-7);
								acceptData = new String(acceptbyte, GBK);
							}
						} catch (UnsupportedEncodingException e) {
							errorlogger.info("出错日志  记录："+e.getMessage()+"  信息：Socket错误,channelRead" );
						}
						debuglogger.debug("跟踪日志  记录："+" 终端握手类型：" + byteType + " 命令：" + byteOrder+"  信息：" + address );
						AcceptData(ctx,byteType,byteOrder,fileoffset,acceptData,acceptbyte);
					}
				}
			}

		});

	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
//		encoded.release();
		super.channelReadComplete(ctx);
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		String address = ctx.channel().remoteAddress().toString();
		debuglogger.info("跟踪日志  记录：终端中断连接  信息：" +address);
		SocketServer.socketChannelList.remove(ctx);
		ctx.close(); 
		for(int i=0;i<SocketServer.clients.size();i++){
			if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
				SocketServer.clients.remove(i);
			}
		}
	}
	public void AcceptData(ChannelHandlerContext ctx,String byteType,String byteOrder, String fileoffset,String acceptData,byte[] acceptbyte){
//		//获取已连接的IMEI
//		String myimei = "862105022977619";
		String myimei = null;
		byte[] ReturnData=null;
		byte[] byteFile=null;
		int byteCount=0;
		if(byteType.equals("1")){
			myimei = acceptData;
			for(int i=0;i<SocketServer.clients.size();i++){
				if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
					//			index = i;
					if(SocketServer.clients.get(i).getImei()==null||SocketServer.clients.get(i).getImei().equals("")){
						SocketServer.clients.get(i).setImei(acceptData);
						SocketServer.clients.get(i).setLastTime(new Date());
						SocketServer.clients.get(i).setByteCount(0);
					}
				}
			}
		}
		if(acceptData.equals("upgrade.bin")){
			for(int i=0;i<SocketServer.clients.size();i++){
				if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
					if(SocketServer.clients.get(i).getImei()==null||SocketServer.clients.get(i).getImei().equals("")){
						SocketServer.clients.get(i).setByteCount(0);
						SocketServer.clients.get(i).setByteFile(null);
					}
				}
			}
		}
		if(!byteType.equals("1")){
			for(int i=0;i<SocketServer.clients.size();i++){
				if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
					if((myimei==null|| myimei.equals("")) &&SocketServer.clients.get(i).getImei()!=null&&!SocketServer.clients.get(i).getImei().equals("")){
						myimei = SocketServer.clients.get(i).getImei();
					}
					if(SocketServer.clients.get(i).getByteCount()!=null){
						byteCount = SocketServer.clients.get(i).getByteCount();
					}
					if(SocketServer.clients.get(i).getByteFile()!=null){
						byteFile = SocketServer.clients.get(i).getByteFile();
					}
					SocketServer.clients.get(i).setLastTime(new Date());
				}
			}
		}
		try{
			//显示
			switch(byteType){
			case "1"://获取服务器时间
				ReturnData = mydata.GetDataTime().trim().getBytes(GBK);
				myimei = acceptData;
				infologger.info("正常日志   记录：获取服务器时间    信息："+ myimei+":"+ctx.channel().remoteAddress());
				break;
			case "2"://终端状态
				mydata.SetTerminalParameter(myimei, acceptData);
				ReturnData = "".getBytes(GBK);
				infologger.info("正常日志   记录：终端状态:" + acceptData+"  信息："+ myimei);
				break;
			case "3"://终端音量
				mydata.SetTerminalVoice(myimei, acceptbyte);
				ReturnData = acceptbyte;
				infologger.info("正常日志   记录：终端音量   信息："+ myimei);
				break;
				//case "4"://删除的文件
				//	String myfiles=mydata.GetDeleteFiles(myimei, acceptData);
				//	ReturnData = myfiles.trim().getBytes(GBK);
				//	logger.info("正常日志   记录：申请删除文件:" + myfiles+"  信息："+ myimei);
				//	break;
			case "5"://获取文件
					if(!byteOrder.equals("0")){
						senderlenth = (Integer.parseInt(byteOrder)*16);//定义每次发送的字节长度 出去包尾和校验两个字节
					}
					if(byteCount==0 && byteFile==null){//获取新的文件，并保存到变量中
						if(myimei!=null &&mydata.getTerIsuse(myimei)){
							byteFile = mydata.FileContent(myimei, acceptData);
							infologger.info("正常日志   记录：申请发送文件:" + acceptData + " 总字节数：" +byteFile.length + ";byteOrder:" + byteOrder + "; 信息："+ myimei);
						}else{
							ReturnData = new byte[0];
							infologger.info("正常日志   记录：终端已停用，申请发送文件:" + acceptData + " 失败! 信息："+ myimei);
						}
					}
					if(byteFile!=null){//如果存在数据，则开始按照512的大小进行分割发送
						if(byteFile.length>senderlenth&& byteFile.length!=0){//当文件字节过长时，进行切割，仅发送定义的字节长度，剩余字节暂时保存等待下次发送
							ReturnData = new byte[senderlenth];
							byteOrder="0";//标示有后续文件还需要继续发送的命令
							System.arraycopy(byteFile, 0, ReturnData, 0, senderlenth);//复制本次需要发送的数据字节
							for(int i=0;i<SocketServer.clients.size();i++){//将本次发送后剩余的字节保存起来
								if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
									SocketServer.clients.get(i).setByteCount(SocketServer.clients.get(i).getByteCount()+senderlenth);//标示当前文件字节发送的长度到哪个位置
//									SocketServer.clients.get(i).setByteFile(new byte[byteFile.length-senderlenth]);
									byte[] tem = new byte[byteFile.length-senderlenth];
									System.arraycopy(byteFile, senderlenth, tem, 0, byteFile.length-senderlenth);//保存剩余的文件字节，等待下次发送
									SocketServer.clients.get(i).setByteFile(tem);
									if(SocketServer.clients.get(i).getFilelenth()==0){
										SocketServer.clients.get(i).setFilelenth(byteFile.length);
									}
									byteCount = SocketServer.clients.get(i).getByteCount()+senderlenth;
								}
							}
							infologger.info("正常日志   记录：正在发送文件:" + acceptData + "  字节数：" + senderlenth + ";byteOrder:" + byteOrder + "; 信息："+ myimei);
						}else{
							byteOrder="1";//标示该文件最后的一段发送完毕
							ReturnData = byteFile;//发送剩余所有的字节
							int filelenth=0;
							for(int i=0;i<SocketServer.clients.size();i++){//清空保存的剩余数据字节
								if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
									SocketServer.clients.get(i).setByteCount(0);
									SocketServer.clients.get(i).setByteFile(null);
									if(SocketServer.clients.get(i).getFilelenth()==0){
										filelenth=byteFile.length;
									}else{
										filelenth=SocketServer.clients.get(i).getFilelenth();
									}
									SocketServer.clients.get(i).setFilelenth(0);
								}
							}
							infologger.info("正常日志   记录：申请发送文件:" + acceptData + "发送完毕!  字节数：" + senderlenth + ";byteOrder:" + byteOrder + "; 信息："+ myimei);
							//记录终端下载文件使用流量
							if(!acceptData.equals("list.txt"))   
								mydata.SetTerTraffic(myimei,filelenth);
						}
					}
				
				break;
			case "6"://获取维护授权电话
				String mytels=mydata.GetTels(myimei);
				ReturnData = mytels.getBytes(GBK);
				infologger.info("正常日志   记录：获取授权电话:" + mytels+"  信息："+ myimei);
				break;
			case "7"://交互时间
				ReturnData = mydata.GetInteractionTime(myimei).getBytes(GBK);
				infologger.info("正常日志   记录：获取系统交互时间    信息："+ myimei);
				break;
			case "8":
					if(!byteOrder.equals("0")){
						senderlenth = (Integer.parseInt(byteOrder)*16);//定义每次发送的字节长度 出去包尾和校验两个字节
					}
					if(byteCount==0 && byteFile==null){//获取新的文件，并保存到变量中
						if(myimei!=null &&mydata.getTerIsuse(myimei)){
							byteFile = mydata.UrgentFile(myimei, acceptData);
							infologger.info("正常日志   记录：申请发送文件:" + acceptData + " 总字节数：" +byteFile.length + ";byteOrder:" + byteOrder + "; 信息："+ myimei);
						}else{
							ReturnData = new byte[0];
							infologger.info("正常日志   记录：终端已停用，申请发送文件:" + acceptData + " 失败! 信息："+ myimei);
						}
					}
					if(byteFile!=null){//如果存在数据，则开始按照512的大小进行分割发送
						if(byteFile.length>senderlenth&& byteFile.length!=0){//当文件字节过长时，进行切割，仅发送定义的字节长度，剩余字节暂时保存等待下次发送
							ReturnData = new byte[senderlenth];
							byteOrder="0";//标示有后续文件还需要继续发送的命令
							System.arraycopy(byteFile, 0, ReturnData, 0, senderlenth);//复制本次需要发送的数据字节
							for(int i=0;i<SocketServer.clients.size();i++){//将本次发送后剩余的字节保存起来
								if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
									SocketServer.clients.get(i).setByteCount(SocketServer.clients.get(i).getByteCount()+senderlenth);//标示当前文件字节发送的长度到哪个位置
									SocketServer.clients.get(i).setByteFile(new byte[byteFile.length-senderlenth]);
									byte[] tem = new byte[byteFile.length-senderlenth];
									System.arraycopy(byteFile, senderlenth, tem, 0, byteFile.length-senderlenth);//保存剩余的文件字节，等待下次发送
									SocketServer.clients.get(i).setByteFile(tem);
									if(SocketServer.clients.get(i).getFilelenth()==0){
										SocketServer.clients.get(i).setFilelenth(byteFile.length);
									}
									byteCount = SocketServer.clients.get(i).getByteCount();
								}
							}
							infologger.info("正常日志   记录：正在发送文件:" + acceptData + "  字节数：" + senderlenth + ";byteOrder:" + byteOrder +";byteCount:" + byteCount + "; 信息："+ myimei);
						}else{
							byteOrder="1";//标示该文件最后的一段发送完毕
							ReturnData = byteFile;//发送剩余所有的字节
							int filelenth=0;
							for(int i=0;i<SocketServer.clients.size();i++){//清空保存的剩余数据字节
								if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
									SocketServer.clients.get(i).setByteCount(0);
									SocketServer.clients.get(i).setByteFile(null);
									if(SocketServer.clients.get(i).getFilelenth()==0){
										filelenth=byteFile.length;
									}else{
										filelenth=SocketServer.clients.get(i).getFilelenth();
									}
									SocketServer.clients.get(i).setFilelenth(0);
								}
							}
							infologger.info("正常日志   记录：申请发送文件:" + acceptData + "发送完毕!  字节数：" + byteFile.length + ";byteOrder:" + byteOrder + "; 信息："+ myimei);
							//记录终端下载文件使用流量
							mydata.SetTerTraffic(myimei,filelenth);
						}
						
					}
				
				break;
			case "9":
					if(!byteOrder.equals("0")){
						senderlenth = (Integer.parseInt(byteOrder)*16);//定义每次发送的字节长度 出去包尾和校验两个字节
					}
					if(byteFile==null){//获取新的文件，并保存到变量中
						if(myimei!=null &&mydata.getTerIsuse(myimei)){
							byteFile = mydata.FileContent(myimei, acceptData);
							for(int i=0;i<SocketServer.clients.size();i++){
								if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
									SocketServer.clients.get(i).setByteFile(byteFile);
									SocketServer.clients.get(i).setLastTime(new Date());
									SocketServer.clients.get(i).setFilelenth(byteFile.length);
								}
							}
						}else{
							ReturnData = new byte[0];
							infologger.info("正常日志   记录：终端已停用，申请发送文件:" + acceptData + " 失败! 信息："+ myimei);
						}
						infologger.info("正常日志   记录：申请发送文件:" + acceptData + " 总字节数：" +byteFile.length + ";byteOrder:" + byteOrder + "; 信息："+ myimei);
					}
					if(byteFile!=null){//如果存在数据，则开始按照512的大小进行分割发送
						int offset = 0;
						if(!fileoffset.trim().equals(""))
							offset=Integer.parseInt(fileoffset, 16);
						if((byteFile.length-offset)>senderlenth&& byteFile.length!=0){//当文件字节过长时，进行切割，仅发送定义的字节长度，剩余字节暂时保存等待下次发送
							ReturnData = new byte[senderlenth];
							byteOrder="0";//标示有后续文件还需要继续发送的命令
							System.arraycopy(byteFile, offset, ReturnData, 0, senderlenth);//复制本次需要发送的数据字节
							infologger.info("正常日志   记录：正在发送文件:" + acceptData + "偏移量："+offset+ " 总字节数：" +byteFile.length +"  字节数：" + senderlenth + ";byteOrder:" + byteOrder + "; 信息："+ myimei);
						}else{
							ReturnData = new byte[byteFile.length-offset];
							byteOrder="1";//表示发送完毕
							System.arraycopy(byteFile, offset, ReturnData, 0, byteFile.length-offset);//复制本次需要发送的数据字节
							infologger.info("正常日志   记录：申请发送文件:" + acceptData + "发送完毕!  字节数：" + (byteFile.length-offset) + ";byteOrder:" + byteOrder + "; 信息："+ myimei);
							int filelenth=0;
							for(int i=0;i<SocketServer.clients.size();i++){
								if(SocketServer.clients.get(i).getChannelAddress().equals(address)){
									SocketServer.clients.get(i).setByteFile(null);
									filelenth = SocketServer.clients.get(i).getFilelenth();
									SocketServer.clients.get(i).setFilelenth(0);
								}
							}
							//记录终端下载文件使用流量
							mydata.SetTerTraffic(myimei,filelenth);
						}
					}
				
				break;
			case "10"://获取终端应急rds码和调频频率
				String str = mydata.GetRdsAndfreq(myimei);
				ReturnData =str.getBytes(GBK);
				infologger.info("正常日志   记录：获取终端应急rds码和调频频率    信息："+ myimei);
				break;
			case "11"://FM调频发射RDS码/发射频率/发射机
				 mydata.SetFMConditions(myimei,acceptData);
				 ReturnData = "1".getBytes(GBK);
				infologger.info("正常日志   记录：FM调频发射RDS码/发射频率/发射机参数    信息："+ myimei);
				break;
//			case "12"://心跳包
//				String imei = acceptData;
//				//记录终端的ip地址
//				InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
//				String clientIP = insocket.getAddress()+":"+insocket.getPort();
//				System.out.println(imei+":"+clientIP);
//				Tools.setProValue(Const.TERIPS, imei, clientIP);
//				ReturnData = "1".getBytes(GBK);
//				infologger.info("正常日志   记录：终端IP变更    信息："+ myimei+":"+clientIP);
//				break;
			case "13"://基站信息
				mydata.SetBaseStation(myimei,acceptData);
				 ReturnData = "1".getBytes(GBK);
				infologger.info("正常日志   记录：基站信息    信息："+ myimei+":"+ctx.channel().remoteAddress());
				break;
			case "16"://LED显示信息
				 ReturnData = mydata.GetLedShowInfoByTid(myimei).getBytes(GBK);
				infologger.info("正常日志   记录：LED显示信息   信息："+ myimei+":"+ctx.channel().remoteAddress());
				break;
			}
			SendToByte(ctx,byteType,byteOrder,ReturnData);
		} catch (UnsupportedEncodingException e) {
			errorlogger.info("出错日志  记录："+e.getMessage()+"  信息：Socket错误,AcceptData" );
		}
	}
	private void SendToByte(ChannelHandlerContext ctx,String byteType,String byteOrder, byte[] SendData){
		if (SendData != null && SendData.length != 0){
			byte[] checkData = new byte[SendData.length+3];//用来计算校验和
			try {
				int buffersize = 7+SendData.length;
				encoded = ctx.alloc().buffer(buffersize);//建立发送数据字节数组
				//加上包头尾
				encoded.writeByte(Convert.hexStringToBytes(BeginChar)[0]);//将开始标示复制到发送数据中去
//				encoded.writeByte(Convert.hexStringToBytes(Integer.toHexString(Integer.parseInt(byteType)))[0]);//将类型复制到发送数据中去
				encoded.writeByte((byte)Integer.parseInt(byteType));//将类型复制到发送数据中去
//				byteType="11";
//				System.out.println(Integer.toHexString(Integer.parseInt(byteType)));
//				System.out.println(Convert.hexStringToBytes(Integer.toHexString(Integer.parseInt(byteType)))[0]);
//				System.out.println((byte)Integer.parseInt(byteType));
				byte[] l = Convert.hexStringToBytes(Integer.toHexString(SendData.length+2));
				if(l.length==1){
					encoded.writeByte(l[0]);//将包长复制到发送数据中去
					encoded.writeByte((byte)0);
					checkData[0]=l[0];
					checkData[1]=(byte)0;
				}else if(l.length==2){
					encoded.writeByte(l[1]);//将包长复制到发送数据中去
					encoded.writeByte(l[0]);
					checkData[1]=l[0];
					checkData[0]=l[1];
				}
				encoded.writeByte((byte)Integer.parseInt(byteOrder));//将命令设置到发送数据中去
				checkData[2] = (byte)Integer.parseInt(byteOrder);
				encoded.writeBytes(SendData);//将数据字节复制到发送数据中去
				System.arraycopy(SendData, 0, checkData, 3, SendData.length);
				String check = Convert.checksum(checkData);
				encoded.writeByte(Convert.hexStringToBytes(check)[0]);//将校验复制到发送数据中去
				encoded.writeByte(Convert.hexStringToBytes(EndChar)[0]);//将结束标示复制到发送数据中去
				
				ctx.writeAndFlush(encoded);
			} catch (Exception ex) {
				errorlogger.info("出错日志  记录："+ex.getMessage()+"  信息：Socket错误,SendToByte  byteType:" + byteType + ",byteOrder:" + byteOrder);
			}
		}else{
			try {
				int buffersize = 7;
				encoded = ctx.alloc().buffer(buffersize);//建立发送数据字节数组
				//加上包头尾
				encoded.writeByte(Convert.hexStringToBytes(BeginChar)[0]);//将开始标示复制到发送数据中去
				encoded.writeByte((byte)Integer.parseInt(byteType));//将类型复制到发送数据中去
				byte[] l = Convert.hexStringToBytes(Integer.toHexString(2));
				encoded.writeByte( l[0]);
				encoded.writeByte( (byte)0);
				encoded.writeByte( (byte)1);
				encoded.writeByte( Convert.hexStringToBytes("1e")[0]);//将校验复制到发送数据中去
				encoded.writeByte( Convert.hexStringToBytes(EndChar)[0]);//将结束标示复制到发送数据中去
				ctx.writeAndFlush(encoded);
			} catch (Exception ex) {
				errorlogger.info("出错日志  记录："+ex.getMessage()+"  信息：Socket错误,SendToByte  byteType:" + byteType + ",byteOrder:" + byteOrder );
			}
		}
	}

}
