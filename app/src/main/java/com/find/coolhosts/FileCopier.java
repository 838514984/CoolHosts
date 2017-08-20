package com.find.coolhosts;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import com.stericson.RootTools.RootTools;

public class FileCopier extends AsyncTask<Object, Void, Boolean>
{
	private CoolHosts callback;
    private static final String TAG = FileCopier.class.getSimpleName();
    private boolean isCopy;
	public FileCopier (CoolHosts callback)
	{
		this.callback = callback;
		isCopy=false;
	}
    /**inputs0: 拷贝的源地址
     * inputs1: 要拷贝到的地址*/
	@Override
	protected Boolean doInBackground (Object... inputs)
	{

		BufferedReader bufferedReader = null;
		Process process = null;
		try
		{
			String[] mountLocation = SystemMount.getMountLocation();

			final Runtime runtime = Runtime.getRuntime();
			process = runtime.exec("su");
            //写入目的文件
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("mount -o rw,remount -t " + mountLocation[1] + " " + mountLocation[0] + " /system\n");
            os.writeBytes("chmod 666 /system/etc/hosts\n");
            os.flush();
            if(inputs[0]!=null){
                isCopy=true;
                RootTools.copyFile(inputs[0].toString(), inputs[1].toString(), false, true);
			}else{
                //empty hosts
                os.writeBytes("echo '127.0.0.1 localhost' > '" + inputs[1] + "'\n");
			}
			os.writeBytes("chmod 666 /system/etc/hosts\n");
//            os.writeBytes("mount -o ro,remount -t " + mountLocation[1] + " " + mountLocation[0] + " /system\n");
			os.writeBytes("exit\n");
			os.flush();
			os.close();

			process.waitFor();

			int exitValue = process.exitValue();
			Log.d(TAG, "Exit Value For File Copier: " + exitValue);
			if (exitValue != 255 && exitValue != 126)
				return Boolean.TRUE;

		} catch (InterruptedException ex)
		{
			Log.e(TAG, ex.getMessage(), ex);
		} catch (IOException ex)
		{
			Log.e(TAG, ex.getMessage(), ex);
		} catch (UnableToMountSystemException ex)
		{
			Log.e(TAG, ex.getMessage(), ex);
		} finally
		{
			closeStream(bufferedReader);
			if(process != null)
				process.destroy();
		}

		return Boolean.FALSE;
	}

	private Reader getReader (Object input) throws FileNotFoundException
	{
		if(input instanceof  InputStream)
			return new InputStreamReader((InputStream) input);
		if(input instanceof  String)
			return new FileReader((String)input);
		else throw new FileNotFoundException("Unknown file type. " + input);
	}

	private void closeStream (Closeable closeable)
	{
		try
		{
			if (closeable != null)
				closeable.close();
		} catch (IOException ex)
		{
			Log.e(TAG, "Error closing stream. ", ex);
		}
	}

	@Override
	protected void onPostExecute (Boolean success)
	{
		if (success){
			//如果是复制，则设置按钮的动态
			if(isCopy){
				callback.setOneKeyState(360);
				Lib.isSuccessed=true;
			}
			callback.appendOnConsole(callback.getConsole(),true,R.string.copysuccess);
		}else
			callback.appendOnConsole(callback.getConsole(),true,R.string.copyfailed);
		callback.doNextTask();
	}

}
