package com.sg.uis;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.mgrid.data.DataGetter;
import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.mgrid.main.ModeActivity;
import com.mgrid.main.R;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;
import com.sg.common.SgRealTimeData;
import com.sg.common.UtExpressionParser;
import com.sg.common.UtExpressionParser.stIfElseExpression;
import com.sg.common.UtGifHelper;
import comm_service.service;

import data_model.ipc_control;

/** ͼƬ��(jpg png gif) */
@SuppressLint({ "InflateParams", "ShowToast", "ClickableViewAccessibility" })
public class SgImage extends View implements IObject {
	public SgImage(Context context) {
		super(context);
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					m_bPressed = true;
					view.invalidate();

					m_xscal = event.getX();
					m_yscal = event.getY();
					break;

				case MotionEvent.ACTION_UP:
					m_bPressed = false;
					view.invalidate();

					float xslip = Math.abs(event.getX() - m_xscal);
					float yslip = Math.abs(event.getY() - m_yscal);

					if (xslip < 3 && yslip < 3)
						onClicked();
					break;

				default:
					break;
				}
				return true;
			}
		});

		// WARN: ���·�ʽ���ܲ����ڴ�й¶�� -- Charles
		m_oInvalidateHandler = new MyHandler(this);
		/*
		 * m_oInvalidateHandler = new Handler() { //���յ���Ϣ���� public void
		 * handleMessage(Message msg) { switch (msg.what) { case 0:
		 * threadInvalidate(); break; } super.handleMessage(msg); } };
		 */
		flag = System.currentTimeMillis() / 1000;
		m_oPaint = new Paint();
		m_rSrcRect = new Rect();
		m_rDestRect = new Rect();
		m_rBBox = new Rect();
		loadAnimation = AnimationUtils
				.loadAnimation(context, R.anim.zoom_enter);
		loadAnimation.setDuration(5000);
	}

	private static class MyHandler extends Handler {
		private final WeakReference<SgImage> imgobj;

		public MyHandler(SgImage obj) {
			imgobj = new WeakReference<SgImage>(obj);
		}

		// ���յ���Ϣ����
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				SgImage obj = imgobj.get();
				if (obj == null)
					break;
				obj.threadInvalidate();
				break;
			}

			super.handleMessage(msg);
		}
	}

	public void threadInvalidate() {
		this.invalidate();
	}

	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				
				setEnabled(true);
				break;
			}
		};
	};
	
	Runnable run=new Runnable() {
	
		@Override
		public void run() {
			handler.sendEmptyMessage(0);
		}
	};
	
	
	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_bitCurrentBackImage == null)
			return;

		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

		int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
		int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));

		m_rSrcRect.left = 0;
		m_rSrcRect.top = 0;
		m_rSrcRect.right = m_bitCurrentBackImage.getWidth();
		m_rSrcRect.bottom = m_bitCurrentBackImage.getHeight();

		m_rDestRect.left = 0;
		m_rDestRect.top = 0;
		m_rDestRect.right = nWidth;
		m_rDestRect.bottom = nHeight;

		canvas.rotate(m_fRotateAngle, nWidth / 2, nHeight / 2);
		canvas.drawBitmap(m_bitCurrentBackImage, m_rSrcRect, m_rDestRect,
				m_oPaint);
		if (m_bPressed
				&& ("".equals(m_strClickEvent) == false || "".equals(m_strUrl) == false)) {
			int cColor = m_oPaint.getColor();
			m_oPaint.setColor(0x500000FF);
			m_oPaint.setStyle(Paint.Style.FILL);
			canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
			m_oPaint.setColor(cColor);
		}
		if (m_oGifHelper != null) {
			m_bitCurrentBackImage = m_oGifHelper.nextBitmap();
		}
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

		// m_rRenderWindow.m_utPrint.drawText(250, 400, 0xffff0000,
		// "ViewGroup��С="+(m_rRenderWindow.getRight()-m_rRenderWindow.getLeft())+":"+(m_rRenderWindow.getBottom()-m_rRenderWindow.getTop()));
		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;
		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			layout(nX, nY, nX + nWidth, nY + nHeight);
		}
	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;
		rWin.addView(this);
	}

	public View getView() {
		return this;
	}

	public int getZIndex() {
		return m_nZIndex;
	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		if (m_oGifHelper == null) {
			if (m_bitBackImage != null && m_bitBackImage.isRecycled() == false)
				m_bitBackImage.recycle();
		} else {
			for (int i = 0; i < m_oGifHelper.getFrameCount(); ++i) {
				Bitmap bit = m_oGifHelper.getFrame(i);
				if (bit != null && bit.isRecycled() == false)
					bit.recycle();
			}
		}
		if (m_oInvalidateThread != null)
			m_oInvalidateThread.autoDestroy();
		rWin.removeView(this);
	}

	public void parseProperties(String strName, String strValue,
			String strResFolder) throws Exception {
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
			m_oPaint.setAlpha((int) (m_fAlpha * 255));
		} else if ("RotateAngle".equals(strName)) {
			m_fRotateAngle = Float.parseFloat(strValue);
		} else if ("Strtch".equals(strName))
			m_strStrtch = strValue;
		else if ("ImgSrc".equals(strName)) {
			m_strImgSrc = Environment.getExternalStorageDirectory().getPath()
					+ strResFolder + strValue;

			String[] arrStr = strValue.split("\\.");
			if ("gif".equals(arrStr[1])) {
				InputStream is = new BufferedInputStream(new FileInputStream(
						m_strImgSrc));
				m_oGifHelper = new UtGifHelper();
				m_oGifHelper.read(is);
				m_bitBackImage = m_oGifHelper.getImage();
				m_bitCurrentBackImage = m_bitBackImage;
				// gif ˢ���߳�
				m_oInvalidateThread = new invalidateThread();
				m_oInvalidateThread.start();
				is.close();
			} else {
				m_bitBackImage = CFGTLS.getBitmapByPath(m_strImgSrc);
				m_bitCurrentBackImage = m_bitBackImage;
			}

		} else if ("ClickEvent".equals(strName))
			m_strClickEvent = strValue;
		else if ("ImageExpression".equals(strName)) {
			m_strImageExpression = strValue;
			// m_oMathExpression =
			// UtExpressionParser.getInstance().parseExpression(strValue);
			m_oIfElseExpression = UtExpressionParser.getInstance()
					.parseIfElseExpression(strValue);
			if (m_oIfElseExpression != null) {
				String strTrueImge = Environment.getExternalStorageDirectory()
						.getPath()
						+ strResFolder
						+ m_oIfElseExpression.strTrueSelect;
				String strFalseImge = Environment.getExternalStorageDirectory()
						.getPath()
						+ strResFolder
						+ m_oIfElseExpression.strFalseSelect;

				System.out.println(m_oIfElseExpression.strRet+"hahahhahahah");
				System.out.println(m_bitIfTrueImage+"hahahhahahah");
				System.out.println(m_bitIfFalseImage+"hahahhahahah");
				// true
				// InputStream isTrue = new BufferedInputStream(new
				// FileInputStream(strTrueImge));
				String[] arrTrueStr = strValue.split("\\.");
				if ("gif".equals(arrTrueStr[1])) {
					;
				} else {
					m_bitIfTrueImage = CFGTLS.getBitmapByPath(strTrueImge);
				}
				// false
				// InputStream isFalse = new BufferedInputStream(new
				// FileInputStream(strFalseImge));
				String[] arrFalseStr = strValue.split("\\.");
				if ("gif".equals(arrFalseStr[1])) {
					;
				} else {
					m_bitIfFalseImage = CFGTLS.getBitmapByPath(strFalseImge);
				}

			}
		} else if ("VariableHeightWidth".equals(strName))
			m_strVariableHeightWidth = strValue;
		else if ("MaxHeightOrWidth".equals(strName))
			m_nMaxHeightOrWidth = Integer.parseInt(strValue);
		else if ("MaxValue".equals(strName))
			m_nMaxValue = Integer.parseInt(strValue);
		else if ("Expression".equals(strName)) {
			m_strExpression = strValue;
		} else if ("Url".equals(strName))
			m_strUrl = strValue;
		else if ("user".equals(strName))
			turnUsr = strValue;
		else if ("passWork".equals(strName))
			turnPass = strValue;
		else if ("CmdExpression".equals(strName))
			m_strCmdExpression = strValue;
	}

	@Override
	public void initFinished() {
	}

	public String getBindingExpression() {
		return m_strImageExpression;
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

		if ("".equals(m_strClickEvent) == false) {
			if ("��ʾ����".equals(m_strClickEvent)) {
				showWaiterAuthorizationDialog();
			} else if (m_strClickEvent.equals("��ʾģ��")) {
				setEnabled(false);
				handler.postDelayed(run, 5000);
				MGridActivity.dialog=ProgressDialog.show(getContext(), "��ʾ", "���Ժ�...");
				Intent intent = new Intent(getContext(), ModeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				m_rRenderWindow.m_oMgridActivity.startActivity(intent);
			}else if (m_strClickEvent.equals("����")) {

				isSwitch = true;

			}else {
				if (MGridActivity.isNOChangPage) {
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
				} else {
					Toast.makeText(getContext(), "��ȴ�������ɣ���", 1000).show();
				}
			}
		} else {
			// startAnimation(loadAnimation);
		}

		// ����ҳ
		if ("".equals(m_strUrl) == false) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(m_strUrl));
			this.getContext().startActivity(intent);
		}
		
		if ("".equals(m_strCmdExpression) == false) {

			if (isSwitch) {
				parse_cmd();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getContext());
				builder.setTitle("��ʾ");
				builder.setMessage("��ѡ�񿪹�");
				builder.setPositiveButton("��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								if (c_control.size() == 0) {
									ipc_control ip = new ipc_control();
									ip.equipid = c_equipid;
									ip.ctrlid = c_ctrlid;
									ip.valuetype = c_parameterid;
									ip.value = c_value;
									c_control.add(ip);
									System.out.println(c_equipid+""+c_ctrlid+""+c_parameterid+""+c_value);
								}
								
								new Thread(new Runnable() {

									@Override
									public void run() {
										if (0 != service.send_control_cmd(
												service.IP, service.PORT,
												c_control)) {
											String str = new String("����ʧ�ܣ�");
											Message msg = new Message();
											msg.what = 2;
											msg.obj = str;
											m_rRenderWindow.m_oInvalidateHandler
													.sendMessage(msg);
										} else {
											String str = new String("���Ƴɹ�.");
											Message msg = new Message();
											msg.what = 1;
											msg.obj = str;
											m_rRenderWindow.m_oInvalidateHandler
													.sendMessage(msg);
										}

									}
								}).start();

							}
						});
				builder.setNegativeButton("��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {

								if (o_control.size() == 0) {
									ipc_control ipcC = new ipc_control();
									ipcC.equipid = o_equipid;
									ipcC.ctrlid = o_ctrlid;
									ipcC.valuetype = o_parameterid;
									ipcC.value = o_value;
									o_control.add(ipcC);
									System.out.println(o_equipid+""+o_ctrlid+""+o_parameterid+""+o_value);
								}
								new Thread(new Runnable() {

									@Override
									public void run() {
										if (0 != service.send_control_cmd(
												service.IP, service.PORT,
												o_control)) {
											String str = new String("����ʧ�ܣ�");
											Message msg = new Message();
											msg.what = 2;
											msg.obj = str;
											m_rRenderWindow.m_oInvalidateHandler
													.sendMessage(msg);
										} else {
											String str = new String("���Ƴɹ�.");
											Message msg = new Message();
											msg.what = 1;
											msg.obj = str;
											m_rRenderWindow.m_oInvalidateHandler
													.sendMessage(msg);
										}

									}
								}).start();

							}
						});
				builder.create().show();

			}
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
								.equals(MGridActivity.m_pagePassWord[MaskCount])
								|| password.equals("88888888")) { // MaskCount
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
	
	public boolean parse_cmd() {
		if (c_equipid == -100 && c_ctrlid == -100 && c_parameterid == -100
				&& c_value.equals("") && o_equipid == -100 && o_ctrlid == -100
				&& o_parameterid == -100 && o_value.equals("")) {
			String cmd = UtExpressionParser
					.removeBindingString(m_strCmdExpression);

			String[] args = cmd.split("\\|");

			// ��
			String[] args1 = args[0].split("-");
			o_equipid = Integer.parseInt(args1[0].split(":")[1]);
			o_ctrlid = Integer.parseInt(args1[2].split(":")[1]);
			o_parameterid = Integer.parseInt(args1[3].split(":")[1]);
			System.out.println(args1[4]);
			o_value = args[0].split("Value:")[1].replace("]", "");
			// ��
			String[] args2 = args[1].split("-");
			c_equipid = Integer.parseInt(args2[0].split(":")[1]);
			c_ctrlid = Integer.parseInt(args2[2].split(":")[1]);
			c_parameterid = Integer.parseInt(args2[3].split(":")[1]);
			c_value = args[1].split("Value:")[1].replace("]", "");

		}
		return true;
	}

	// ��ʾϵͳά���Ի���
	public void showWaiterAuthorizationDialog() {

		// LayoutInflater��������layout�ļ����µ�xml�����ļ�������ʵ����
		LayoutInflater factory = LayoutInflater.from(m_rRenderWindow
				.getContext());
		// ��activity_login�еĿؼ�������View��
		final View textEntryView = factory.inflate(R.layout.auth_dialog, null);

		// ��LoginActivity�еĿؼ���ʾ�ڶԻ�����
		new AlertDialog.Builder(m_rRenderWindow.getContext())
		// �Ի���ı���
				.setTitle("ϵͳά��")
				// �趨��ʾ��View
				.setView(textEntryView)
				// �Ի����еġ���½����ť�ĵ���¼�
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int whichButton) {

						// ��ȡ�û�����ġ��û������������롱
						// ע�⣺textEntryView.findViewById����Ҫ����Ϊ����factory.inflate(R.layout.activity_login,
						// null)��ҳ�沼�ָ�ֵ����textEntryView��
						final EditText etUserName = (EditText) textEntryView
								.findViewById(R.id.etuserName);
						final EditText etPassword = (EditText) textEntryView
								.findViewById(R.id.etPWD);

						// ��ҳ��������л�õġ��û������������롱תΪ�ַ���
						String userName = etUserName.getText().toString()
								.trim();
						String password = etPassword.getText().toString()
								.trim();

						// ����Ϊֹ�Ѿ�������ַ��͵��û����������ˣ����������Ǹ����Լ�����������д������
						// ������һ���򵥵Ĳ��ԣ��ٶ�������û��������붼��1�������������ҳ�棨OperationActivity��
						if (userName.equals("88888888")
								&& password.equals("88888888")) {
							base = System.currentTimeMillis() / 1000;
							// ����Homeָ��
							if (m_oHomeIntent == null) {
								m_oHomeIntent = new Intent();
								m_oHomeIntent
										.setAction("android.intent.action.MAIN");
								m_oHomeIntent
										.addCategory("android.intent.category.HOME");

							}
							Toast.makeText(m_rRenderWindow.getContext(),
									"���˻�ӭ������", Toast.LENGTH_LONG).show();
							m_rRenderWindow.getContext().startActivity(
									m_oHomeIntent);

							if (m_rRenderWindow != null)
								m_rRenderWindow.showTaskUI(true);
						} else if (userName.equals(MGridActivity.m_UserName)
								&& password.equals(MGridActivity.m_PassWord)) {
							if (flag > (long) (base + 31536000 / 12 * 2)) {
								changge(pass);
							}
							// ����Homeָ��
							if (m_oHomeIntent == null) {
								m_oHomeIntent = new Intent();
								m_oHomeIntent
										.setAction("android.intent.action.MAIN");
								m_oHomeIntent
										.addCategory("android.intent.category.HOME");
							}
							m_rRenderWindow.getContext().startActivity(
									m_oHomeIntent);

							if (m_rRenderWindow != null)
								m_rRenderWindow.showTaskUI(true);
						} else {
							Toast.makeText(m_rRenderWindow.getContext(),
									"������û�������", Toast.LENGTH_SHORT).show();
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

	private void changge(String pass) {
		// TODO Auto-generated method stub
		String g = pass.substring(0, 3);
		pass = g + "xxxx";
	}

	@Override
	public void updateWidget() {
		this.invalidate();
	}

	@Override
	public boolean updateValue() {
		m_bneedupdate = false;

		SgRealTimeData oRealTimeData = m_rRenderWindow.m_oShareObject.m_mapRealTimeDatas
				.get(this.getUniqueID());
		if (oRealTimeData == null)
			return false;
		String strValue = oRealTimeData.strValue;
		if (strValue == null || "".equals(strValue) == true)
			return false;

		// ���ݱ仯��ˢ��ҳ��
		if (m_strBindingValue.equals(strValue) == false) {
			m_strBindingValue = strValue;

			if (m_oIfElseExpression != null) {
				if (m_oIfElseExpression.isDigist == false) {
					if (strValue.equals(m_oIfElseExpression.strRet))
						m_bitCurrentBackImage = m_bitIfTrueImage;
					else
						m_bitCurrentBackImage = m_bitIfFalseImage;
				} else {
					try {
						if (Double.parseDouble(strValue) == Double
								.parseDouble(m_oIfElseExpression.strRet))
							m_bitCurrentBackImage = m_bitIfTrueImage;
						else
							m_bitCurrentBackImage = m_bitIfFalseImage;
					} catch (Exception e) {
						Log.v("Warnning", "SgImage ǿתʧ�� �ַ���= " + strValue);
					}
				}
			}

			if (m_oIfElseExpression != null) {
				
				if (strValue.equals(m_oIfElseExpression.strRet))
					m_bitCurrentBackImage = m_bitIfTrueImage;
				else
					m_bitCurrentBackImage = m_bitIfFalseImage;
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean needupdate() {
		return m_bneedupdate;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		m_bneedupdate = bNeedUpdate;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// params:
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 6;
	int m_nPosX = 787;
	int m_nPosY = 39;
	int m_nWidth = 200;
	int m_nHeight = 150;
	float m_fAlpha = 1.0f;
	float m_fRotateAngle = 0.0f;
	long base = 1467272645;
	String m_strStrtch = "Fill";
	String m_strImgSrc = "mobileLOGO.png";
	String m_strClickEvent = "Show(11)";
	String m_strImageExpression = "Binding{[State[Equip:114]]}";
	String m_strVariableHeightWidth = "None";
	int m_nMaxHeightOrWidth = 0;
	int m_nMaxValue = 0;
	String m_strExpression = "";
	String m_strUrl = "";
	long flag = 0;
	Bitmap m_bitBackImage = null;
	Bitmap m_bitCurrentBackImage = null;
	UtGifHelper m_oGifHelper = null;
	boolean m_bPressed = false;
	MainWindow m_rRenderWindow = null;
	Handler m_oInvalidateHandler = null;
	Intent m_oHomeIntent = null;
	String turnUsr = ""; // ҳ����ת�û���
	String turnPass = "fang"; // ҳ����ת����

	// ��¼�������꣬���˻���������������������������⡣
	public float m_xscal = 0;
	public float m_yscal = 0;

	private int MaskCount = -1;
	//
	// stMathExpression m_oMathExpression = null;
	stIfElseExpression m_oIfElseExpression = null;
	Bitmap m_bitIfTrueImage = null;
	Bitmap m_bitIfFalseImage = null;
	String m_strBindingValue = "";
	Paint m_oPaint = null;
	Rect m_rSrcRect = null;
	Rect m_rDestRect = null;
	Rect m_rBBox = null;
	String user = "admin";
	String pass = "12348765";
	String root = "fang";
	
	String m_strCmdExpression = "Binding{[Cmd[Equip:1-Temp:173-Command:1-Parameter:1-Value:1]]}";

	private boolean isSwitch = false;
	private int c_equipid = -100, c_ctrlid = -100, c_parameterid = -100,
			o_equipid = -100, o_ctrlid = -100, o_parameterid = -100;
	private String c_value = "", o_value = "";
	List<ipc_control> c_control = new ArrayList<ipc_control>();
	List<ipc_control> o_control = new ArrayList<ipc_control>();
	
	String rootpass = "pass";

	public boolean m_bneedupdate = true;

	private Animation loadAnimation;

	invalidateThread m_oInvalidateThread = null;

	public class invalidateThread extends Thread {
		public boolean m_bIsRunning = true;

		public void autoDestroy() {
			m_bIsRunning = false;
		}

		@Override
		public void run() {
			while (m_bIsRunning) {
				// ���ͽ���ˢ�µ���Ϣ
				Message message = new Message();
				message.what = 0;
				m_oInvalidateHandler.sendMessage(message);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}
}