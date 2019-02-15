package com.socket.socket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.socket.entity.BaseAttribs;
import com.socket.entity.LedShowInfo;
import com.socket.entity.Program_List;
import com.socket.entity.Programmes;
import com.socket.entity.TerTraffic;
import com.socket.entity.TerminalTels;
import com.socket.entity.Terminal_Conditions;
import com.socket.entity.Terminal_Recieve;
import com.socket.mapper.BaseAttribsMapper;
import com.socket.mapper.LedShowMapper;
import com.socket.mapper.ProgrammesMapper;
import com.socket.mapper.TerTrafficMapper;
import com.socket.mapper.TerminalTelsMapper;
import com.socket.mapper.TerminalMapper;
import com.socket.util.Const;
import com.socket.util.Logger;
import com.socket.util.Tools;

public class SenderData {
	final static String GBK = "GBK";
//	private ApplicationContext applicationContext;
	ProgrammesMapper programmesMapper ;
	BaseAttribsMapper baseAttribsMapper ;
	TerminalMapper terminalMapper ;
	TerminalTelsMapper terminalTelsMapper ;
	TerTrafficMapper terTrafficMapper ;
	LedShowMapper ledShowMapper ;
	private SqlSessionFactory sqlSessionFactory;
//	protected Logger logger = Logger.getLogger(this.getClass());
	protected Logger errorlogger = Logger.getLogger("error");//错误日志
	public SenderData(){
		//mybatis配置文件
				String resource = "mybatis/SqlMapConfig.xml";
				//得到配置文件流
				InputStream inputStream = null;
				try {
					inputStream = Resources.getResourceAsStream(resource);
				} catch (IOException e) {
					e.printStackTrace();
				}
				//创建会话工厂,传入mybatis的配置信息
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//		applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext.xml");
	}
	/**
	 * 终端是否启用
	 * @param imei
	 * @return
	 */
	public Boolean getTerIsuse(String imei){
		 SqlSession sqlSession = sqlSessionFactory.openSession();
		 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
		 Boolean isenabled = false;
		try {
			isenabled = terminalMapper.getTerIsuse(imei);
		} catch (Exception e) {
			isenabled=true;
			errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取终端是否启用状态,getTerIsuse" );
		}
		sqlSession.close();
		return isenabled;
	}
	/*
	 * 获取基础属性
	 */
	public Map<String, Integer> GetBaseAttribs(){
		Map<String,Integer> attr = new HashMap<String, Integer>();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		baseAttribsMapper = sqlSession.getMapper(BaseAttribsMapper.class);
        BaseAttribs ba2 = new BaseAttribs();
        BaseAttribs ba3 = new BaseAttribs();
	   	 try {
			List<BaseAttribs> bas = baseAttribsMapper.getBaseAttribs();
			for(BaseAttribs ba :bas){
				if(ba.getValueID().equals("终端通讯访问端口")){
					ba2 = ba;
				}
				if(ba.getValueID().equals("终端通讯数量")){
					ba3 = ba;
				}
			}
			sqlSession.close();
			attr.put("PORT", Integer.parseInt(ba2.getValueName()));
			attr.put("Socketbacklog", Integer.parseInt(ba3.getValueName()));
	   	 } catch (Exception e) {
	   		errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取基础属性,GetBaseAttribs" );
			}
	   	 return attr;
	}
	/*
	 * 获取终端LED最新显示
	 */
	public String GetLedShowInfoByTid(String tid){
		Map<String,String> attr = new HashMap<String, String>();
		SqlSession sqlSession = sqlSessionFactory.openSession();
		ledShowMapper = sqlSession.getMapper(LedShowMapper.class);
		LedShowInfo lsi = null;
		try {
			lsi = ledShowMapper.getTerNewLedShow();
			
		} catch (Exception e) {
			errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:终端LED最新显示,GetLedShowInfoByTid" );
		}
		sqlSession.close();
		if(lsi!=null){
			
			return lsi.getContent()+","+lsi.getShowtime();
		}else{
			return "";
		}
		
	}
	/*
	 * 获取服务器时间
	 */
	public String GetDataTime(){
		Date nowTime=new Date();  
		SimpleDateFormat ftime=new SimpleDateFormat("yyyyMMddHHmmss");
		String date=ftime.format(nowTime);//现在的时间按照sdf1模式返回字符串  
		return date;
	}
	/**
	 * 获取终端交互时间
	 * @param IMEI
	 * @return
	 * @throws Exception 
	 */
	 public String GetInteractionTime(String IMEI) 
     {
		 SqlSession sqlSession = sqlSessionFactory.openSession();
		 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
//		 terminalMapper= (TerminalMapper) applicationContext.getBean("terminalMapper");
		 String time = null;
		try {
			time = terminalMapper.getIntertime(IMEI);
		} catch (Exception e) {
			errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取终端交互时间,GetInteractionTime" );
		}
		sqlSession.close();
         if (time==null || time.equals("")) {
             return "00:01:00";
         } else {
             return time;
         }
     }
	 /**
	  * 获取终端应急rds码和调频频率
	  * @param IMEI
	  * @return
	  * @throws Exception 
	  */
	 public String GetRdsAndfreq(String IMEI) 
	 {
		 SqlSession sqlSession = sqlSessionFactory.openSession();
		 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
//		 terminalMapper= (TerminalMapper) applicationContext.getBean("terminalMapper");
		 Map<String,String> fm = null;
		 try {
			 fm = terminalMapper.getRdsAndfreq(IMEI);
		 } catch (Exception e) {
			 errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取终端应急rds码和调频频率,GetRdsAndfreq" );
		 }
		 sqlSession.close();
		 if(fm!=null){
			 String rds = fm.get("rds").toString();
			 String fmfrequency = fm.get("fmfrequency").toString();
//		 if(!rds.matches("[0-9]{3}")){
//			 rds="";
//		 }
//		 if(!fmfrequency.matches("[0-9]{2,3}.d{1}")){
//			 fmfrequency="";
//		 }
			 return rds+","+fmfrequency;
		 }else{
			 return "";
		 }
	 }
	 /**
	  * FM调频发射RDS码/发射频率/发射机参数
	  * @param IMEI
	  * @param mymsg
	  */
	 public void SetFMConditions(String IMEI, String mymsg)
	 {
		 try
		 {
			 SqlSession sqlSession = sqlSessionFactory.openSession();
			 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
			 String[] mylist = mymsg.split(",");
			 if(mylist.length>=5){
				 Terminal_Conditions tc = new Terminal_Conditions();
				 tc.setTerminalID(IMEI);
				 tc.setRds(mylist[0]);
				 tc.setFmfrequency(mylist[1]);
				 tc.setTransmitpower(mylist[2]);
				 tc.setReflectedpower(mylist[3]);
				 tc.setWorkvoltage(mylist[4]);
				 
				 Date nowTime=new Date();  
				 SimpleDateFormat ftime=new SimpleDateFormat("yyyy-MM-dd");
				 String date=ftime.format(nowTime);//现在的时间按照sdf1模式返回字符串  
				 tc.setCreatedTime(date);
				 if(terminalMapper.existCondition(IMEI)>0){
					 terminalMapper.updateConditions3(tc);
				 }else{
					 terminalMapper.insertConditionsOfIMEI(tc);
				 }
				 sqlSession.commit();
				 sqlSession.close();
			 }else{
				 errorlogger.error("出错日志  记录:终端回传参数格式不正确  信息:得到FM调频发射RDS码/发射频率/发射机参数错误,SetFMConditions" );
			 }
		 }
		 catch (Exception err)
		 {
			 errorlogger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:得到FM调频发射RDS码/发射频率/发射机参数错误,SetFMConditions" );
		 }
	 }
	 /**
	  * 基站信息
	  * @param IMEI
	  * @param mymsg
	  */
	 public void SetBaseStation(String IMEI, String mymsg)
	 {
		 try
		 {
			 SqlSession sqlSession = sqlSessionFactory.openSession();
			 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
				 Terminal_Conditions tc = new Terminal_Conditions();
				 tc.setTerminalID(IMEI);
				 tc.setBasestation(mymsg);
				 
				 Date nowTime=new Date();  
				 SimpleDateFormat ftime=new SimpleDateFormat("yyyy-MM-dd");
				 String date=ftime.format(nowTime);//现在的时间按照sdf1模式返回字符串  
				 tc.setCreatedTime(date);
				 if(terminalMapper.existCondition(IMEI)>0){
					 terminalMapper.updateBasestation(tc);
				 }else{
					 terminalMapper.insertConditionsOfIMEI(tc);
				 }
				 sqlSession.commit();
				 sqlSession.close();
		 }
		 catch (Exception err)
		 {
			 errorlogger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:基站信息错误,SetBaseStation" );
		 }
	 }
	 /**
	  * 得到终端相关参数
	  * @param IMEI
	  * @param mymsg
	  */
     public void SetTerminalParameter(String IMEI, String mymsg)
     {
         try
         {
        	 SqlSession sqlSession = sqlSessionFactory.openSession();
    		 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
//        	 terminalMapper= (TerminalMapper) applicationContext.getBean("terminalMapper");
        	 Terminal_Conditions tc = new Terminal_Conditions();
        	 tc.setTerminalID(IMEI);
             String[] mylist = mymsg.split("，");
             for (int i = 0; i < mylist.length; i++)
             {
            	 String[] myy = mylist[i].split("：");
                 switch (myy[0])
                 {
                     case "硬件版本":
                    	 tc.setHardwareVersion( myy[1].trim());
                         break;
                     case "软件版本":
                    	 tc.setSoftwareVersion(myy[1].trim());
                         break;
                     case "内核温度":
                    	 tc.setTemperature(myy[1].trim());
                         break;
                     case "网络类型":
                    	 tc.setNetworkTypes(myy[1].trim());
                         break;
                     case "信号质量":
                    	 tc.setSignalQuality(myy[1].trim());
                         break;
                     case "SD卡容量":
                    	 tc.setSdCapacity(myy[1].trim());
                    	 break;
                     case "发射功率":
                    	 tc.setTransmitpower(myy[1]==null||myy[1].equals("")?"0":myy[1].trim());
                    	 break;
                     case "反射功率":
                    	 tc.setReflectedpower(myy[1]==null||myy[1].equals("")?"0":myy[1].trim());
                    	 break;
                     case "工作电压":
                    	 tc.setWorkvoltage(myy[1]==null||myy[1].equals("")?"0":myy[1].trim());
                         break;
                 }
             }
             Date nowTime=new Date();  
             SimpleDateFormat ftime=new SimpleDateFormat("yyyy-MM-dd");
             String date=ftime.format(nowTime);//现在的时间按照sdf1模式返回字符串  
             tc.setCreatedTime(date);
             if(terminalMapper.existCondition(IMEI)>0){
            	 terminalMapper.updateConditions1(tc);
             }else{
            	 terminalMapper.insertConditionsOfIMEI(tc);
             }
             sqlSession.commit();
             sqlSession.close();
         }
         catch (Exception err)
         {
        	 errorlogger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:得到终端相关参数错误,SetTerminalParameter" );
         }
     }

     /**
      * 得到终端相关音量设置
      * @param IMEI
      * @param mymsg
      */
     public void SetTerminalVoice(String IMEI, byte[] mymsg)
     {
         try
         {
        	 SqlSession sqlSession = sqlSessionFactory.openSession();
    		 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
//        	 terminalMapper= (TerminalMapper) applicationContext.getBean("terminalMapper");
        	 Terminal_Conditions tc = new Terminal_Conditions();
        	 tc.setTerminalID(IMEI);
        	 tc.setMp3Volume((int)mymsg[0]);
        	 tc.setFmVolume((int)mymsg[1]);
        	 tc.setGsmVolume((int)mymsg[2]);
        	 tc.setNullVolume((int)mymsg[3]);
        	 Date nowTime=new Date();  
             SimpleDateFormat ftime=new SimpleDateFormat("yyyy-MM-dd");
             String date=ftime.format(nowTime);//现在的时间按照sdf1模式返回字符串  
             tc.setCreatedTime(date);
             if(terminalMapper.existCondition(IMEI)>0){
            	 terminalMapper.updateConditions2(tc);
             }else{
            	 terminalMapper.insertConditionsOfIMEI(tc);
             }
             sqlSession.commit();
             sqlSession.close();
         }
         catch (Exception err)
         {
        	 errorlogger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:得到终端相关音量设置错误,SetTerminalVoice" );
         }
     }
     /**
      * 设置终端流量信息
      * @param IMEI
      * @param filelenth
      */
     public void SetTerTraffic(String IMEI, int filelenth)
     {
    	 try
    	 {
    		 SqlSession sqlSession = sqlSessionFactory.openSession();
    		 terTrafficMapper= sqlSession.getMapper(TerTrafficMapper.class);
    		 TerTraffic tt =  terTrafficMapper.getTerTrafficByIMEI(IMEI);
    		 Date nowTime=new Date();  
    		 SimpleDateFormat ftime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		 String date=ftime.format(nowTime);//现在的时间按照sdf1模式返回字符串  
    		 //计算本次使用流量
    		 Double fl = filelenth/(1024.0*1024.0);
    		 BigDecimal   b   =   new   BigDecimal(fl); 
    		 Double traffic = b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();//四舍五入 两位小数
    		 if(tt==null || tt.getTid()==null||"".equals(tt.getTid())){
    			 if(tt==null)	 tt= new TerTraffic();
    			 tt.setLasttime(date);
    			 tt.setTid(IMEI);
    			 tt.setUsetraffic(traffic );
    			 terTrafficMapper.insertTerTraffic(tt);
    		 }else{
    			 tt.setLasttime(date);
    			 traffic = traffic+tt.getUsetraffic();
    			 tt.setUsetraffic(traffic );
    			 terTrafficMapper.updateTerTrafficByIMEI(tt);
    		 }
    		 if(tt.getTrafficlimit()>0 && traffic>=tt.getTrafficlimit()){//如果使用流量超过流量限制
    			 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
    			 terminalMapper.updateTerNouse(IMEI);
    			 errorlogger.info("正常日志   记录：终端已使用 "+traffic+"MB 流量，超过 "+tt.getTrafficlimit()+"MB 限制，终端设置停用;  信息："+ IMEI);
			 }
    		 sqlSession.commit();
    		 sqlSession.close();
    	 }
    	 catch (Exception err)
    	 {
    		 errorlogger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:设置终端流量信息错误,SetTerTraffic");
    	 }
     }

//     /**
//      * 获取终端文件删除
//      * @param IMEI
//      * @param mymsg
//      * @return
//      */
//     public String GetDeleteFiles(String IMEI, String files)
//     {
//         try
//         {
//             String[] myfile = files.trim().split("\\|");
//             String delfile = "";
//             SqlSession sqlSession = sqlSessionFactory.openSession();
//             terminal_FilesMapper = sqlSession.getMapper(Terminal_FilesMapper.class);
////             terminal_FilesMapper= (Terminal_FilesMapper) applicationContext.getBean("terminal_FilesMapper");
//             //获取要删除的文件
//             List<Terminal_Files> tfs = terminal_FilesMapper.getDeleteFilesByIMEI(IMEI);
//             for (Terminal_Files tf:tfs){
//            	 delfile += tf.getFileName().trim()+ "|";
//             }
//             //删除文件的记录
//             terminal_FilesMapper.deleteAllFiles(IMEI);
//             //添加文件记录
//             Terminal_Files mytf=new Terminal_Files();
//             mytf.setTerminalID(IMEI);
//             mytf.setIsDelete(false);
//             Date nowTime=new Date();  
//             SimpleDateFormat ftime=new SimpleDateFormat("yyyy-MM-dd");
//             String date=ftime.format(nowTime);//现在的时间按照sdf1模式返回字符串  
//             mytf.setCreatedTime(date);
//             for(int i=0;i<myfile.length;i++){
//            	 mytf.setFileName(myfile[i]);
//            	 terminal_FilesMapper.insertFilesOfIMEI(mytf);
//             }
//             sqlSession.commit();
//             sqlSession.close();
//             delfile = delfile.substring(0, delfile.length()-1);
//             return delfile;
//         }
//         catch (Exception err)
//         {
//        	 logger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:获取终端文件删除错误,GetDeleteFiles" );
//             return "";
//         }
//     }

     /**
      * 获取终端授权电话
      * @param IMEI
      * @return
      */
     public String GetTels(String IMEI)
     {
         try
         {
        	 String tels = "";
        	 SqlSession sqlSession = sqlSessionFactory.openSession();
        	 terminalTelsMapper = sqlSession.getMapper(TerminalTelsMapper.class);
//        	 terminalTelsMapper= (TerminalTelsMapper) applicationContext.getBean("terminalTelsMapper");
             List<TerminalTels> tts = terminalTelsMapper.getTerminalTelsByIMEI(IMEI);
             if(tts.size()>0){
            	 for(int i=0;i<tts.size();i++){
            		 if(i==0) tels += tts.get(i).getTel().trim() ;
            		 else tels += ","+tts.get(i).getTel().trim()  ;
            	 }
//            	 tels = tels.substring(0, tels.length()-1);
             }
             sqlSession.close();
             return tels;
         }
         catch (Exception err)
         {
        	 errorlogger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:获取终端授权电话错误,GetTels" );
             return "";
         }
     }

     /**
      * 获取服务器短信电话号码
      * @return
      */
     private String GetServerTels()
     {
//    	 long starttime = System.currentTimeMillis();
         try
         {
        	 SqlSession sqlSession = sqlSessionFactory.openSession();
        	 baseAttribsMapper = sqlSession.getMapper(BaseAttribsMapper.class);
//        	 baseAttribsMapper= (BaseAttribsMapper) applicationContext.getBean("baseAttribsMapper");
        	 List<BaseAttribs> bas = baseAttribsMapper.getBaseAttribs();
             String ServerTel = "";
             for(BaseAttribs ba:bas){
            	 if(ba.getValueID().equals("服务器短信号码")){
            		 ServerTel = ba.getValueName();
            	 }
             }
             sqlSession.close();
             return ServerTel;
         }
         catch (Exception err)
         {
        	 errorlogger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:获取服务器短信电话号码错误,GetServerTels" );
             return "";
         }
     }
	/**
	 * 文件转换数据流.  
	 * @param IMEI
	 * @param fileName 文件名
	 * @return
	 */
    public byte[] FileContent(String IMEI, String fileName) {
//    	long starttime = System.currentTimeMillis();
		 
    	Date nowTime=new Date();  
		SimpleDateFormat ftime=new SimpleDateFormat("yyyy-MM-dd");
		String date=ftime.format(nowTime);//现在的时间按照sdf1模式返回字符串  
		
		 SqlSession sqlSession = sqlSessionFactory.openSession();
		 programmesMapper = sqlSession.getMapper(ProgrammesMapper.class);
//		programmesMapper= (ProgrammesMapper) applicationContext.getBean("programmesMapper");
		Map<String,String> map = new HashMap<String, String>();
		map.put("imei", IMEI);
		map.put("date", date);
        try
        {
            byte[] mybytes = null;
            Programmes model = programmesMapper.getProgrammesByIMEI(map);//根据IMEI号和播出日期获取节目单
            
            switch (fileName)
            {
                case "list.txt"://旧终端获取节目单
                	String mylist = "";
                	if(model!=null)   {
                		SetReceives(IMEI,model.getSfid());
                		mylist = GetProgrammes(model,"list");
                	}else{
                		SetReceives(IMEI,"");
                	}
                	mybytes = mylist.getBytes(GBK);
                    break;
                case "playlist.txt"://新终端获取节目单
                	String playlist = "";
                	if(model!=null)   {
                		SetReceives(IMEI,model.getSfid());
                		playlist = GetProgrammes(model,"playlist");
                	}else{
                		SetReceives(IMEI,"");
                	}
                	mybytes = playlist.getBytes(GBK);
                	break;
                case "config.ini"://获取配置文件
                	String myconfig = Getconfig(IMEI);
                    mybytes = myconfig.getBytes(GBK);
                    break;
                case "upgrade.bin"://获取升级文件
                    mybytes = GetUpgrade();
                    break;
                default://获取其他文件
                	String filepath = null;
                	if(model!=null){
                		for(Program_List prolist : model.getProlist()){
                			if(prolist.getProtype().trim().equals(Const.ProgrammeListTypes_file)){
                				if(prolist.getFilename().trim().equals(fileName.trim())){
                					filepath=prolist.getAddress();
                				}
                			}
                		}
                	}else{
                		filepath = programmesMapper.getProAddressByFile(fileName);
                	}
                    mybytes = GetFiles(filepath);
                    break;

            }
            sqlSession.close();
            if(mybytes==null) mybytes = new byte[0];
            return mybytes;
        }
        catch (Exception err)
        {
        	errorlogger.error("出错日志  记录:" + err.getMessage() + "  信息:文件转换数据流错误,FileContent" );
        	return new byte[0];
        }
    }
    /**
     * 紧急任务文件  
     * @param IMEI
     * @param fileName 文件名
     * @return
     */
    public byte[] UrgentFile(String IMEI, String fileName) {
    	SqlSession sqlSession = sqlSessionFactory.openSession();
    	programmesMapper = sqlSession.getMapper(ProgrammesMapper.class);
//		programmesMapper= (ProgrammesMapper) applicationContext.getBean("programmesMapper");
    	try
    	{
    		byte[] mybytes = null;
    		String filepath = programmesMapper.getProAddressByFile(fileName);
    		sqlSession.close();
    		mybytes = GetFiles(filepath);
    		if(mybytes==null) mybytes = new byte[0];
    		return mybytes;
    	}
    	catch (Exception err)
    	{
    		errorlogger.error("出错日志  记录:" + err.getMessage() + "  信息:文件转换数据流错误,UrgentFile" );
    		return new byte[0];
    	}
    }
    /**
     * 记录终端访问
     * @param IMEI
     * @param programmeid 节目单ID
     */
    private void SetReceives(String IMEI, String programmeid)
    {
        try
        {
        	//添加终端访问记录
        	 SqlSession sqlSession = sqlSessionFactory.openSession();
    		 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
//        	terminalMapper= (TerminalMapper) applicationContext.getBean("terminalMapper");
        	Terminal_Recieve tr = new Terminal_Recieve();
        	tr.setProgrammeID(programmeid);
        	tr.setTerminalID(IMEI);
        	Date nowTime=new Date();  
            SimpleDateFormat ftime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String datetime=ftime.format(nowTime);//现在的时间按照sdf1模式返回字符串  
        	tr.setCreatedTime(datetime);
        	terminalMapper.insertTerminalRecieve(tr);
        	sqlSession.commit();
            sqlSession.close();
            System.out.println(tr.toString());
        }
        catch (Exception err)
        {
        	errorlogger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:记录终端访问错误,SetReceives" );
        }
    }
    /**
     * 获取节目单
     * @param model节目单实体类
     * @return
     */
    private String GetProgrammes(Programmes model,String type)
    {
        String listtxt = "";
        try {
            for(Program_List prolist : model.getProlist()) {
                switch (prolist.getProtype()) {
                    case Const.ProgrammeListTypes_file:
                    	if(type.equals("list")){
                    		listtxt += prolist.getBegintime() + ",mp3," + prolist.getFilename() + "\r\n";
                    	}else{
                    		listtxt += prolist.getBegintime() + ",mp3," + Tools.GetValueByKey(Const.CONFIG, "server.url")+prolist.getFilename() + ","+ prolist.getUrls() + "\r\n";
                    	}
                        break;
                    case Const.ProgrammeListTypes_radio:
                        listtxt += prolist.getBegintime() + ",fm," + prolist.getProname() + "\r\n";
                        break;
                    case Const.ProgrammeListTypes_openGF:
                        listtxt += prolist.getBegintime() + ",amp-on\r\n";
                        break;
                    case Const.ProgrammeListTypes_closeGF:
                        listtxt += prolist.getBegintime() + ",amp-off\r\n";
                        break;
                    case Const.ProgrammeListTypes_shutdown:
                        listtxt += prolist.getBegintime() + ",stop\r\n";
                        break;
                }
            }
        }
        catch (Exception err) {
        	errorlogger.error("出错日志  记录:" + err.getMessage().toString() + "  信息:获取节目单错误,GetProgrammes" );
        }
        return listtxt;
    }

    private String Getconfig(String IMEI)
    {
    	String config = null;
        config = "boot-server:192.168.1.102:8600\r\n";
        SqlSession sqlSession = sqlSessionFactory.openSession();
		 terminalMapper = sqlSession.getMapper(TerminalMapper.class);
//        baseAttribsMapper= (BaseAttribsMapper) applicationContext.getBean("baseAttribsMapper");
        BaseAttribs ba1 = new BaseAttribs();
        BaseAttribs ba2 = new BaseAttribs();
	   	 try {
			List<BaseAttribs> bas = baseAttribsMapper.getBaseAttribs();
			for(BaseAttribs ba :bas){
				if(ba.getValueID().equals("服务器公网IP地址")){
					ba1 = ba;
				}
				if(ba.getValueID().equals("终端通讯访问端口")){
					ba2 = ba;
				}
			}
			
		} catch (Exception e) {
			errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取配置文件,Getconfig" );
		}
        //系统端IP地址及端口号
        config += "app-server:" + ba1.getValueName() + ":" + ba2.getValueName() + "\r\n";

        //上游终端号码
        config += "upstream-number:" + GetTels(IMEI) + "\r\n";

        //维护终端号码，如果设置了该号码，设备掉电时将往该号码发送通知短信
        config += "miantain-number:" + "" + "\r\n";

        //下游终端号码，保留
        config += "downstream-number:" + "" + "\r\n";
        
//        terminalMapper= (TerminalMapper) applicationContext.getBean("terminalMapper");
        Terminal_Conditions tc = new Terminal_Conditions();
        try {
			tc = terminalMapper.getTerminalCondition(IMEI);
		} catch (Exception e) {
			errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取配置文件,Getconfig" ,e);
		}
        //音频通道默认音量
        config += "chan-volume:" + tc.getMp3Volume() + "," + tc.getFmVolume() + "," + tc.getGsmVolume() + "," + tc.getNullVolume() + "\r\n";
        sqlSession.close();
        return config;
    }

    //获取升级文件
    private byte[] GetUpgrade()
    {
    	String filepath = Tools.GetValueByKey(Const.CONFIG, "updateurl");
//    	String filepath = "F:/BroadcastService/Update/upgrade.bin";
    	if (filepath != null & filepath != "")
        {
        	byte[] buffer = null;  
        	ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);  
        	FileInputStream fis = null ;  
            try {  
                File file = new File(filepath);  
                fis = new FileInputStream(file);  
                byte[] b = new byte[1024];  
                int n;  
                while ((n = fis.read(b)) != -1) {  
                    bos.write(b, 0, n);  
                }  
                buffer = bos.toByteArray();  
                return buffer;
            } catch (FileNotFoundException e) {  
            	errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取升级文件,GetUpgrade" );
            	return null;
            } catch (IOException e) {  
            	errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:获取升级文件,GetUpgrade" );
            	return null;
            }finally
            {
            	if (bos != null)
            	{
            		try {
            			bos.close();
            		} catch (IOException e) {
            			System.out.print("文件资源关闭出错");
            		}  
            	}
                if (fis != null)
                {
                	try {
						fis.close();
					} catch (IOException e) {
						System.out.print("文件资源关闭出错");
					}  
                }
            }

        } else  {
            return null;
        }

    }
    private byte[] GetFiles(String filepath)
    {

    	 FileInputStream fis = null ;  
         ByteArrayOutputStream bos = new ByteArrayOutputStream(1024*2);
        if (filepath != null & filepath != "")
        {
        	byte[] buffer = null;  
            try {  
                File file = new File(filepath);
                if(file.exists()){
                	fis = new FileInputStream(file);  
                	byte[] b = new byte[1024*2];  
                	int n;  
                	while ((n = fis.read(b)) != -1) {  
                		bos.write(b, 0, n);  
                	}  
                	buffer = bos.toByteArray();  
                }else{
                	errorlogger.error("出错日志  记录:下载文件 "+filepath+"不存在   信息:下载文件,GetFiles" );
                }
                return buffer;
                
            } catch (FileNotFoundException e) {  
            	errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:下载文件,GetFiles" );
            	return null;
            } catch (IOException e) {  
            	errorlogger.error("出错日志  记录:" + e.getMessage().toString() + "  信息:下载文件,GetFiles" );
            	return null;
            }
            finally
            {
            	if (bos != null)
            	{
            		try {
            			bos.close();
            		} catch (IOException e) {
            			System.out.print("文件资源关闭出错");
            		}  
            	}
                if (fis != null)
                {
                	try {
						fis.close();
					} catch (IOException e) {
						System.out.print("文件资源关闭出错");
					}  
                }
            }

        } else
        {
            return null;
        }
    }
}
