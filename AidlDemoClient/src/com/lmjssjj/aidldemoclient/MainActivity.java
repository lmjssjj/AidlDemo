package com.lmjssjj.aidldemoclient;

import com.lmjssjj.aidl.IClientCallback;
import com.lmjssjj.aidl.IService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private IService mRemoteService;
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.tv);
	}

	public void conn(View v){
		boolean b = bindService(getRemoteIntent(), mServiceConnection, Context.BIND_AUTO_CREATE);
		Log.v("lmjssjj", "isbind:"+b);
	}
	public void cun(View v){
		try {
			mRemoteService.play();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Intent getRemoteIntent() {
		Intent service = new Intent();
		service.addCategory(Intent.CATEGORY_DEFAULT);
		ComponentName componentName = new ComponentName("com.lmjssjj.aidldemoService",
				"com.lmjssjj.aidldemoService.PlayService");
		service.setComponent(componentName);
		return service;
	}

	
	private IClientCallback mCallback = new IClientCallback.Stub() {

		@Override
		public void updateState(boolean state) throws RemoteException {
			Log.v("lmjssjj", "service callback data:"+state);
		}

		
	};
	
	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v("lmjssjj", "client conn remote succce");
			mRemoteService = IService.Stub.asInterface(service);
			try {
				mRemoteService.registerCallback(mCallback);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			try {
				mRemoteService.unregisterCallback(mCallback);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			mRemoteService = null;
			Log.v("lmjssjj", "client->onServiceDisconnected");
		}
	};
	
	@Override
	public void onDestroy() {
		
		unbindService(mServiceConnection);
		Log.v("lmjssjj", "onDestroy");
		super.onDestroy();
	}
}
