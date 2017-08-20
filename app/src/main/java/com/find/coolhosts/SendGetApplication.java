package com.find.coolhosts;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SendGetApplication extends AsyncTask<Integer, Void, String> {
	CoolHosts caller;
	public SendGetApplication(CoolHosts caller) {
		this.caller=caller;
	}
	@Override
	protected String doInBackground(Integer... temp) {
        try {
            Log.e("Getter", "获取coolhosts版本信息");
            URL url = new URL(Lib.COOLHOSTS_VERSION_INFO);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(4000);
            urlConnection.setConnectTimeout(7000);
            int responseCode = urlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                InputStream is = urlConnection.getInputStream();
                String res = getStringFromInputStream(is);
                Log.v("Getter", "Your data: " + res); //response data
                return res;
            }
            Log.e("Getter", "Failed to get page");
        } catch (MalformedURLException e) {
            Log.e(CoolHosts.TAG, "wrong links when get version");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(result==null)Log.e(CoolHosts.TAG, "没有信息");
		else{
			Lib.echoBuffer=result;
			caller.checkCoolHostsVersion();
		}
			caller.doNextTask();
	}
    /**根据流返回字符串信息
     * @author: http://www.eoeandroid.com/thread-543421-1-1.html*/
    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 模板代码 必须熟练
        byte[] buffer = new byte[1024];
        int len = -1;
        // 一定要写len=is.read(buffer)
        // 如果while((is.read(buffer))!=-1)则无法将数据写入buffer中
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
        os.close();
        return state;
    }
}
