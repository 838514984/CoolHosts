package com.find.coolhosts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

public final class Lib {
	/**Hosts源地址*/
	public static String SOURCE="http://googleips-google.stor.sinaapp.com/hosts";
	public static final String HOSTS_VERSION_URL="http://googleips-google.stor.sinaapp.com/updateTime";
	static final String REMOTEVERSION="";
	private static final String TAG=Lib.class.getSimpleName();
	static final String NOT_EXIST="Don't Exist.";
	static final String READ_ERROR="Read Error.";
	static final String TIMEMARK_HEAD="#+UPDATE_TIME";
	static final String LOG_NAME="CoolHosts";
	public static final String HOSTSPATH="/system/etc/hosts";
	public static String REMOTE_VERSION="";
	public static String LOCALCHVERSION="";
	public static String REMOTECHVERSION="";
	static final String EMPTH_STRING="";
	static final String MY_AD_UNIT_ID="ca-app-pub-8527554614606787/5603479557";
	/**获取的字符串buffer，包括要展示的页面链接，版本号，新版本下载地址*/
	public static String echoBuffer="";
	public static String COOLHOSTS_UPDATE_LINK="";
	public static String UPDATE_INFO="";
	//testid
//	static final String MY_AD_UNIT_ID="ca-app-pub-3940256099942544/1033173712";
	//存在本地文件名
	public static final String HOSTSINCACHE="hosts";
	public static String SHOWADPAGE;
	/**自定义本地文件源的路径*/
	public static String LOCALCUSTOMHOSTSPATH="";
	/**是否成功*/
	public static boolean isSuccessed=false;
	/**读取本地hosts文件时需要的返回码*/
	public static final int FILE_SELECT_CODE=2;
	/**更新模式
	 * @value 0:默认hosts源;1:本地文件源*/
	public static int UPDATEMODE=0;
    /**Get The local version mark
     * getExternalCacheDir
     * */
    public static String getlocalversion(){
    	String versionText="";
    	File f=new File("/system/etc/hosts");
    	try {
    		FileReader freader2=new FileReader(f);
    		BufferedReader breader2=new BufferedReader(freader2);
			int i=0;
			String tempstr;
			boolean getit=false;
			while(i<5){
				tempstr=breader2.readLine();
				if(tempstr!=null&&tempstr.contains(Lib.TIMEMARK_HEAD)){
					versionText=tempstr.substring(Lib.TIMEMARK_HEAD.length()+1);
					getit=true;
					break;
				};
				i++;
			}
			Log.d(TAG, "Got local hosts");
			if (!getit) {
				versionText=Lib.NOT_EXIST;
			}
			breader2.close();
		} catch (FileNotFoundException e) {
			versionText=Lib.NOT_EXIST;
			e.printStackTrace();
		} catch (IOException e) {
			versionText=Lib.READ_ERROR;
			e.printStackTrace();
		}
    	return versionText;
    }
		
	public static String getRemoteVersion(){
		return REMOTE_VERSION;
	}
	
	
}
