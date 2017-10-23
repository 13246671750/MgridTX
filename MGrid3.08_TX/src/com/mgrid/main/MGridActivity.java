package com.mgrid.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.mgrid.data.DataGetter;
import com.mgrid.util.LabelUtils;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtIniReader;
import com.sg.uis.SgAlarmAction;
import com.sg.uis.SgAlarmChangTime;
import com.sg.uis.LsyNewView.AlarmShieldTime;
import comm_service.service;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("InlinedApi")
public class MGridActivity extends Activity {


	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		m_oViewGroups = new HashMap<String, MainWindow>();
		m_oPageList = new ArrayList<String>();
		whatLanguage = whatLanguage();
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//ǿ��Ϊ����

		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//���뷨����

		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);//�Ը�window����Ӳ������.

		//System.out.println("�Ǻ�");
		final IntentFilter filter = new IntentFilter(); 
		filter.addAction(Intent.ACTION_SCREEN_ON);//������Ļ ��
		filter.addAction(Intent.ACTION_SCREEN_OFF);//������Ļ ��
		//ͨ���㲥��������Ϣ
		BroadcastReceiver BroastcastScreenOn = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				
				
				if (arg1.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				
					if (m_bTakePhoto) {
						Intent cameraIntent = new Intent(MGridActivity.this,
								CameraActivity.class);
						startActivity(cameraIntent);	 
					}

				}
				if (arg1.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
					 onPageChange(m_sMainPage);
				}
			}
		};
		getApplicationContext().registerReceiver(BroastcastScreenOn, filter);
		
	
		//new UsbReceiver(this);
		
		//������Ļ���
		mContainer = new ContainerView(this);

		MainWindow.SCREEN_WIDTH = 1024; // VTU screen width
		MainWindow.SCREEN_HEIGHT = 768; // VTU screen height
		
		
//		new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
				
				//�ҳ����ε��豸
				LabelUtils labelUtils=new LabelUtils(MGridActivity.this);	
				if(LabelList==null)
				LabelList = labelUtils.getButtonId();
			
			
//			} 
//		});  

		
		
        //����Mgrid.ini
		UtIniReader iniReader = null;
		try {
			iniReader = new UtIniReader(Environment
					.getExternalStorageDirectory().getPath() + "/MGrid.ini");
		} catch (Exception e) {
			iniReader = null;
			e.printStackTrace();
			new AlertDialog.Builder(this)
					.setTitle("����")
					.setMessage(
							"��ȡ�����ļ� [ MGrid.ini ] �쳣��ֹͣ���أ�\n���飺" + e.toString())
					.show();
		}

		if (iniReader == null) {
			return;
		}

		m_sRootFolder = iniReader.getValue("SysConf", "FolderRoot");
		m_sMainPage = iniReader.getValue("SysConf", "MainPage");
		m_UserName = iniReader.getValue("SysConf", "UserName","admin");
		m_PassWord = iniReader.getValue("SysConf", "PassWord","12348765");
		m_pageUserName = iniReader.getValue("SysConf", "MaskPageUser","admin");
		m_MaskCount=Integer.parseInt(iniReader.getValue("SysConf", "MaskCount", "0")) ;
		if(m_MaskCount==0)
		{
			//Toast.makeText(MGridActivity.this, "��ǰû��Ȩ��ҳ��", 500).show();
			m_MaskPage=new String [1][1];
			m_pagePassWord=new String[1];
			m_MaskPage[0][0]=iniReader.getDefPageValue("SysConf", "MaskPage");
			m_pagePassWord[0]=iniReader.getValue("SysConf", "MaskPagePassword","admin");
		 			
		}
		else
		{
			m_MaskPage=new String [m_MaskCount][]; 
			m_pagePassWord=new String[m_MaskCount];
		}
				
		for(int i=0;i<m_MaskCount;i++)
		{
			m_MaskPage[i] = iniReader.getPageValue("SysConf", "MaskPage"+(i+1));
			m_pagePassWord[i]=iniReader.getValue("SysConf", "MaskPagePassword"+(i+1),"admin");
		}
		

		m_bHasRandomData = Boolean.parseBoolean(iniReader.getValue("SysConf",
				"HasRandomData"));
		m_bBitmapHIghQuality = Boolean.parseBoolean(iniReader.getValue(
				"SysConf", "BitmapHIghQuality"));
		m_bErrMsgParser = !Boolean.parseBoolean(iniReader.getValue("SysConf",
				"NoErrMsgParser"));
		m_bShowLoadProgress = Boolean.parseBoolean(iniReader.getValue(
				"SysConf", "ShowLoadProgress", "true")); 
  
		try {
			tmp_load_int_time = Integer.parseInt(iniReader.getValue("SysConf",
					"LoadingInterval"));
		} catch (java.lang.NumberFormatException e) {
			tmp_load_int_time = 200;
		}

		try {
			MainWindow.SWITCH_STYLE = Integer.parseInt(iniReader.getValue(
					"SysConf", "UseAnimation"));
		} catch (java.lang.NumberFormatException e) {
			MainWindow.SWITCH_STYLE = 0;
		}

		try {
			m_bCanZoom = Boolean.parseBoolean(iniReader.getValue("SysConf",
					"WindowCanZoom"));
		} catch (Exception e) {
			m_bCanZoom = true;
		}
		try {
			m_bTakePhoto = Boolean.parseBoolean(iniReader.getValue("SysConf",
					"TakePhoto"));
		} catch (Exception e) {
			m_bTakePhoto = false;
		}
		
		try {
			m_bTakeEMail = Boolean.parseBoolean(iniReader.getValue("SysConf",
					"TakeEMail"));
		} catch (Exception e) {
			m_bTakeEMail = false;
		}
		try {
			m_sEMailTitle = iniReader.getValue("SysConf", "EMailTitle");
			m_sEMailVTUnumber = iniReader.getValue("SysConf", "EMailVTUnumber");
			m_sEMailToAddress = iniReader.getValue("SysConf", "EMailToAddress");
		} catch (Exception e) {
			m_sEMailTitle = "-VTU�ʼ��澯-";
			m_sEMailVTUnumber = "vtu001"; // �ʼ������е�vtu���,
			m_sEMailToAddress = "vtu007@sina.com";// �ʼ����͵ĵ�ַ
		}

		CFGTLS.BITMAP_HIGHQUALITY = m_bBitmapHIghQuality;

		String strIPC_IP = iniReader.getValue("NetConf", "IPC_IP");
		if (null != strIPC_IP && !strIPC_IP.trim().isEmpty()) {
			service.IP = strIPC_IP.trim();
		}

		try {
			int port = Integer.parseInt(iniReader.getValue("NetConf",
					"IPC_PORT"));
			service.PORT = port;
		} catch (java.lang.NumberFormatException e) {
		}

		
		
		String line = "";
		MainWindow page = null;
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(Environment
							.getExternalStorageDirectory().getPath()
							+ m_sRootFolder + "pagelist"), "gb2312"));  //��ȡҳ���б�

			DataGetter.bIsLoading = true;
			for (int i = 0; i < 1024; i++) {

				if ((line = reader.readLine()) == null)
					break;

				line = line.trim(); 
		 		if (line.isEmpty())
					continue;

				m_oPageList.add(line);

				if (!line.equals(m_sMainPage)) {
					continue;
				}

				page = new MainWindow(this);
				page.m_strRootFolder = m_sRootFolder;//·��
				page.m_bHasRandomData = m_bHasRandomData;//�Ƿ�ʹ���������
				page.loadPage(line);
				page.active(false);

				page.setVisibility(View.GONE);
				m_oViewGroups.put(line, page); 
				mContainer.addView(page, 1024, 768); 

			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			new AlertDialog.Builder(this)
					.setTitle("����")
					.setMessage(
							"����ҳ�� [ " + line + " ] �����쳣��ֹͣ���أ�\n���飺"
									+ e.toString()).show();
		}

		m_oSgSgRenderManager = m_oViewGroups.get(m_sMainPage);
		if (null == m_oSgSgRenderManager) {
			new AlertDialog.Builder(this).setTitle("����")
					.setMessage("�Ҳ�����ҳ [ " + m_sMainPage + " ] ��").show();
		}

		if (0 != mContainer.getChildCount() && null != m_oSgSgRenderManager) {
			// m_oPageList.trimToSize();

			m_oSgSgRenderManager.active(true);
			// setContentView(m_oSgSgRenderManager);

			mContainer.setClipChildren(false);
			mContainer.mCurrentView = m_oSgSgRenderManager;
			m_oSgSgRenderManager.setVisibility(View.VISIBLE);
			//m_oSgSgRenderManager.requestFocus();

			requestWindowFeature(Window.FEATURE_NO_TITLE); // ȡ������
			getWindow().getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);// ȫ������
			getWindow()
					.setFlags(
							WindowManager.LayoutParams.FLAG_FULLSCREEN
									| WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
							WindowManager.LayoutParams.FLAG_FULLSCREEN
									| WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
			// Window������Ӳ������
			// setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);

			setContentView(mContainer);
			mContainer.requestFocus();

			showTaskUI(false);

			getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);// ���android����̵�ס��������⣡

			if (m_oPageList.size() == 1) {
				tmp_flag_loading = false;
				DataGetter.bIsLoading = false;
				Toast.makeText(this, "�������", Toast.LENGTH_LONG).show();

			} else {
				String strPageName = m_oPageList.get(m_sMainPage
						.equals(m_oPageList.get(0)) ? 1 : 0);
				strPageName = strPageName
						.substring(0, strPageName.length() - 4);
				showLoadProgToast("����  [ " + (tmp_load_pageseek + 1) + "/"
						+ m_oPageList.size() + " ] , ���ڼ���  [ " + strPageName
						+ " ]  ...", Toast.LENGTH_SHORT);
			}

			final Handler handler = new Handler();
			Runnable runnable = new Runnable() { 
				public void run() {
					// m_oSgSgRenderManager.notifylistflush();

					String pagename = m_oPageList.get(tmp_load_pageseek);

					if (pagename.equals(m_sMainPage)) {

						MainWindow mainPage = m_oViewGroups.get(pagename);
						mainPage.m_oPrevPage = tmp_load_prevpage;
						if (null != tmp_load_prevpage)
							tmp_load_prevpage.m_oNextPage = mainPage;
						tmp_load_prevpage = mainPage;

						tmp_load_pageseek++;
						if (m_oPageList.size() > tmp_load_pageseek)
							pagename = m_oPageList.get(tmp_load_pageseek);
						else {
							tmp_flag_loading = false;
							DataGetter.bIsLoading = false;
							Toast.makeText(MGridActivity.this, "�������",
									Toast.LENGTH_LONG).show();
							
							return;
						}
					}

					MainWindow page = new MainWindow(MGridActivity.this);
					page.m_strRootFolder = m_sRootFolder;
					page.m_bHasRandomData = m_bHasRandomData;

					try {
						page.loadPage(pagename);
						page.active(false);

						page.setVisibility(View.GONE);
						m_oViewGroups.put(pagename, page);
						mContainer.addView(page, 1024, 768);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						tmp_flag_loading = false;
						DataGetter.bIsLoading = false;
						new AlertDialog.Builder(MGridActivity.this)
								.setTitle("����")
								.setMessage(
										"����ҳ�� [ " + pagename
												+ " ] �����쳣��ֹͣ���أ�\n���飺"
												+ e.toString()).show();
						return;
					}

					page.m_oPrevPage = tmp_load_prevpage;
					if (null != tmp_load_prevpage)
						tmp_load_prevpage.m_oNextPage = page;
					tmp_load_prevpage = page;

					tmp_load_pageseek++;

					if (m_oPageList.size() > tmp_load_pageseek) {
						String nextPage = m_oPageList.get(tmp_load_pageseek);
						if (m_sMainPage.equals(nextPage)) {
							if (m_oPageList.size() > tmp_load_pageseek + 1) {
								nextPage = m_oPageList
										.get(tmp_load_pageseek + 1);
								nextPage = nextPage.substring(0,
										nextPage.length() - 4);
								showLoadProgToast("����  [ "
										+ (tmp_load_pageseek + 1) + "/"
										+ m_oPageList.size() + " ] , ���ڼ���  [ "
										+ nextPage + " ]  ...",
										Toast.LENGTH_SHORT);
							}
						} else {
							nextPage = nextPage.substring(0,
									nextPage.length() - 4);
							showLoadProgToast("����  [ "
									+ (tmp_load_pageseek + 1) + "/"
									+ m_oPageList.size() + " ] , ���ڼ���  [ "
									+ nextPage + " ]  ...", Toast.LENGTH_SHORT);
						}

				 		handler.postDelayed(this, tmp_load_int_time);
					} else {
						tmp_flag_loading = false;
						DataGetter.bIsLoading = false;
						isChangPage=true;
						Toast.makeText(MGridActivity.this, "�������",
								Toast.LENGTH_LONG).show();
					
						isNOChangPage=true;
					}

				} // end of run
			};
  
			handler.postDelayed(runnable, tmp_load_int_time);
			// */
		} else {
			requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			requestWindowFeature(Window.FEATURE_PROGRESS);
			setContentView(R.layout.main);
			setProgressBarVisibility(true);
			setProgressBarIndeterminateVisibility(true);
		}

		mContainer
				.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);

		DataGetter.currentPage = m_sMainPage;
		mDataGetter = new DataGetter();
		//System.out.println("123�������߳�MgridActivity��onCreate");
		mDataGetter.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		mDataGetter.start();
		 
		
		
		// xmlPhoneNumber=getXmlPhoneNumber();
	} // end of onCreate 
	
    
	



	// ��ȡϵͳ����
	private boolean whatLanguage() {
		Locale locale = getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		if (language.endsWith("zh"))	
			return true;		
		else
			return false; 
	}

	// �õ�������IP��ַ
	public static String getLocalIP() {
		String IP = null;
		StringBuilder IPStringBuilder = new StringBuilder();
		try {
			// NetworkInterface��ʾ����Ӳ���������ַ
			Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface
					.getNetworkInterfaces();
			while (networkInterfaceEnumeration.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaceEnumeration
						.nextElement();
				Enumeration<InetAddress> inetAddressEnumeration = networkInterface
						.getInetAddresses();
				while (inetAddressEnumeration.hasMoreElements()) {
					InetAddress inetAddress = inetAddressEnumeration
							.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						IPStringBuilder.append(inetAddress.getHostAddress()
								.toString() + "\n");
					}
				}
			}
		} catch (SocketException ex) {

		}

		IP = IPStringBuilder.toString();
		return IP;
	}

	// �޸�����
	public static void changPassWord(String type, String newPassWord) {	
			textReplace(type, m_PassWord, newPassWord,-1);
			m_PassWord = newPassWord;
	}
	
	public static void changPassWord(String type, String newPassWord,int count) {
			textReplace(type, m_pagePassWord[count-1], newPassWord,count);
			m_pagePassWord[count-1]= newPassWord;

	}
	
	//�ı��滻
	public static void textReplace(String type, String oldText, String newText,int count) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(Environment
							.getExternalStorageDirectory().getPath()
							+ "/MGrid.ini"), "gb2312"));
			String MGridTxt = "";
			String line;
			while ((line = reader.readLine()) != null) {
				MGridTxt = MGridTxt + line + "&&&";
			}
			if(count!=-1)
			{
				MGridTxt = MGridTxt.replaceAll(type +count+"=" + oldText, type +count+ "="
					+ newText);
			}
			else
			{
				MGridTxt = MGridTxt.replaceAll(type + "=" + oldText, type + "="
						+ newText);
			}
			
			String[] MGridArgs = MGridTxt.split("&&&");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(Environment
							.getExternalStorageDirectory().getPath()
							+ "/MGrid.ini"), "gb2312"));
			for (int i = 0; i < MGridArgs.length; i++) {
			//	System.out.println(MGridArgs[i]);
				writer.write(MGridArgs[i]);
				writer.newLine();
			}
			writer.flush();
			writer.close();
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	

	public void applyRotation(final String pagename, float start, float end) {
		// Find the center of the container
		final float centerX = mContainer.getWidth() / 2.0f;
		final float centerY = mContainer.getHeight() / 2.0f;

		// �ṩ��������һ���µ�3D��������
		// �����������������������һ������
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 310.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(pagename));

		mContainer.startAnimation(rotation);		
	}

	public void onPageChange(String pagename) {
		/*
		 * InputMethodManager
		 * imm=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE
		 * ); imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
		 * InputMethodManager.HIDE_NOT_ALWAYS);
		 */

		if (null == m_oViewGroups.get(pagename)) {
			if (tmp_flag_loading)
				new AlertDialog.Builder(this).setTitle("��ʾ��")
						.setMessage("Ŀ��ҳ�����ڼ����� ��").show();
			else
				new AlertDialog.Builder(this).setTitle("����")
						.setMessage("�޷��ҵ���̬ҳ�棺 " + pagename).show();

			return;
		}

		// if("".equals(m_maskPage) || pagename.equals(m_maskPage)){
		// return;
		// }

		m_oSgSgRenderManager.active(false);
		m_oSgSgRenderManager = m_oViewGroups.get(pagename);
		m_oSgSgRenderManager.active(true);

		// ����ʹ��������ʾ View ������������������� -- CharlesChen May 8, 2014.
		// setContentView(m_oSgSgRenderManager);

		mContainer.mCurrentView.setVisibility(View.GONE);
		mContainer.mCurrentView = m_oSgSgRenderManager;
		mContainer.mCurrentView.setVisibility(View.VISIBLE);
		// mContainer.bringChildToFront(mContainer.mCurrentView);

		DataGetter.currentPage = pagename;
		
		
		LabelUtils labelUtils=new LabelUtils(MGridActivity.this);
		labelUtils.setDoubleButton(m_oSgSgRenderManager);  //Ϊ��DoubleImageButton�ؼ����� 
		Iterator<String> iter = m_oSgSgRenderManager.m_mapUIs.keySet().iterator();
		while (iter.hasNext()) {
			String strKey = iter.next();
			IObject obj = m_oSgSgRenderManager.m_mapUIs.get(strKey);
            if(obj.getType().equals("AlarmAction"))
            {
            	SgAlarmAction sg=(SgAlarmAction) obj;
            	sg.updateText();
            }else if(obj.getType().equals("SgAlarmChangTime"))
            {
            	SgAlarmChangTime sa=(SgAlarmChangTime) obj;
            	SgAlarmAction sg=(SgAlarmAction) MGridActivity.AlarmAll.get(sa.label);
				if(sg!=null)
				{
					sa.updateText(sg.TimeLapse);
				}
            }else if(obj.getType().equals("AlarmShieldTime"))
            {
            	AlarmShieldTime ast=(AlarmShieldTime)obj;
            	if(MGridActivity.AlarmShieldTimer.get(ast.equitId+"_"+ast.eventId)!=null)
            	{
            		ast.updateText();
            	}
            }
			obj.initFinished();
		}		
	}

	/** ��ʾ/��������˵� */
	public void showTaskUI(boolean bShow) {
		if (m_oTaskIntent == null)
			m_oTaskIntent = new Intent();
		if (bShow) {
			m_oTaskIntent
					.setAction("android.intent.action.STATUSBAR_VISIBILITY");
			m_oSgSgRenderManager.getContext().sendBroadcast(m_oTaskIntent);
		} else {
			m_oTaskIntent
					.setAction("android.intent.action.STATUSBAR_INVISIBILITY");
			m_oSgSgRenderManager.getContext().sendBroadcast(m_oTaskIntent);
		}
	}

	/** ����һЩˢ��UI�ĺ��� **/
	@Override
	protected void onResume() {
		super.onResume();
		if (m_oSgSgRenderManager == null)
			return;
		showTaskUI(false);
	}

	/** ��Ϣ��ʾ��ʾ **/
	void showToast(CharSequence msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/** ��Ϣ��ʾ��ʾ **/
	void showLoadProgToast(CharSequence msg, int duration) {
		if (m_bShowLoadProgress)
			Toast.makeText(this, msg, duration).show();
	}

	// ҳ��finishʱִ��
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		restartApplication();
		
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		//showTaskUI(true);
	}

	

	// ����Ӧ��
	private void restartApplication() {
		final Intent intent = getPackageManager().getLaunchIntentForPackage(
				getPackageName());
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}	

	// ������
	public int tmp_load_int_time = 200;
	public int tmp_load_pageseek = 0;
	public boolean tmp_flag_loading = true;
	public MainWindow tmp_load_prevpage = null;

	// Params:
	public String m_sMainPage = null;
	public String m_sRootFolder = null;
	public static boolean m_bHasRandomData = false;
	public static boolean m_bBitmapHIghQuality = false;
	public static boolean m_bShowLoadProgress = true;
	public static boolean m_bErrMsgParser = true;
	public static boolean m_bCanZoom = true;
	public static boolean m_bTakePhoto = false;
	public static boolean m_bTakeEMail = false; // �Ƿ�ʵʱ�澯�ʼ�����
	public static String m_sEMailTitle = "-VTU�ʼ��澯";// �ʼ��ı���
	public static String m_sEMailVTUnumber = "vtu001"; // �ʼ������е�vtu���
	public static String m_sEMailToAddress = "vtu007@sina.com";// �ʼ����͵ĵ�ַ
	//�澯����ʱ�䱣��
	public static HashMap<String,HashMap<Long,String>> AlarmShieldTimer=new HashMap<String, HashMap<Long,String>>();
	

	public ArrayList<String> m_oPageList = null;
	private Intent m_oTaskIntent = null;
	private MainWindow m_oSgSgRenderManager = null;
	private HashMap<String, MainWindow> m_oViewGroups = null;

	public static HashMap<String,ArrayList<String>> AlarmShow=new HashMap<String, ArrayList<String>>();
	public static boolean isNOChangPage=false;
	// ϵͳ����
	public static boolean whatLanguage = true;
	// xml�ļ�����ĸ���
	// public static int xmlPhoneNumber=0;

	// �û���������
	public static String m_UserName;
	public static String m_PassWord;

	// ҳ��Ȩ���û���������
	public static String m_pageUserName;
	public static String[] m_pagePassWord;

	public static String[][] m_MaskPage;//Ȩ��ҳ���ڵ���ҳ��
	public static int m_MaskCount;//��Ȩ��ҳ��ĸ���
	
	//
	public static HashMap<String,IObject> AlarmAll=new HashMap<String, IObject>();
	public static File  all_Event_file=new File("/mgrid/data/Command/0.log");
	private ContainerView mContainer;

	private DataGetter mDataGetter;

	public InputMethodManager mImm = null;
	
	public static ProgressDialog dialog;
	
	public static boolean isChangPage=false;
	
	public static ArrayList<String> LabelList=null; 
	
	public static HashMap<String, stBindingExpression> m_DoubleButton=null;
	//public static Map<IObject, stBindingExpression>    m_Label=null;
	
	public static ExecutorService xianChengChi = Executors.newCachedThreadPool();
	
	
	public static Handler handlerT= new Handler() {
        public void handleMessage(Message msg) {
                // handler���յ���Ϣ��ͻ�ִ�д˷���
                switch (msg.what) {
                case 1:
                        dialog.dismiss();
                        // �ر�ProgressDialog
                        break;
                default:
                        break;
                }
                super.handleMessage(msg);
        }
};

	/**
	 * ���´���Ϊ�ڲ��� This class listens for the end of the first half of the
	 * animation. It then posts a new action that effectively swaps the views
	 * when the container is rotated 90 degrees and thus invisible.
	 */
	private final class DisplayNextView implements Animation.AnimationListener {
		private final String mPageName;

		private DisplayNextView(String pagename) {
			mPageName = pagename;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			mContainer.post(new SwapViews(mPageName));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * ���ฺ���л�View���� ��ִ�к�벿�ֵĶ���Ч��
	 */
	private final class SwapViews implements Runnable {
		private final String mPageName;

		public SwapViews(String pagename) {
			mPageName = pagename;
		}

		public void run() {
			final float centerX = mContainer.getWidth() / 2.0f;
			final float centerY = mContainer.getHeight() / 2.0f;
			Rotate3dAnimation rotation;

			onPageChange(mPageName);

			/*
			 * �����л��Ƕȵĳ��� if (mPosition > -1) {
			 * mPhotosList.setVisibility(View.GONE);
			 * mImageView.setVisibility(View.VISIBLE);
			 * mImageView.requestFocus();
			 * 
			 * rotation = new Rotate3dAnimation(90, 180, centerX, centerY,
			 * 310.0f, false); } else { mImageView.setVisibility(View.GONE);
			 * mPhotosList.setVisibility(View.VISIBLE);
			 * mPhotosList.requestFocus();
			 * 
			 * rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 310.0f,
			 * false); }
			 */

			// TODO: ��ʱ������Բ�ܷ���Ч�����Ժ���ʱ���ٵ�У���Ч�� -- CharlesChen
			rotation = new Rotate3dAnimation(270, 360, centerX, centerY,
					310.0f, false); // �ٴ��Ż��� ��270�ȿ�ʼ�����ɱ��ⷴת��

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mContainer.startAnimation(rotation);
		}
	} /* end of class SwapViews */

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == 1) {

		}

	}

}