package com.flashzhang.ypilibrary;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
public class LoginActivity extends Activity
{
    private Button loginbutton;
    private EditText user;
    private EditText pass;
    private ProgressBar progressBar;
    private String NameSpace = "http://tempuri.org/";
    private String MethodName = "loginCheck";
    private String url = GlobalSetting.url;
    private String soapAction = NameSpace + MethodName;
    
    @Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		user=(EditText)findViewById(R.id.username_edit);
		pass=(EditText)findViewById(R.id.password_edit);
		loginbutton=(Button)findViewById(R.id.signin_button);
		progressBar=(ProgressBar)findViewById(R.id.progressBar1);
		loginbutton.setOnClickListener(new OnClickListener(){
	    	
	    	

			@Override
			public void onClick(View v)
			{
				
				if(user.getText().toString().isEmpty()||pass.getText().toString().isEmpty())
				{
					Toast toast=Toast.makeText(getApplicationContext(), "用户名或密码不能为空", Toast.LENGTH_SHORT);
	       			toast.show();
				}
				else
				{
				   progressBar.setVisibility(progressBar.VISIBLE);
				   Thread loginThread=new Thread(thread);
				   loginThread.start();
				   
				}
				
			}
	    });	
	}
    private Handler myHandler = new Handler() { 
        public void handleMessage(Message msg)
        { 
             switch (msg.what) 
             { 
                  case 0: progressBar.setVisibility(progressBar.INVISIBLE);
      	    	           break;
                  
                       
             } 
             super.handleMessage(msg); 
        } 
   }; 
    private Thread thread=new Thread(){
    	
    	public void run()
    	{
    		Looper.prepare();
    		callLoginMethod();
    		Message message=new Message();
    		message.what=0;
	    	myHandler.sendMessage(message); 
	    	Looper.loop();
    	}
    	
    };
    
    
   
    public void callLoginMethod()
    {
    	try
    	{
    		SoapObject rpc = new SoapObject(NameSpace, MethodName);
    		rpc.addProperty("id1",user.getText().toString());
    		rpc.addProperty("pass",pass.getText().toString());
    		AndroidHttpTransport ht = new AndroidHttpTransport(url);
    		ht.debug = true;
    		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    		envelope.bodyOut = rpc;
    		envelope.dotNet = true; 
    		envelope.setOutputSoapObject(rpc);
    		ht.call(soapAction, envelope); 
    		Object result = (Object) envelope.getResult();
    		
    		if (result != null)
    		{
    			 
    			boolean response=Boolean.parseBoolean(result.toString());
    			if(response==true)
    			{
    		     Toast toast=Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT);
       			 toast.show();
       			Intent intent = new Intent();
       			intent.putExtra("id", user.getText().toString());
       			intent.setClass(this,MainActivity.class);
       			startActivity(intent); 
    			}
    			else
    			{
    				 Toast toast=Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT);
          			 toast.show();
    			}
    			
    			 
    		}
    		else
    		{
    			Toast toast=Toast.makeText(getApplicationContext(), "服务暂时不可用，请稍后再试", Toast.LENGTH_SHORT);
      			 toast.show();
    			
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
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
