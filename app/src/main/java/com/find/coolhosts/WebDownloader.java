package com.find.coolhosts;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class WebDownloader extends AsyncTask<String, Void, File>{
	CoolHosts caller;
	public WebDownloader(CoolHosts caller) {
		this.caller=caller;
	}
	@Override
	protected File doInBackground(String... params) {
		File f = null;
        try {
            URL url = new URL(params[0]);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            
            f = new File(CoolHosts.CACHEDIR, params[1]);
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(baf.toByteArray());
            fos.close();
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return f;
	}
	@Override
    public void onPostExecute(File f) {
        if (f != null) {
			Log.d(CoolHosts.TAG, "download success");
			caller.appendOnConsole(caller.getConsole(), true, R.string.downsuccuess);
			caller.setOneKeyState(180);
			caller.doNextTask();
        }
    }

}
