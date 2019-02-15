package com.socket.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.apache.log4j.Logger;

import com.socket.entity.SocketInfo;
import com.socket.mapper.BaseAttribsMapper;
import com.socket.util.Const;
import com.socket.util.Tools;

public class SocketServer {

	public static void main(String[] args) {
		SocketServer server= new SocketServer();
		server.bind();
	}
	private int PORT = 8600;//终端端口
	private static int Socketbacklog = 1000; //允许客户端数量
	final static String UTF_8 = "utf-8";
	public static List<ChannelHandlerContext> socketChannelList = new ArrayList<>();
	public static List<SocketInfo> clients = new ArrayList<>();
	public static ExecutorService executorService =  Executors.newCachedThreadPool();
	public static SenderData mydata ;
     // 消息的中止判断符
     public String EndChar = "cc";
     // 消息的开始判断符
     public String BeginChar = "aa";
     
 	BaseAttribsMapper baseAttribsMapper ;
     protected Logger logger = Logger.getLogger("info");//正常日志
     protected Logger errorlogger = Logger.getLogger("error");//错误日志
     
	public SocketServer() {
		mydata = new SenderData();
		Map<String,Integer> attr = new HashMap<String, Integer>();
		attr = mydata.GetBaseAttribs();
		if(attr==null){//访问数据库失败
			this.PORT= Integer.parseInt(Tools.GetValueByKey(Const.CONFIG, "PORT"));
			SocketServer.Socketbacklog = Integer.parseInt(Tools.GetValueByKey(Const.CONFIG, "Socketbacklog"));
		}else{
			this.PORT= attr.get("PORT");
			SocketServer.Socketbacklog = attr.get("Socketbacklog");
			if(Integer.parseInt(Tools.GetValueByKey(Const.CONFIG, "PORT"))!=this.PORT)
				Tools.setProValue(Const.CONFIG, "PORT", this.PORT+"");
			if(Integer.parseInt(Tools.GetValueByKey(Const.CONFIG, "Socketbacklog"))!=SocketServer.Socketbacklog)
				Tools.setProValue(Const.CONFIG, "Socketbacklog", SocketServer.Socketbacklog+"");
		}
	}

	public void bind() {

		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();

			bootstrap.group(boss, worker);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.option(ChannelOption.SO_BACKLOG, Socketbacklog); //连接数
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);  //允许重复使用本地地址和端口
			bootstrap.option(ChannelOption.TCP_NODELAY, true);  //立即发送
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true); //长连接
			bootstrap.childHandler(new NettyChannelHandler());
			
			ChannelFuture channelFuture = bootstrap.bind(PORT).sync();
			if (channelFuture.isSuccess()) {
				System.out.println("启动监听");
				logger.info("正常日志  信息： 开始启动Stocket监听，端口:" + PORT + "；" );
			}
			// 关闭连接
			channelFuture.channel().closeFuture().sync();
			
		} catch (Exception e) {
			errorlogger.error("启动Netty服务异常，异常信息：" + e.getMessage());
		} finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

	private class NettyChannelHandler extends ChannelInitializer<SocketChannel> {  
		  
        @Override  
        protected void initChannel(SocketChannel socketChannel)  
                throws Exception {  
        	// 自己的逻辑Handler
        	socketChannel.pipeline().addLast(new NettyServerHandler(mydata));  
        }  
    }  
}
