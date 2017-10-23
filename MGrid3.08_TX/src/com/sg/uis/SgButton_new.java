package com.sg.uis;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lsy.ViewStyle.ButtonM;
import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.R;
import com.mgrid.main.SoundService;
import com.mgrid.util.ShellUtils;
import com.mgrid.util.ShellUtils.CommandResult;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stBindingExpression;
import com.sg.common.UtExpressionParser.stExpression;

/** ��ť */
@SuppressLint({ "ShowToast", "InflateParams", "RtlHardcoded", "ClickableViewAccessibility" })
public class SgButton_new extends TextView implements IObject {

	public SgButton_new(Context context) {
		super(context);

	

		m_oPaint = new Paint();
		m_rBBox = new Rect();
 

        button = new ButtonM(context);
        //������ɫ
        button.setTextColori(android.graphics.Color.WHITE);
        //�����С
        button.setTextSize(14);
        
        //�����Ƿ�ΪԲ��
        button.setFillet(true);
        //����Բ�ǵİ뾶��С
        button.setRadius(18);
        //����ɫ
        button.setBackColor(Color.parseColor("#7067E2"));
        //ѡ�еı���ɫ
        button.setBackColorSelected(Color.parseColor("#3C3779"));
        //������ʾ
        button.setOnClickListener(l);
   
        
       
	}
	OnClickListener l=new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			onClicked();
		}
	};
	

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		if (m_bPressed) {
			int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
			int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

			m_oPaint.setColor(0x500000F0);
			m_oPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
		}
		super.onDraw(canvas);
	}

	@Override
	public void doLayout(boolean bool, int l, int t, int r, int b) {
		if (m_rRenderWindow == null)
			return;
		int nX = l
				+ (int) (((float) m_nPosX / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nY = t
				+ (int) (((float) m_nPosY / (float) MainWindow.FORM_HEIGHT) * (b - t));
		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (r - l));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (b - t));

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			button.layout(nX, nY, nX + nWidth, nY + nHeight);
			
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
		rWin.addView(button);
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue,
			String strResFolder) {
		if ("ZIndex".equals(strName)) {
			m_nZIndex = Integer.parseInt(strValue);
			if (MainWindow.MAXZINDEX < m_nZIndex)
				MainWindow.MAXZINDEX = m_nZIndex;
		} else if ("Location".equals(strName)) {
			String[] arrStr = strValue.split(",");
			m_nPosX = Integer.parseInt(arrStr[0]);
			m_nPosY = Integer.parseInt(arrStr[1]);
		} else if ("Size".equals(strName)) {
			String[] arrSize = strValue.split(",");
			m_nWidth = Integer.parseInt(arrSize[0]);
			m_nHeight = Integer.parseInt(arrSize[1]);
		} else if ("Alpha".equals(strName)) {
			m_fAlpha = Float.parseFloat(strValue);
		} else if ("BackgroundColor".equals(strName)) {
			if (strValue.isEmpty())
				return;
			m_cBackgroundColor = Color.parseColor(strValue);
			// this.setBackgroundColor(m_cBackgroundColor);
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			button.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("FontSize".equals(strName)) {
			float fWinScale = (float) MainWindow.SCREEN_WIDTH
					/ (float) MainWindow.FORM_WIDTH;
			m_fFontSize = Float.parseFloat(strValue) * fWinScale;
		//	this.setTextSize(Float.parseFloat(strValue));
			button.setTextSize(Float.parseFloat(strValue));
		} else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
		//	this.setTextColor(m_cFontColor);
		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("CmdExpression".equals(strName))
			m_strCmdExpression = strValue;

		else if ("HorizontalContentAlignment".equals(strName))
			m_strHorizontalContentAlignment = strValue;
		else if ("VerticalContentAlignment".equals(strName))
			m_strVerticalContentAlignment = strValue;
		else if ("Expression".equals(strName)) {
			// m_strCmdExpression = strValue; //fjw add //ע�͵�
		}
	}

	@Override
	public void initFinished() {
		int nFlag = Gravity.NO_GRAVITY;
		if ("Left".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.LEFT;
		else if ("Right".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.RIGHT;
		else if ("Center".equals(m_strHorizontalContentAlignment))
			nFlag |= Gravity.CENTER_HORIZONTAL;

		if ("Top".equals(m_strVerticalContentAlignment))
			nFlag |= Gravity.TOP;
		else if ("Bottom".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.BOTTOM;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize());
			setPadding(0, (int) padSize, 0, 0);
		} else if ("Center".equals(m_strVerticalContentAlignment)) {
			nFlag |= Gravity.CENTER_VERTICAL;
			double padSize = CFGTLS.getPadHeight(m_nHeight,
					MainWindow.FORM_HEIGHT, getTextSize()) / 2f;
			setPadding(0, (int) padSize, 0, (int) padSize);
		}

		setGravity(nFlag);
	}

	public String getBindingExpression() {
		return m_strCmdExpression;
	}

	public void setUniqueID(String strID) {
		m_strID = strID;
	}

	public void setType(String strType) {
		m_strType = strType;
	}

	public String getUniqueID() {
		return m_strID;
	}

	public String getType() {
		return m_strType;
	}

	private void onClicked() {
		// ����¼�
		if ("".equals(m_strClickEvent) == false) {
			if ("��ʾ����".equals(m_strClickEvent)) {
				// ����Homeָ��
				if (m_oHomeIntent == null) {
					m_oHomeIntent = new Intent();
					m_oHomeIntent.setAction("android.intent.action.MAIN");
					m_oHomeIntent.addCategory("android.intent.category.HOME");
				}
				this.getContext().startActivity(m_oHomeIntent);

				if (m_rRenderWindow != null)
					m_rRenderWindow.showTaskUI(true);
			} else if ("��ʾIP".equals(m_strClickEvent)) {
				Toast.makeText(m_rRenderWindow.getContext(),
						MGridActivity.getLocalIP(), Toast.LENGTH_SHORT).show();
			} else if ("ɾ����ʷ".equals(m_strClickEvent)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("��ʾ"); 
				builder.setMessage("�Ƿ�ȷ��ɾ����ʷ��¼?");
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								deleteDir(new File("/mgrid/log"));

								showHint();
							}
						});
				builder.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								Toast.makeText(getContext(), "����ȡ��", 1000)
										.show();
							}
						});
				builder.create().show();

				// restartApplication();

			} else if ("��������".equals(m_strClickEvent)) {

				dialog = ProgressDialog.show(getContext(), "��ʾ",
						"���ڵ���,�벻Ҫ������������");
				new Thread(new Runnable() {

					@Override
					public void run() {
						String[] commands = new String[] { "sh /data/mgrid/lsy_usb_out.sh &" };
						CommandResult result = ShellUtils.execCommand(commands,
								false);
						System.out.println(result.errorMsg);

					}
				}).start();

			} else if ("��תӦ��".equals(m_strClickEvent)) {
				// ���� ������֪��
				ComponentName componetName = new ComponentName(
				// ���������һ��Ӧ�ó���İ���
						"com.mcu.iVMSHD",
						// ���������Ҫ������Activity
						"com.mcu.iVMSHD.activity.LoadingActivity");
				Intent intent = new Intent();
				// ���һ��������ʾ��apk1����ȥ��
				// Bundle bundle = new Bundle();
				// bundle.putString("arge1", "������ת�����ģ�����apk1");
				// intent.putExtras(bundle);
				intent.setComponent(componetName);
				getContext().startActivity(intent);
				if (m_rRenderWindow != null)
					m_rRenderWindow.showTaskUI(true);

				// ֻ֪����������֪������
				// PackageManager packageManager =
				// getContext().getPackageManager();
				// PackageInfo pi = null;
				// try {
				// pi = packageManager.getPackageInfo("com.mcu.iVMSHD", 0);
				// } catch (NameNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				// Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
				// resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
				// resolveIntent.setPackage(pi.packageName);
				// List<ResolveInfo> apps =
				// packageManager.queryIntentActivities(resolveIntent, 0);
				// ResolveInfo ri = apps.iterator().next();
				// if(ri!=null)
				// {
				// String className = ri.activityInfo.name;
				// System.out.println(className);
				// Intent intent = new Intent(Intent.ACTION_MAIN);
				// intent.addCategory(Intent.CATEGORY_LAUNCHER);
				// ComponentName cn = new ComponentName("com.mcu.iVMSHD",
				// className);
				// intent.setComponent(cn);
				// getContext().startActivity(intent);
				// if (m_rRenderWindow != null)
				// m_rRenderWindow.showTaskUI(true);
				// }

			} else if (m_strClickEvent.equals("�رո澯")) {
               
				Intent intent = new Intent(m_rRenderWindow.m_oMgridActivity,
						SoundService.class);
				intent.putExtra("playing", false);
				m_rRenderWindow.m_oMgridActivity.startService(intent); 
				
			} else {
				String[] arrStr = m_strClickEvent.split("\\(");
				boolean isMask = true;// �����ж��Ƿ�ΪȨ��ҳ��
				boolean isNeedPW = true; // �����ж�Ȩ��ҳ���Ƿ���Ҫ����
				if (m_rRenderWindow != null && arrStr[0].equals("Show")) {
					int count = -1;
					String[] str = arrStr[1].split("\\)");
					// �˴�ѭ�����������ҳ���ǰȨ��ҳ�����ڵ���Ȩ��ҳ��
					if (MGridActivity.m_MaskPage != null) {
						for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {

							for (String s : MGridActivity.m_MaskPage[i]) {
								if (s.equals(DataGetter.currentPage)) {
									count = i;
									break;
								}
							}
							if (count != -1)
								break;
						}

						if (count != -1) {
							// �˴�ѭ�����ж���Ҫ��ת��ҳ��͵�ǰҳ���ǲ�����ͬһ����Ȩ��ҳ��
							for (String s : MGridActivity.m_MaskPage[count]) {
								if (s.equals(str[0] + ".xml")) // �����ǰҳ��ΪȨ��ҳ�棨ֻ֧����һ����Ȩ��ҳ�棩
								{
									isNeedPW = false;

								}
							}
						}

						if (isNeedPW) {
							// �˴�ѭ�����ж���Ҫ��תҳ���Ƿ�ΪȨ��ҳ�档
							for (int i = 0; i < MGridActivity.m_MaskPage.length; i++) {
								for (String s : MGridActivity.m_MaskPage[i]) {
									if (!s.equals("1")) {
										if ((s.substring(0, s.length() - 4))
												.equals(str[0])) {
											MaskCount = i;
											showPassDialog();
											isMask = false;
											break;
										}
									}
								}
								if (!isMask)
									break;
							}

						}
					}

					if (isMask) {
						m_rRenderWindow.changePage(str[0]);
					}

				}
			}
		}

		// ����ҳ
		if ("".equals(m_strUrl) == false) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(m_strUrl));
			this.getContext().startActivity(intent);
		}

		// ���Ϳ������� Ŀǰ���ֻ֧�֣�ң��ֵ��һ���Ķ�󶨣�
		if ("".equals(m_strCmdExpression) == false) {

			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("��ʾ");
			builder.setMessage("�Ƿ�ȷ��?");
			builder.setPositiveButton("ȷ��",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							synchronized (m_rRenderWindow.m_oShareObject) {
								deal_cmd();
								if ("".equals(cmd_value) == false) {
									// Log.e("button_onclick->cmd_value:",
									// cmd_value);
									// ��ӹ㲥��Ϣ
									// �����������������ӵ��������������� fjw add
									m_rRenderWindow.m_oShareObject.m_mapCmdCommand
											.put(getUniqueID(), cmd_value);

								}
							}

						}
					});
			builder.setNegativeButton("ȡ��",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {

							Toast.makeText(getContext(), "����ȡ��", 1000).show();
						}
					});
			builder.create().show();

		}

	}

	// ��ʾ�û�Ȩ�޽���Ի���
	public void showPassDialog() {
		// LayoutInflater��������layout�ļ����µ�xml�����ļ�������ʵ����
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow
				.getContext());
		// ��activity_login�еĿؼ�������View��
		final View textEntryView = factory.inflate(R.layout.page_xml, null);
		// ��LoginActivity�еĿؼ���ʾ�ڶԻ�����
		new AlertDialog.Builder(m_rRenderWindow.getContext())
		// �Ի���ı���
				.setTitle("�û�Ȩ�޵�¼")
				// �趨��ʾ��View
				.setView(textEntryView)
				// �Ի����еġ���½����ť�ĵ���¼�
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						// ��ȡ�û�����ġ��û������������롱
						// ע�⣺textEntryView.findViewById����Ҫ����Ϊ����factory.inflate(R.layout.activity_login,
						// null)��ҳ�沼�ָ�ֵ����textEntryView��
						// final EditText etUserName =
						// (EditText)textEntryView.findViewById(R.id.etuserName);
						final EditText etPassword = (EditText) textEntryView
								.findViewById(R.id.pageet);

						// ��ҳ��������л�õġ��û������������롱תΪ�ַ���
						// String userName =
						// etUserName.getText().toString().trim();
						String password = etPassword.getText().toString()
								.trim();
						if (password
								.equals(MGridActivity.m_pagePassWord[MaskCount])) {
							String[] arrStr = m_strClickEvent.split("\\(");
							if (m_rRenderWindow != null
									&& "Show".equals(arrStr[0])) {
								String[] arrSplit = arrStr[1].split("\\)");
								m_rRenderWindow.changePage(arrSplit[0]);
							}
						} else {
							Toast.makeText(m_rRenderWindow.getContext(),
									"�������", Toast.LENGTH_SHORT).show();
							// Toast.makeText(m_rRenderWindow.getContext(),
							// "Incorrect username or password!",
							// Toast.LENGTH_SHORT).show();
						}
					}

				})
				// �Ի���ġ��˳��������¼�
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// LoginActivity.this.finish();
					}
				})

				// �Ի���Ĵ�������ʾ
				.create().show();
	}

	// ɾ����ʷ�ļ�
	private void deleteDir(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();

			for (File f : files) {
				deleteDir(f);
			}
		}
		dir.delete();
		//isDelete = "�ɹ�";
		// Toast.makeText(getContext(), "hahhaha"+is, 1000).show();
	}

	//
	private void showHint() {
		new AlertDialog.Builder(getContext())
				.setTitle("ɾ��" + isDelete + ",��������������")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub

					}
				}).create().show();
	}

	// ����Ӧ��
	
	private void restartApplication() {
		m_rRenderWindow.m_oMgridActivity.finish();// ҳ��finishʱ�������onDestory()����
		Toast.makeText(getContext(), "����Ӧ��", 1000).show();
	}

	// fjw add ��ť��������ܵĿ�������İ󶨱��ʽ����
	// �������ؼ����ʽ�����ؿؼ����ʽ��
	public boolean deal_cmd() {
		// if("".equals(m_strCmdExpression)) return false;
		// if(m_strCmdExpression==null) return false;
		stExpression oMathExpress = UtExpressionParser.getInstance()
				.parseExpression(m_strCmdExpression);
		if (oMathExpress != null) {
			// �����ؼ����ʽ�������ݵ�Ԫ���ʽ��
			Iterator<HashMap.Entry<String, stBindingExpression>> it = oMathExpress.mapObjectExpress
					.entrySet().iterator();
			while (it.hasNext()) {
				stBindingExpression oBindingExpression = it.next().getValue();
				cmd_value = oBindingExpression.strValue;
			}
		}
		return true;
	}

	@Override
	public void updateWidget() {
	}

	@Override
	public boolean updateValue() {
		return false;
	}

	@Override
	public boolean needupdate() {
		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	public static Handler handler = new Handler() {
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


	// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 7;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	int m_cBackgroundColor = 0xF00CF00C;
	String m_strContent = "��ť";
	String m_strFontFamily = "΢���ź�";
	float m_fFontSize = 12.0f;
	boolean m_bIsBold = false;
	int m_cFontColor = 0xFF008000;
	String m_strClickEvent = "��ʿ��-IDUϵͳ�趨UPS.xml";
	String m_strUrl = "www.baidu.com";
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";
	String m_strHorizontalContentAlignment = "Center";
	String m_strVerticalContentAlignment = "Center";
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	String cmd_value = "";

	Paint m_oPaint = null;
	Rect m_rBBox = null;
	public static ProgressDialog dialog;

	// ��¼�������꣬���˻���������������������������⡣
	public float m_xscal = 0;
	public float m_yscal = 0;

	private int MaskCount = -1;

	Intent m_oHomeIntent = null;

	String isDelete = "ʧ��";
	
	private   ButtonM  button;
	

}
