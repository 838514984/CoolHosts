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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SendGetApplication extends AsyncTask<Integer, Void, String> {
	CoolHosts caller;
	public SendGetApplication(CoolHosts caller) {
		this.caller=caller;
	}
	@Override
	protected String doInBackground(Integer... url) {
		HttpClient httpclient=null;
		String getUrl=Lib.COOLHOSTS_VERSION_INFO;
		HttpGet get=null;
		StringBuilder builder = new StringBuilder();
		try{
			httpclient=new DefaultHttpClient();
			get=new HttpGet(getUrl);
			HttpResponse response=httpclient.execute(get);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line+"\n");
				}
				Log.v("Getter", "Your data: " + builder.toString()); //response data
			} else {
				Log.e("Getter", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
            e.printStackTrace();
	    } catch (IOException e) {
	            e.printStackTrace();
	    }
		return builder.toString();
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
}
