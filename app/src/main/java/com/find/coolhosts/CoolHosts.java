package com.find.coolhosts;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;

public class CoolHosts extends Activity {
  
	private boolean root;
	private TextView console,versionConsole;
	private Button ad,customHosts,customIP,clearHosts,help,catHosts;
	private LoadingButton oneKey;
	private ScrollView scrollView;
	
	public static final String TAG="coolhosts";
	private boolean netState=false;
	public CheckCoolHostsVersion getVersion;
	private ButtonListener btnListener;
	
	/**
	 * GetHostsVersion: 获取服务器上的hosts版本
     * GetCHVersion: 获取服务器上coolhosts的最新版本号*/
	private enum TASK
	{
		DOWNHOSTS,COPYNEWHOSTSFROMWEB,COPYNEWHOSTSFROMLOCAL,DELETEOLDHOSTS,GETCHVERSION,GETHOSTSVERSION,AFTERWORK
	}
	private Queue <TASK> taskQueue=null;
	
	@SuppressLint("SetJavaScriptEnabled") 
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main);
        Lib.CACHEDIR=getFilesDir().toString();
        Log.v(TAG, Lib.CACHEDIR);
        setButtons();
        taskQueue = new LinkedList<TASK>();
        //检查CoolHosts的版本
        try {
        	getVersion=new CheckCoolHostsVersion(CoolHosts.this);
			getVersion.getLocalVersion();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
        // 调试时，注释掉这个任务，以免因而我i过多地访问服务器而导致ip被封禁
        taskQueue.add(TASK.GETHOSTSVERSION);
        taskQueue.add(TASK.GETCHVERSION);
        doNextTask();
    }
	public void setButtons(){
		btnListener=new ButtonListener();
		ad=(Button)findViewById(R.id.ad);
		ad.setOnClickListener(btnListener);
		customHosts=(Button)findViewById(R.id.customehosts);
		customHosts.setOnClickListener(btnListener);
		customIP=(Button)findViewById(R.id.readfromfile);
		customIP.setOnClickListener(btnListener);
		clearHosts=(Button)findViewById(R.id.clearHosts);
		clearHosts.setOnClickListener(btnListener);
        catHosts = (Button)findViewById(R.id.catHosts);
        catHosts.setOnClickListener(btnListener);
		help=(Button)findViewById(R.id.help);
		help.setOnClickListener(btnListener);
		console=(TextView)findViewById(R.id.console);
		console.setMovementMethod(new ScrollingMovementMethod());
		versionConsole=(TextView)findViewById(R.id.versionConsole);
		oneKey=(LoadingButton)findViewById(R.id.onekey);
		oneKey.setOnClickListener(btnListener);
		scrollView=(ScrollView)findViewById(R.id.scrollView);
		scrollView.post(new Runnable() {
			   @Override
			   public void run() {
			    // TODO Auto-generated method stub
			    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
			   }
			  });
	}
    @Override
    public void onResume (){
    	super.onResume();
    	root=RootChecker.hasRoot();
    	oneKey.setCallback(new LoadingButton.Callback() {
            @Override
            public void complete() {
            	String note=Lib.isSuccessed?getString(R.string.updatehostssuccessed):getString(R.string.updatehostsfailed);
                Toast.makeText(getApplicationContext(),note,Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**Update the console textview
     * 更新console
     * @param textview: 更新哪个textview
     * @param isAppend: 追加还是覆盖
     * @param id: R.string.值*/
    public void appendOnConsole(TextView textview,boolean isAppend,final int ...id ){
    	if(!isAppend)textview.setText("");
    	for(int i:id){
    		textview.append(getString(i)+"\n");
    	}
    }
    public void appendOnConsole(TextView textview,boolean isAppend,final String ...strs){
    	if(!isAppend)textview.setText("");
    	for(String tempstr:strs)
    		textview.append(tempstr+"\n");
    }
  
    
	public TextView getConsole(){return console;}
	public TextView getVersionConsole(){return versionConsole;}
	public boolean getNetState() {
		return netState;
	}
	/**设置网络状态*/
	public void setNetState(boolean netState) {
		this.netState = netState;
	}  
	/**CoolHosts版本更新*/
	public void showVersion(){
		AlertDialog.Builder builderAbout = new AlertDialog.Builder(CoolHosts.this);
		builderAbout.setMessage(getString(R.string.local_version)+Lib.LOCALCHVERSION+"\n"+getString(R.string.remote_version)+Lib.REMOTECHVERSION+"\n"+Lib.UPDATE_INFO.replace("#", "\n"));
		builderAbout.setTitle(R.string.updatechversion);
		builderAbout.setCancelable(true);
		builderAbout.setPositiveButton("更新", new DialogInterface.OnClickListener(){
			@Override
			public void onClick (DialogInterface dialog, int which){
				Uri uri =Uri.parse(Lib.COOLHOSTS_UPDATE_LINK);
				DownloadManager dm= (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
				DownloadManager.Request req= new DownloadManager.Request(uri);
				req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
				req.setDestinationInExternalFilesDir(CoolHosts.this, Environment.DIRECTORY_DOWNLOADS, "");
				req.setTitle("CoolHosts.apk");
		    	req.setDescription("下载完后请点击打开");
		    	req.setMimeType("application/vnd.android.package-archive");
		    	dm.enqueue(req);
				dialog.cancel();
				
			}});
		AlertDialog alertAbout = builderAbout.create();
		alertAbout.show();
	}
	public void doNextTask(){
		if(taskQueue!=null && taskQueue.peek()!=null){
			switch(taskQueue.remove()){
			case COPYNEWHOSTSFROMWEB:
				appendOnConsole(getConsole(),true,R.string.copyingnewhosts);
				new FileCopier(CoolHosts.this).execute(Lib.CACHEDIR + "/hosts", Lib.HOSTSPATH);
				break;
			case DELETEOLDHOSTS:
				appendOnConsole(getConsole(),true,R.string.deleteoldhosts);
				new FileCopier(CoolHosts.this).execute(null,  Lib.HOSTSPATH);
				break;
			case DOWNHOSTS:
				appendOnConsole(getConsole(),false,R.string.downloadhosts);
				new WebDownloader(CoolHosts.this).execute(Lib.SOURCE,Lib.HOSTSINCACHE);
				break;
			case GETCHVERSION:
				new SendGetApplication(CoolHosts.this).execute();
				break;
			case GETHOSTSVERSION:
				new GetHostsVersion(CoolHosts.this).execute();
				break;
			case COPYNEWHOSTSFROMLOCAL:
				appendOnConsole(getConsole(),true,R.string.copyingnewhosts);
				new FileCopier(CoolHosts.this).execute(Lib.LOCALCUSTOMHOSTSPATH, Lib.HOSTSPATH);
				break;
			case AFTERWORK:
				// 格式统一下
				appendOnConsole(versionConsole,false,getString(R.string.local_version)+Lib.getlocalversion());
				appendOnConsole(versionConsole,true,getString(R.string.remote_version)+Lib.getRemoteVersion());
				break;
			default:
				break;
			
			}
		}
	}
	/**oncreate时从服务器获取coolhosts version和更新链接等信息，在此处处理*/
	public void checkCoolHostsVersion() {
        String[] ans = Lib.echoBuffer.split("\n");
        Lib.SHOWADPAGE = ans[0];
        /**由于主机服务商设置，可能会出现防火墙，导致网站连不上，但是会获取一些杂乱的信息，此处排除这种可能*/
        if (ans[1].indexOf('.') > 0 && ans[1].length() < 10) {
            Lib.REMOTECHVERSION = ans[1] == null ? "" : ans[1];
            Lib.COOLHOSTS_UPDATE_LINK = ans[2] == null ? "" : ans[2];
            Lib.UPDATE_INFO = ans[3] == null ? "" : ans[3];
            Log.e(TAG, Lib.UPDATE_INFO);
            // 比较两个版本号
            String[] version_remote = Lib.REMOTECHVERSION.trim().split("\\.");
            String[] version_local = Lib.LOCALCHVERSION.trim().split("\\.");
            Log.e(CoolHosts.TAG, Lib.REMOTECHVERSION+"\n"+Lib.LOCALCHVERSION);
            int compareLength = Math.min(version_remote.length, version_local.length);
            boolean show_version = false;
            for (int i = 0; i < compareLength; i++) {
                int vr = Integer.valueOf(version_remote[i]);
                int vl = Integer.valueOf(version_local[i]);
                if (vr > vl) {
                    show_version = true;
                    break;
                }
            }
            if (show_version)
                showVersion();
        }
    }
	public void setOneKeyState(int num){
		oneKey.setTargetProgress(num);
	}
	@SuppressLint("NewApi")
	private final class ButtonListener implements View.OnClickListener{
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.onekey:
				if(!root){
					Toast.makeText(CoolHosts.this, R.string.unrooted, Toast.LENGTH_SHORT).show();
				}else{
					//从本地文件更新
					if(Lib.UPDATEMODE==1 && !Lib.LOCALCUSTOMHOSTSPATH.equals("")){
						appendOnConsole(console, false, getString(R.string.readfromfilenote)+Lib.LOCALCUSTOMHOSTSPATH);
						taskQueue.add(TASK.DELETEOLDHOSTS);
						taskQueue.add(TASK.COPYNEWHOSTSFROMLOCAL);
						taskQueue.add(TASK.AFTERWORK);
						doNextTask();
					}else{
						/**判断网络状态*/
						if(getNetState()){
							oneKey.setOnClickDefault();
							oneKey.callOnClick();
							taskQueue.add(TASK.DOWNHOSTS);
							taskQueue.add(TASK.DELETEOLDHOSTS);
							taskQueue.add(TASK.COPYNEWHOSTSFROMWEB);
							taskQueue.add(TASK.AFTERWORK);
							doNextTask();
						}else{
							Toast.makeText(CoolHosts.this, R.string.neterror, Toast.LENGTH_SHORT).show();
						}
					}
				}
				break;
            // 更新为分享功能
			case R.id.ad:
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "我正在使用CoolHosts一键修改我的hosts，你也试试吧 http://www.findspace.name/easycoding/503 ");
                shareIntent.setType("text/plain");

                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "快分享给你的小伙伴吧！"));
                break;
			case R.id.customehosts:
				final EditText et = new EditText(CoolHosts.this);
				new AlertDialog.Builder(CoolHosts.this).setTitle("请输入源地址")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et).setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
					    	 public void onClick(DialogInterface dialog, int which) {
					    		 Lib.SOURCE=et.getText().toString();
					    		 CoolHosts.this.appendOnConsole(getConsole(), true, R.string.customhostsaddressnote);
					    		 Toast.makeText(CoolHosts.this, "Host源已经切换，仅此次有效，重启应用后恢复为默认的findspace的源", Toast.LENGTH_SHORT).show();
					    	 }})
					     .setNegativeButton("取消", null).show();
				break;
//			case R.id.customehosts:
//				Intent intent_custom = new Intent(CoolHosts.this, ManageSourceList.class);
//				CoolHosts.this.startActivityForResult(intent_custom, 1);
//				break;
			case R.id.readfromfile:
				Intent intent3 = new Intent(Intent.ACTION_GET_CONTENT);
				intent3.setType("*/*");
				intent3.addCategory(Intent.CATEGORY_OPENABLE);
				try {
					startActivityForResult(Intent.createChooser(intent3, "Choose File"), Lib.FILE_SELECT_CODE);
				} catch (android.content.ActivityNotFoundException ex) {
					Toast.makeText(CoolHosts.this, "No File Manager Founded", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.clearHosts:
				taskQueue.add(TASK.DELETEOLDHOSTS);
				doNextTask();
				break;
			case R.id.help:
                Log.d(TAG, "点击help");
				Intent intent2=new Intent(CoolHosts.this,AdPage.class);
                intent2.putExtra("url","http://www.findspace.name/easycoding/503");
                CoolHosts.this.startActivity(intent2);
				break;
			case R.id.catHosts:
				Intent intentCatHosts=new Intent(CoolHosts.this,CatHosts.class);
				CoolHosts.this.startActivity(intentCatHosts);
				break;

			}
		}
		
	}
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        // TODO Auto-generated method stub  
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case Lib.FILE_SELECT_CODE:
                    if(data.getData()!=null){
                        Uri uri=data.getData();
                        Lib.LOCALCUSTOMHOSTSPATH=uri.getPath();
                        Toast.makeText(this, "现在你可以点击一键更新从本地更新了～", Toast.LENGTH_SHORT).show();
                        Lib.UPDATEMODE=1;
                    }
                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);  
    }
    protected void show_Toast(String note){
        Toast.makeText(getApplicationContext(),note,Toast.LENGTH_SHORT).show();
    }
    protected void show_dialog(String title, String content){
        new AlertDialog.Builder(CoolHosts.this).setTitle(title).setMessage(content).setPositiveButton("确定",null).show();
    }
}









