package com.find.coolhosts;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

@SuppressLint("SetJavaScriptEnabled")
public class AdPage extends Activity{
	private WebView webview;
	private ProgressBar progressBar;
	private String weburl;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adpage);
		weburl=this.getIntent().getStringExtra("url");
		progressBar=(ProgressBar)findViewById(R.id.ad_progressBar);
		
		webview=(WebView)findViewById(R.id.webview);
		webview.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本  
        webview.getSettings().setBuiltInZoomControls(false);//设置使支持缩放  
        webview.loadUrl(weburl);  
        webview.setWebViewClient(new WebViewClient(){  
            @Override  
            public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            	 progressBar.setVisibility(View.VISIBLE);
                view.loadUrl(url);// 使用当前WebView处理跳转  
                return true;//true表示此事件在此处被处理，不需要再广播  
            }  
            @Override   //转向错误时的处理  
            public void onReceivedError(WebView view, int errorCode,  
                    String description, String failingUrl) {  
//                Toast.makeText(CoolHosts.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();  
            }  
        });
        

        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.e("newProgress", newProgress+"");
                progressBar.setProgress(newProgress);
                if(newProgress >= 100){
                    progressBar.setVisibility(View.GONE);
                }
//                super.onProgressChanged(view, newProgress);
            }
            
        });		
        webview.setDownloadListener(new MyWebViewDownLoadListener());
		
	}
	/**@description 支持从内置的web浏览器里下载*/
	private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }

    }
}
