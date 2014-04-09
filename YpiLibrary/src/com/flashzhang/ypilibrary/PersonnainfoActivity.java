package com.flashzhang.ypilibrary;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class PersonnainfoActivity extends Activity
{
    private TextView name;
    private TextView id;
    private TextView classname;
    
    private long exitTime = 0;
    String myid;
    private String NameSpace = "http://tempuri.org/";
    private String MethodName = "getStudentinfo";
    private String url = GlobalSetting.url;
    private String soapAction = NameSpace + MethodName;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personnainfo);
		
		//android4.0 ��Ĭ�ϲ����������߳̽���http���������������д���ǿ�ƺ��ԣ������Ժ��������߳�ʵ��
		if (android.os.Build.VERSION.SDK_INT > 9) {
		    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		    StrictMode.setThreadPolicy(policy);
		}
		
		name=(TextView)findViewById(R.id.textView2);
		id=(TextView)findViewById(R.id.textView4);
		classname=(TextView)findViewById(R.id.textView6);
		
		Intent intent=getIntent();
		myid=intent.getStringExtra("id");//��ȡ��������id
		
		try
		{
			SoapObject rpc = new SoapObject(NameSpace, MethodName);
			rpc.addProperty("id",myid);
			AndroidHttpTransport ht = new AndroidHttpTransport(url);
			ht.debug = true;
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.bodyOut = rpc;
			envelope.dotNet = true; 
			envelope.setOutputSoapObject(rpc);
			ht.call(soapAction, envelope); 
			SoapObject result =(SoapObject)envelope.bodyIn;
			SoapObject userinfo=(SoapObject)result.getProperty(0);
			//System.out.println(result.toString());
			if(result !=null)
			{
				//System.out.println(userinfo.getProperty("Userid"));
				id.setText(userinfo.getProperty("Userid").toString());
				name.setText(userinfo.getProperty("Name").toString());
				classname.setText(userinfo.getProperty("Classname").toString());
			}
			
		}
		catch(Exception e)
    	{
    		
    		Toast toast=Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
 			 toast.show();
    		e.printStackTrace();
    	}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personnainfo, menu);
		return true;
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){ 
	if((System.currentTimeMillis()-exitTime) > 2000){ 
	Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show(); 
	exitTime = System.currentTimeMillis(); 
	} else { 
	finish(); 
	System.exit(0); 
	
	} 
	return true; 
	} 
	return super.onKeyDown(keyCode, event); 
	} 

}
