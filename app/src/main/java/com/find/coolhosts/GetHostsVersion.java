package com.find.coolhosts;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class GetHostsVersion extends AsyncTask<Integer, Void, String>{
	CoolHosts caller;
	public GetHostsVersion(CoolHosts caller) {
		this.caller=caller;
	}
	@Override
	protected String doInBackground(Integer... params) {
		try {
			URL url=new URL(Lib.HOSTS_VERSION_URL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
			return reader.readLine();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		Lib.REMOTE_VERSION=result;
		caller.appendOnConsole(caller.getVersionConsole(),false,caller.getString(R.string.local_version)+Lib.getlocalversion());
		caller.appendOnConsole(caller.getVersionConsole(),true,caller.getString(R.string.remote_version)+Lib.getRemoteVersion());
		caller.appendOnConsole(caller.getConsole(), false, "");
		caller.setNetState(true);
		caller.doNextTask();
	}

}
