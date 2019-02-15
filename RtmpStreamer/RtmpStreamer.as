package {

import flash.display.MovieClip;
import flash.external.ExternalInterface;
import flash.net.NetConnection;
import flash.events.NetStatusEvent;
import flash.net.NetStream;
import flash.media.Video;
import flash.media.Microphone;
import flash.events.StatusEvent;
import flash.text.*; 
import fl.controls.ComboBox;
import fl.data.DataProvider;
import	flash.media.SoundCodec;
//import flash.media.H264Profile;
//import flash.media.H264VideoStreamSettings;

public class RtmpStreamer extends MovieClip {

    internal var nc:NetConnection;
    internal var ns:NetStream;
    internal var nsPlayer:NetStream;
	internal var vidPlayer:Video;
    internal var mic:Microphone;   

	
	/*使用 Speex 编解码器时的编码语音品质
	品质值	所需的比特率（KB/秒）
	0	3.95
	1	5.75
	2	7.75
	3	9.80
	4	12.8
	5	16.8
	6	20.6
	7	23.8
	8	27.8
	9	34.2
	10	42.2
	*/
    internal var _micQuality:int = 8;
	//rate 麦克风捕获声音时使用的速率，单位是 kHz
	//rate 值	实际频率
	//44	44,100 Hz
	//22	22,050 Hz
	//11	11,025 Hz
	//8		8,000 Hz
	//5		5,512 Hz
    internal var _micRate:int = 16;

    internal var _screenWidth:int = 200;
    internal var _screenHeight:int = 110;
	
	internal var _text:String="录音未开始";
	internal var _myText:TextField;
	internal var _micindex:int=-1;
	internal var cbMicSelection:ComboBox;
	
    public function RtmpStreamer() {
		initShowText(_text);
		initMicList();
		ExternalInterface.addCallback("setShowText", setShowText);
        ExternalInterface.addCallback("setScreenSize", setScreenSize);
        
        ExternalInterface.addCallback("setMicQuality", setMicQuality);
        ExternalInterface.addCallback("setMicRate", setMicRate);

        ExternalInterface.addCallback("publish", publish);
        ExternalInterface.addCallback("play", playVideo);
        ExternalInterface.addCallback("disconnect", disconnect);

        ExternalInterface.call("setSWFIsReady");
		
    }
	public function initMicList():void {
				var format:TextFormat = new TextFormat(); 
            format.size = 15; 
			var mytext:TextField= new TextField(); 
			mytext.defaultTextFormat = format; 
			mytext.text = "请选择麦克风："; 
			mytext.width = 200; 
           // mytext.height = 50;
			mytext.y = 20;
			mytext.x = 20;
			addChild(mytext);		
		
		var miclist:Array  = Microphone.names;
		cbMicSelection = new ComboBox();
		var micData:Array = new Array();
		var i:int=0;
		for(i = 0; i < miclist.length; i++) {
			//cbMicSelection.addItemAt(miclist[i],i);
			micData.push( {label:miclist[i], data:i} );
		}
		//var cbStyles:Object = ComboBox.getStyleDefinition();
		//var bW:Number = cbStyles.buttonWidth as Number
		// cbMicSelection.restrict="";
		cbMicSelection.dataProvider = new DataProvider(micData);
		cbMicSelection.drawNow();
		cbMicSelection.width = 150;
		//cbMicSelection.width = cbMicSelection.textField.textWidth + 35;
		cbMicSelection.dropdownWidth = cbMicSelection.textField.textWidth+ 10;
		cbMicSelection.move(20, 45);
		addChild(cbMicSelection);
	}
	public function initShowText(text:String):void {
       		
			var format:TextFormat = new TextFormat(); 
            format.size = 23; 
			
			_myText = new TextField();			
			/*_myText.width = 500; 
            _myText.height = 200; 
			_myText.y = 50;*/
			_myText.background = false;
            _myText.wordWrap = true;
			_myText.autoSize =  TextFieldAutoSize.CENTER; 
            _myText.defaultTextFormat = format; 
			_myText.text = text; 
			_myText.textColor= 0x000000; 
			_myText.width = 200; 
            _myText.height = 100; 
			_myText.y = 90;
			_myText.x = 20;
			addChild(_myText);
    }
	public function setShowText(text:String):void {
        _myText.text = text; 
    }
     public function setScreenSize(width:int, height:int):void {
        _screenWidth = width;
        _screenHeight = height;
    }	
   
    public function setMicQuality(quality:int):void {
        _micQuality = quality;
    }

    public function setMicRate(rate:int):void {
        _micRate = rate;
    }

    public function publish(url:String, name:String,text:String):void {
        this.connect(url, name, function (name:String):void {
			//打开提示框
		 //ExternalInterface.call("openRemind");
		 _text=text;
            publishCamera(name);
			
        });
    }

    public function playVideo(url:String, name:String):void {
        this.connect(url, name, function (name:String):void {
            displayPlaybackVideo(name);
        });
    }

    public function disconnect(text:String):void {
		setShowText(text);
        nc.close();
    }

    private function connect(url:String, name:String, callback:Function):void {
        nc = new NetConnection();
        nc.addEventListener(NetStatusEvent.NET_STATUS, function (event:NetStatusEvent):void {
            ExternalInterface.call("console.log", "try to connect to " + url);
            ExternalInterface.call("console.log", event.info.code);
            if (event.info.code == "NetConnection.Connect.Success") {
                callback(name);
            }else if (event.info.code != "NetConnection.Connect.Closed"){
				ExternalInterface.call("streamError", event.info.code);
			}
        });
        nc.connect(url);
    }

    private function publishCamera(name:String):void {
//            Mic

		// _micindex = int( cbMicSelection.selectedIndex );
        mic = Microphone.getMicrophone(cbMicSelection.selectedIndex);
        

        /* The rate at which the microphone is capturing sound, in kHz. Acceptable values are 5, 8, 11, 22, and 44. The default value is 8 kHz
         * if your sound capture device supports this value. Otherwise, the default value is the next available capture level above 8 kHz that
         * your sound capture device supports, usually 11 kHz.
         *
         */
        mic.rate = _micRate;
		
		//Nellymoser
		//mic.codec = SoundCodec.NELLYMOSER;
		
		//Speex
		mic.codec = SoundCodec.SPEEX;
		mic.encodeQuality = _micQuality;
		//mic.enableVAD = true;
		mic.setSilenceLevel(0);
		
		//当应用程序调用 Microphone.getMicrophone() 方法时，Flash Player 将显示“Flash Player 设置”对话框，它提示用户允许或拒绝 Flash Player 对系统上的摄像头和麦克风的访问。
        //在用户单击此对话框中的“允许”或“拒绝”按钮后，将调度 StatusEvent。该 StatusEvent 实例的 code 属性指示是允许还是拒绝对麦克风的访问，
		mic.addEventListener(StatusEvent.STATUS, this.onMicStatus); 
		
		//指定用户是已经拒绝对麦克风的访问 (true) 还是已经允许对麦克风的访问 (false)。
		if(!mic.muted){
			setShowText(_text);
			ExternalInterface.call("mrophoneIsOpen");
		}else{
			setShowText("浏览器不允许使用麦克风!");
		}
		//将麦克风音频传送到本地扬声器
		// mic.setUseEchoSuppression(true); 
		// mic.setLoopBack(true);

        ns = new NetStream(nc);
        ns.attachAudio(mic);
        ns.publish(name, "live");
    }
 //麦克风允许
	private function onMicStatus(event:StatusEvent):void {
	 
		if (event.code == "Microphone.Unmuted") 
		{ 
			trace("Microphone access was allowed."); 
			setShowText(_text);
			ExternalInterface.call("mrophoneIsOpen");
		} 
		else if (event.code == "Microphone.Muted") 
		{ 
			trace("Microphone access was denied."); 
			setShowText("浏览器不允许使用麦克风!");
		} 
		//关闭提示框
		// ExternalInterface.call("closeRemind");
	}

    private function displayPlaybackVideo(name:String):void {
        nsPlayer = new NetStream(nc);
        nsPlayer.play(name);
        vidPlayer = getPlayer();
        vidPlayer.attachNetStream(nsPlayer);
        addChild(vidPlayer);
    }

    private function getPlayer():Video {
        vidPlayer = new Video(_screenWidth, _screenHeight);
        vidPlayer.x = 0;
        vidPlayer.y = 0;

        return vidPlayer;
    }

}

}
