package data_model;

//made author:fjw0312
//date:2016
//notice:
public class rc_value {
	public String equipid;    //�豸id
	public String equip_name; //�豸��
	public String sigid;      //�ź�id
	public String name;        //�ź���
	public String value;       //��ֵ
	public String unit;        //��λ
	//public String value_type;   //�ź�����
	//public String is_invalid;   //0 --> valid, 1 --> invalid
	//public String severity;   //�����澯�ȼ�
	public String datetime;   
	


	public String to_string(){
		
		String buf = null;  
		String a1 = equipid + "`";
		String a2 = equip_name + "`";
		String a3 = sigid + "`";
		String a4 = name + "`";
		String a5 = value + "`";
		String a6 = unit + "`";
		//String a7 = value_type + "`";
		//String a8 = is_invalid + "`";
		//String a9 = severity + "`";
		String a10 = datetime;
		
		buf = a1+a2+a3+a4+a5+a6+a10;
		
		return buf;
	}

	public boolean read_string(String buf){
		
		String[] a  = new String[100];
		
		a = buf.split("`");

		if(a.length != 7){
			return false;
		}
		
		equipid = a[0];    //�豸id
		equip_name = a[1]; //�豸��
		sigid = a[2];      //�ź�id
		name = a[3];        //�ź���
		value = a[4];       //�ź�ֵ
		unit = a[5];        //��λ

		datetime = a[6];   

		
		
		return true;
	}
	
	
}
