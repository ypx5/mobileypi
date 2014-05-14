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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class BorrowinfoActivity extends Activity
{
	private String NameSpace = "http://tempuri.org/";
    private String MethodName = "getBorrowinfo";
    private String url = GlobalSetting.url;
    private String soapAction = NameSpace + MethodName;
    String myid;
    private ProgressBar mWaitingforbooks;
    ListView mListView ;
    List<Map<String,String>> mList;
    SimpleAdapter mAdapter;
    private long exitTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrowinfo);
		mWaitingforbooks=(ProgressBar) findViewById(R.id.waitingforbooks);
		mListView = (ListView)findViewById(R.id.BorrowlistView);  
		//下面是数据映射关系,mFrom和mTo按顺序一一对应  
		String[] mFrom = new String[]{"bookname","borrowdate","duedate"};  
		int[] mTo = new int[]{R.id.bookname,R.id.borrowdate,R.id.duedate};  
		mList = new ArrayList<Map<String,String>>();  
		
		Intent intent=getIntent();
		myid=intent.getStringExtra("id");//获取传过来的id
	    mWaitingforbooks.setVisibility(mWaitingforbooks.VISIBLE);
	    mList.clear();
		mAdapter = new SimpleAdapter(this,mList,R.layout.item,mFrom,mTo);  
		mListView.setAdapter(mAdapter);  
		Utility.setListViewHeightBasedOnChildren(mListView);
		mWaitingforbooks.setVisibility(mWaitingforbooks.VISIBLE);
		Thread gettingBooksThread=new Thread(thread);
		gettingBooksThread.start();
	
	
	
	}
	
    private Thread thread=new Thread(){
    	
    	public void run()
    	{
    		Looper.prepare();
    		callGettingBookMethod();
    		Message message=new Message();
    		message.what=0;
	    	myHandler.sendMessage(message); 
	    	Looper.loop();
    	}
    	
    };
    
    private Handler myHandler = new Handler() { 
        public void handleMessage(Message msg)
        { 
             switch (msg.what) 
             { 
                  case 0: mWaitingforbooks.setVisibility(mWaitingforbooks.INVISIBLE);
                          mListView.setAdapter(mAdapter);
      	    	          break;
                  
                       
             } 
             super.handleMessage(msg); 
        } 
   }; 
    
    private void callGettingBookMethod()
    {
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
			SoapObject borrowinfo=(SoapObject)result.getProperty(0);
		    int count =borrowinfo.getPropertyCount();
			//System.out.println(userinfo.toString());
			if(result !=null)
			{
				//System.out.println(userinfo.getProperty("Userid"));
				//id.setText(userinfo.getProperty("Userid").toString());
				//name.setText(userinfo.getProperty("Name").toString());
				//classname.setText(userinfo.getProperty("Classname").toString());
				
				for(int i=0;i<count;i++)
				{
					SoapObject currentitem=(SoapObject)borrowinfo.getProperty(i);
					Map<String,String> mMap = new HashMap<String,String>(); 
					mMap.put("bookname", currentitem.getProperty("Bookname").toString());
					mMap.put("borrowdate", currentitem.getProperty("Borrowdate").toString());
					mMap.put("duedate", currentitem.getProperty("Duedate").toString());
					
				    mList.add(mMap);
				}
				
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
		getMenuInflater().inflate(R.menu.borrowinfo, menu);
		return true;
	}

}

