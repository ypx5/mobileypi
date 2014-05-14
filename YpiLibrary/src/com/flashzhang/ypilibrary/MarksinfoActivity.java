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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MarksinfoActivity extends Activity
{
    
	String myid;
	private String NameSpace = "http://tempuri.org/";
	private String MethodName = "getMarksinfo";
	private String url = GlobalSetting.url;
	private String soapAction = NameSpace + MethodName;
	private Spinner spinner1;
	private TextView Text;
    private ListView mListView;
    private ProgressBar mWaitingformarks;
    private long exitTime = 0;
    private ArrayAdapter<String> adapter;
    List<Map<String,String>> mList;
    SoapObject markinfo;
    String[] mFrom;
    int[] mTo;
    SimpleAdapter mAdapter ;  
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marksinfo);
		spinner1=(Spinner)findViewById(R.id.spinner1);
		Text=(TextView)findViewById(R.id.textView2);
		mWaitingformarks=(ProgressBar) findViewById(R.id.waitingformarks);
		Text.setVisibility(View.INVISIBLE);
		mListView=(ListView)findViewById(R.id.listView_marks);
		String[] m={"12-13-2","13-14-1","13-14-2"};
		
		mWaitingformarks.setVisibility(mWaitingformarks.INVISIBLE);
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,m);
		
		//设置下拉列表的风格  
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		  //将adapter 添加到spinner中  
        spinner1.setAdapter(adapter);
        
      //添加事件Spinner事件监听    
        spinner1.setOnItemSelectedListener(new SpinnerSelectedListener());  
        Intent intent=getIntent();
		myid=intent.getStringExtra("id");//获取传过来的id
       
	    mFrom = new String[]{"lessonname","marks"};  
		mTo = new int[]{R.id.lessonname,R.id.marks};
		mList = new ArrayList<Map<String,String>>();  
		mAdapter = new SimpleAdapter(MarksinfoActivity.this,mList,R.layout.marksitem,mFrom,mTo);  
	}
	
	 private Thread thread=new Thread(){
	    	
	    	public void run()
	    	{
	    		Looper.prepare();
	    		callGettingMarksMethod();
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
	                  case 0: mWaitingformarks.setVisibility(mWaitingformarks.INVISIBLE);
				                int count =markinfo.getPropertyCount();
				  				if(count==0)
				  				{
				  					Text.setVisibility(View.VISIBLE);
				  					Text.setText("无本学期成绩");
				  					
				  				}
				  				else
				  				{
				  					Text.setVisibility(View.INVISIBLE);
				  					for(int i=0;i<count;i++)
				  					{
				  						SoapObject currentitem=(SoapObject)markinfo.getProperty(i);
				  						Map<String,String> mMap = new HashMap<String,String>(); 
				  						mMap.put("lessonname", currentitem.getProperty("Lessonname").toString());
				  						mMap.put("marks", currentitem.getProperty("Marks").toString());
				  						
				  						
				  					    mList.add(mMap);
				  					}
				  				}
				  				//SimpleAdapter mAdapter = new SimpleAdapter(MarksinfoActivity.this,mList,R.layout.marksitem,mFrom,mTo);  
				  				mListView.setAdapter(mAdapter);  
				      	    	 break;
	                  
	                       
	             } 
	             super.handleMessage(msg); 
	        } 
	   }; 
	    
	    private void callGettingMarksMethod()
		{
			
			try
			{
				SoapObject rpc = new SoapObject(NameSpace, MethodName);
				rpc.addProperty("id",myid);
				rpc.addProperty("xq",spinner1.getSelectedItem().toString());
				AndroidHttpTransport ht = new AndroidHttpTransport(url);
				ht.debug = true;
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.bodyOut = rpc;
				envelope.dotNet = true; 
				envelope.setOutputSoapObject(rpc);
				ht.call(soapAction, envelope); 
				
				SoapObject result =(SoapObject)envelope.bodyIn;
				//System.out.println(result.toString());
			    markinfo=(SoapObject)result.getProperty(0);
				
			}
			catch(Exception e)
	    	{
	    		
	    		Toast toast=Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
	 			 toast.show();
	    		e.printStackTrace();
	    	}
			
		}
	
	 class SpinnerSelectedListener implements OnItemSelectedListener
     {
            //设置下拉框点击事件
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id)
			{
				// TODO Auto-generated method stub
				
				mWaitingformarks.setVisibility(mWaitingformarks.VISIBLE);
				mList.clear();
				
				mListView.setAdapter(mAdapter);  
				Thread gettingMarksThread=new Thread(thread);
				gettingMarksThread.start();
				
				
			
			}
			
			

			@Override
			public void onNothingSelected(AdapterView<?> parent)
			{
				// TODO Auto-generated method stub
				
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
		getMenuInflater().inflate(R.menu.marksinfo, menu);
		return true;
	}

}
