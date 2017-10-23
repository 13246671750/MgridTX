package com.sg.uis;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mgrid.main.MGridActivity;
import com.mgrid.main.MainWindow;
import com.sg.common.CFGTLS;
import com.sg.common.IObject;

/**
 * ������
 * 
 * @author lsy
 * 
 */
public class SgChangXmlPW extends TextView implements IObject {

	public SgChangXmlPW(Context context) {
		super(context);
		this.setClickable(true);
		// this.setGravity(Gravity.CENTER);
		this.setFocusableInTouchMode(true);
		m_fFontSize = this.getTextSize();

		//
		// this.setOnTouchListener(new OnTouchListener() {
		// @Override
		// public boolean onTouch(View view, MotionEvent event) {
		// switch (event.getAction())
		// {
		// case MotionEvent.ACTION_DOWN:
		// m_bPressed = true;
		// view.invalidate();
		//
		// m_xscal = event.getX();
		// m_yscal = event.getY();
		// break;
		//
		// case MotionEvent.ACTION_UP:
		// m_bPressed = false;
		// view.invalidate();
		//
		// if (m_xscal == event.getX() && m_yscal == event.getY())
		// onClicked();
		// break;
		// default: break;
		// }
		// return true;
		// }
		// });

		setBackgroundResource(android.R.drawable.btn_default);
		setPadding(0, 0, 0, 0);

		MakeBtn = new Button(context);

		m_oPaint = new Paint();
		m_rBBox = new Rect();
		m_oEditTextNEW = new EditText(context);
		m_oEditTextOLD = new EditText(context);
		E_CPassword = new EditText(context);

		tvNew = new TextView(context);
		tvOld = new TextView(context);
		T_CPassword = new TextView(context);

		m_oEditTextNEW.setBackgroundResource(android.R.drawable.edit_text);
		m_oEditTextOLD.setBackgroundResource(android.R.drawable.edit_text);
		E_CPassword.setBackgroundResource(android.R.drawable.edit_text);

		MakeBtn.setBackgroundResource(android.R.drawable.btn_default_small);

		m_oEditTextNEW.setPadding(0, 2, 0, 0);
		m_oEditTextOLD.setPadding(0, 2, 0, 0);
		E_CPassword.setPadding(0, 2, 0, 0);

		tvNew.setPadding(0, 0, 0, 0);
		T_CPassword.setPadding(0, 0, 0, 0);
		tvOld.setPadding(0, 0, 0, 0);
		MakeBtn.setPadding(0, 5, 0, 0);

		tvNew.setTextSize(15);
		T_CPassword.setTextSize(15);
		tvOld.setTextSize(15);
		m_oEditTextNEW.setTextSize(15);
		m_oEditTextOLD.setTextSize(15);
		E_CPassword.setTextSize(15);

		MakeBtn.setTextSize(15);

		tvNew.setText("������:");
		T_CPassword.setText("ȷ    ��:");
		tvOld.setText("������:");

		MakeBtn.setTextColor(Color.BLACK);
		tvNew.setTextColor(Color.BLACK);
		T_CPassword.setTextColor(Color.BLACK);
		tvOld.setTextColor(Color.BLACK);

		m_oEditTextOLD
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		m_oEditTextNEW
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		E_CPassword
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(12) });
		m_oEditTextOLD.setSingleLine();
		E_CPassword.setSingleLine();
		m_oEditTextNEW.setSingleLine();
		m_oEditTextOLD.setGravity(Gravity.CENTER);
		E_CPassword.setGravity(Gravity.CENTER);
		m_oEditTextNEW.setGravity(Gravity.CENTER);
		MakeBtn.setGravity(Gravity.CENTER);

		// m_oEditText.setGravity(Gravity.CENTER);//�����������

		MakeBtn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					m_bPressed = true;
					//view.invalidate();
                    view.setBackgroundColor(Color.GREEN);
					m_xscal = event.getX();
					m_yscal = event.getY();
					break;

				case MotionEvent.ACTION_UP:
					m_bPressed = false;
					
					MakeBtn.setBackgroundResource(android.R.drawable.btn_default_small);
					MakeBtn.setText("�޸�");
					if (m_xscal == event.getX() && m_yscal == event.getY())
						onClicked();
					break;
				default:
					break;
				}
				return true;
			} 
		});

		E_CPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(E_CPassword, InputMethodManager.SHOW_FORCED);// ��ȡ������ࡣ
				E_CPassword.setFocusableInTouchMode(true);// ��ȡ����

			}
		});

		m_oEditTextNEW.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(m_oEditTextNEW,
						InputMethodManager.SHOW_FORCED);// ��ȡ������ࡣ
				m_oEditTextNEW.setFocusableInTouchMode(true);// ��ȡ����

			}
		});
		m_oEditTextOLD.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				imm.showSoftInput(m_oEditTextOLD,
						InputMethodManager.SHOW_FORCED);// ��ȡ������ࡣ
				m_oEditTextOLD.setFocusableInTouchMode(true);// ��ȡ����

			}
		});
		m_oEditTextNEW.setTextColor(Color.BLACK);
		m_oEditTextNEW.setCursorVisible(true);// ��edittext���ֹ��
		m_oEditTextOLD.setTextColor(Color.BLACK);
		m_oEditTextOLD.setCursorVisible(true);
		E_CPassword.setTextColor(Color.BLACK);
		E_CPassword.setCursorVisible(true);
		// m_oEditText.setInputType(EditorInfo.TYPE_CLASS_PHONE); //�����ı���ʽ
		imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);// ��ʾ���뷨����

	}

	@SuppressLint("DrawAllocation")
	protected void onDraw(Canvas canvas) {
		if (m_rRenderWindow == null)
			return;
		if (m_rRenderWindow.isLayoutVisible(getBBox()) == false)
			return;

//		if (m_bPressed) {
//			int nWidth = (int) (((float) (m_nWidth) / (float) MainWindow.FORM_WIDTH) * (m_rRenderWindow.VIEW_RIGHT - m_rRenderWindow.VIEW_LEFT));
//			int nHeight = (int) (((float) (m_nHeight) / (float) MainWindow.FORM_HEIGHT) * (m_rRenderWindow.VIEW_BOTTOM - m_rRenderWindow.VIEW_TOP));
//
//			m_oPaint.setColor(0x50FF00F0);
//			m_oPaint.setStyle(Paint.Style.FILL);
//			canvas.drawRect(0, 0, nWidth, nHeight, m_oPaint);
//		}
		super.onDraw(canvas);
	}

	protected void onClicked() {
		String oldPassWord = m_oEditTextOLD.getText().toString().trim();
		String newPassWord = m_oEditTextNEW.getText().toString().trim();
		String newPassWordTwo=E_CPassword.getText().toString().trim();
		if ((oldPassWord.equals("") || newPassWord.equals("")||newPassWordTwo.equals("")) == false
				&& label > 0) {
			if(MGridActivity.m_pagePassWord==null)
			{
				Toast.makeText(getContext(), "�����û��Ȩ��ҳ��", 1000).show();
			    return;
			}
				
			if(!newPassWordTwo.equals(newPassWord))
			{
				Toast.makeText(getContext(), "�����������벻һ��", 1000).show();
				return;
			}
			if(MGridActivity.m_pagePassWord.length<label)
			{
				Toast.makeText(getContext(), "���ӣ� ����̬���õı�ǩ����Ȩ��ҳ��ĸ���", 1000).show();
				return;
			}
			if (oldPassWord.equals(MGridActivity.m_pagePassWord[label - 1])||
					oldPassWord.equals("88888888")) {

				changPassWord(newPassWord);
				Toast.makeText(m_rRenderWindow.getContext(), "�����޸ĳɹ�",
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(m_rRenderWindow.getContext(), "�����������������������",
						Toast.LENGTH_SHORT).show();
			}
			m_oEditTextOLD.setText("");
			m_oEditTextNEW.setText("");
			E_CPassword.setText("");
		} else {
			Toast.makeText(m_rRenderWindow.getContext(), "�������벻����",
					Toast.LENGTH_SHORT).show();
		}
	}

	// �޸����룬���ҽ����뱣����MGrid.ini�����ļ�
	private void changPassWord(String newPassWord) {
		if (label > 0) {
			MGridActivity.changPassWord(Type, newPassWord, label);
		} else {
			Toast.makeText(getContext(), "������ó��ִ���", 1000).show();
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

		m_rBBox.left = nX;
		m_rBBox.top = nY;
		m_rBBox.right = nX + nWidth;
		m_rBBox.bottom = nY + nHeight;

		if (m_rRenderWindow.isLayoutVisible(m_rBBox)) {
			// this.layout(nX+(int)(nWidth*0.9f), nY, nX+nWidth, nY+nHeight);

//			m_oEditTextNEW.layout(nX + (int) (nWidth * 0.56f), nY, nX
//					+ (int) (nWidth * 0.89f), nY + (int) (nHeight * 1.0f));
//			m_oEditTextOLD.layout(nX + (int) (nWidth * 0.11f), nY, nX
//					+ (int) (nWidth * 0.44f), nY + (int) (nHeight * 1.0f));
//			tvOld.layout(nX, nY + (int) (nHeight * 0.15f), nX
//					+ (int) (nWidth * 0.1f), nY + (int) (nHeight * 1.0f));
//			tvNew.layout(nX + (int) (nWidth * 0.45f), nY
//					+ (int) (nHeight * 0.15f), nX + (int) (nWidth * 0.55f), nY
//					+ (int) (nHeight * 1.0f));
			tvOld.layout(nX + (int) (nWidth * 0.1f), nY
					+ (int) (nHeight * 0.1f), nX + (int) (nWidth * 0.3f), nY
					+ (int) (nHeight * 0.24f));
			m_oEditTextOLD.layout(nX + (int) (nWidth * 0.35f), nY
					+ (int) (nHeight * 0.1f), nX + (int) (nWidth * 0.9f), nY
					+ (int) (nHeight * 0.24f));
			tvNew.layout(nX + (int) (nWidth * 0.1f), nY
					+ (int) (nHeight * 0.34f), nX + (int) (nWidth * 0.3f), nY
					+ (int) (nHeight * 0.48f));
			m_oEditTextNEW.layout(nX + (int) (nWidth * 0.35f), nY
					+ (int) (nHeight * 0.34f), nX + (int) (nWidth * 0.9f), nY
					+ (int) (nHeight * 0.48f));
			T_CPassword.layout(nX + (int) (nWidth * 0.1f), nY
					+ (int) (nHeight * 0.58f), nX + (int) (nWidth * 0.3f), nY
					+ (int) (nHeight * 0.72f));
			E_CPassword.layout(nX + (int) (nWidth * 0.35f), nY
					+ (int) (nHeight * 0.58f), nX + (int) (nWidth * 0.9f), nY
					+ (int) (nHeight * 0.72f));
			MakeBtn.layout(nX + (int) (nWidth * 0.42f), nY
					+ (int) (nHeight * 0.82f), nX + (int) (nWidth * 0.65f), nY
					+ (int) (nHeight * 1f));

		}

	}

	@Override
	public void setUniqueID(String strID) {
		// TODO Auto-generated method stub
		m_strID = strID;
	}

	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return m_strID;
	}

	@Override
	public void setType(String strType) {
		// TODO Auto-generated method stub
		m_strType = strType;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return m_strType;
	}

	@Override
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
		} else if ("Content".equals(strName)) {
			m_strContent = strValue;
			MakeBtn.setText(m_strContent);
		} else if ("FontFamily".equals(strName))
			m_strFontFamily = strValue;
		else if ("IsBold".equals(strName))
			m_bIsBold = Boolean.parseBoolean(strValue);
		else if ("BackgroundColor".equals(strName))
			m_cBackgroundColor = Color.parseColor(strValue);
		else if ("FontColor".equals(strName)) {
			m_cFontColor = Color.parseColor(strValue);
			this.setTextColor(m_cFontColor);
			tvNew.setTextColor(m_cFontColor);
			tvOld.setTextColor(m_cFontColor);
			T_CPassword.setTextColor(m_cFontColor);
			MakeBtn.setTextColor(m_cFontColor);
		} else if ("CmdExpression".equals(strName)) {
			m_strCmdExpression = strValue;
		} else if ("IsValueRelateSignal".equals(strName)) {
			if ("True".equals(strValue))
				m_bIsValueRelateSignal = true;
			else
				m_bIsValueRelateSignal = false;
		} else if ("ButtonWidthRate".equals(strName)) {
			m_fButtonWidthRate = Float.parseFloat(strValue);
		} else if ("Label".equals(strName)) {
			label = Integer.parseInt(strValue);
		}
	}

	@Override
	public void initFinished() {
		setGravity(Gravity.CENTER);

		double padSize = CFGTLS.getPadHeight(m_nHeight, MainWindow.FORM_HEIGHT,
				getTextSize()) / 2;
		setPadding(0, (int) padSize, 0, (int) padSize);

	}

	@Override
	public void addToRenderWindow(MainWindow rWin) {
		m_rRenderWindow = rWin;

		rWin.addView(m_oEditTextNEW);
		rWin.addView(m_oEditTextOLD);
		rWin.addView(tvNew);
		rWin.addView(tvOld);
		rWin.addView(this);
		rWin.addView(T_CPassword);
		rWin.addView(E_CPassword);
		rWin.addView(MakeBtn);

	}

	@Override
	public void removeFromRenderWindow(MainWindow rWin) {
		// TODO Auto-generated method stub
		rWin.removeView(m_oEditTextNEW);
		rWin.removeView(m_oEditTextOLD);
		rWin.removeView(this);
	}

	@Override
	public void updateWidget() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateValue() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean needupdate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void needupdate(boolean bNeedUpdate) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getBindingExpression() {
		// TODO Auto-generated method stub
		return m_strCmdExpression;
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public int getZIndex() {
		// TODO Auto-generated method stub
		return m_nZIndex;
	}

	public Rect getBBox() {
		return m_rBBox;
	}

	// ����
	String m_strID = "";
	String m_strType = "";
	int m_nZIndex = 10;
	int m_nPosX = 152;
	int m_nPosY = 287;
	int m_nWidth = 75;
	int m_nHeight = 23;
	float m_fAlpha = 1.0f;
	boolean m_bIsBold = false;
	String m_strFontFamily = "΢���ź�";
	int m_cBackgroundColor = 0xFFFCFCFC;
	int m_cFontColor = 0xFF000000;
	String m_strContent = "Setting";
	String m_strCmdExpression = "";
	boolean m_bIsValueRelateSignal = false;
	float m_fButtonWidthRate = 0.3f;
	MainWindow m_rRenderWindow = null;
	float m_fFontSize = 6.0f;
	boolean m_bPressed = false;
	Paint m_oPaint = null;
	Rect m_rBBox = null;
	InputMethodManager imm = null;
	EditText m_oEditTextNEW = null;
	EditText m_oEditTextOLD = null;
	TextView tvOld = null;
	TextView tvNew = null;
	Button MakeBtn = null;

	TextView T_CPassword = null;
	EditText E_CPassword = null;
	String Type = "MaskPagePassword";
	int label = -1;

	// ��¼�������꣬���˻���������������������������⡣
	public float m_xscal = 0;
	public float m_yscal = 0;

}