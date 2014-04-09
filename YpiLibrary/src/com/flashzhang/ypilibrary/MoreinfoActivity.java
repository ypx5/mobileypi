package com.flashzhang.ypilibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MoreinfoActivity extends Activity
{
	String myid;
	private String NameSpace = "http://tempuri.org/";
	private String MethodName = "getFeeinfo";
	private String url = GlobalSetting.url;
	private String soapAction = NameSpace + MethodName;
	private ListView mListView;
	private long exitTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_moreinfo);
		mListView =(ListView)findViewById(R.id.feelistview);
		String[] mFrom={"xn","paid","debt"};
		int[] mTo={R.id.xn,R.id.paidfee,R.id.debt};
		List<Map<String,String>> mList = new ArrayList<Map<String,String>>();
		SimpleAdapter mAdapter = new SimpleAdapter(this,mList,R.layout.feeitem,mFrom,mTo);  
		Intent intent=getIntent();
		myid=intent.getStringExtra("id");//获取传过来的id
		
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
			//System.out.println(result.toString());
			SoapObject feeinfo=(SoapObject)result.getProperty(0);
			int count =feeinfo.getPropertyCount();
			
			
				
				for(int i=0;i<count;i++)
				{
					SoapObject currentitem=(SoapObject)feeinfo.getProperty(i);
					Map<String,String> mMap = new HashMap<String,String>(); 
					mMap.put("xn", currentitem.getProperty("Xn").toString());
					mMap.put("paid", "已缴"+currentitem.getProperty("Paid").toString()+"元");
					mMap.put("debt", "欠费"+currentitem.getProperty("Debt").toString()+"元");
					
					
				    mList.add(mMap);
				}
			
			//SimpleAdapter mAdapter = new SimpleAdapter(MarksinfoActivity.this,mList,R.layout.marksitem,mFrom,mTo);  
			mListView.setAdapter(mAdapter);  
		}
		catch(Exception e)
    	{
    		
    		Toast toast=Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
 			 toast.show();
    		e.printStackTrace();
    	}
	}
    
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){ 
	if((System.currentTimeMillis()-exitTime) > 2000){ 
	Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show(); 
	exitTime = System.currentTimeMillis(); 
	} else { 
	finish(); 
	System.exit(0); 
	
	} 
	return true; 
	} 
	return super.onKeyDown(keyCode, event); 
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.moreinfo, menu);
		return true;
	}

}
