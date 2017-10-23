package com.sg.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;

/**
 * �Ѿ�������  û��
 * @author lsy �����ݵ�����U����
 */
public class Export_His_Data {
	public static String fileName = "";
	File file;

	public boolean is_has() {

		file = new File(fileName);
		if (!file.exists()) {
			try {

				file.createNewFile();
			} catch (IOException e) {
				System.out.println("123����ʧ��");
				return false;
			}
		} else {
			System.out.println("�ļ�����");
		}

		if (!file.canRead()) {
			System.out.println("123���ܶ�");
			return false;
		}

		if (!file.canWrite()) {
			System.out.println("123����д");
			return false;
		}

		return true;
	}

	public void exportData(String oldFileName) throws Exception {
		File f = new File(oldFileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(f)));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file)));
		String str = null;
		while ((str = br.readLine()) != null) {
			bw.write(str);
		}
		
		br.close();
		bw.close();

	}

}
