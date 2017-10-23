package com.mgrid.uncaughtexceptionhandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mgrid.main.MGridActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class CrashHandler implements UncaughtExceptionHandler {

	private static CrashHandler instance;
	private Context context;

	private CrashHandler() {
	};

	public synchronized static CrashHandler getInstance() {
		if (instance == null)
			instance = new CrashHandler();
		return instance;
	}

	public void init(Context context) {
		this.context = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		arg1.printStackTrace(printWriter);
		Throwable cause = arg1.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		final String result = writer.toString();
		System.err.println("�쳣��" + result);
		save(result);
		// new Thread() {
		// @Override
		// public void run() {
		// Looper.prepare();
		// Toast toast = Toast.makeText(context, "������������˳�:\r\n" + result,
		// Toast.LENGTH_LONG);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		// toast.show();
		// // MsgPrompt.showMsg(mContext, "���������", msg+"\n��ȷ���˳�");
		// Looper.loop();
		// }
		// }.start();

		Intent intent = new Intent();
		intent.setAction("android.intent.action.STATUSBAR_VISIBILITY");
		context.sendBroadcast(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);

	}

	private void save(String result) {
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String time = formatter.format(new Date());
			String fileName = "AndroidException.log";
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				String path = "/sdcard/crash/";
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File f = new File(path + fileName);
				FileOutputStream fos=null;
				if (f.length() >= 1048576) {
					fos = new FileOutputStream(path + fileName);
				} else {
					fos = new FileOutputStream(
							path + fileName, true);
				}
				fos.write((time + "  " + result).getBytes());
				fos.close();
			}

		} catch (Exception e) {
			System.out.println("д�ļ�ʧ��");
		}

	}

}
