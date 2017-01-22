package com.lmjssjj.aidldemoService;

import com.lmjssjj.aidl.IClientCallback;
import com.lmjssjj.aidl.IService;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class PlayService extends Service {

	private RemoteCallbackList<IClientCallback> mCallbacks = new RemoteCallbackList<IClientCallback>();

	private static final int SUCCEE = 0;
	private static final int FAIL = 1;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCEE:
				updateCallbackStation(true);
				break;
			case FAIL:
				updateCallbackStation(false);
				break;

			default:
				break;
			}
		};
	};

	private final IService.Stub mBinder = new IService.Stub() {

		@Override
		public void play() throws RemoteException {
			manageLogic();
		}

		@Override
		public void registerCallback(IClientCallback cb) throws RemoteException {
			if (cb != null) {
				mCallbacks.register(cb);
			}
		}

		@Override
		public void unregisterCallback(IClientCallback cb) throws RemoteException {
			if (cb != null) {
				mCallbacks.unregister(cb);
			}
		}

	};

	@Override
	public IBinder onBind(Intent intent) {
		Log.v("lmjssjj", "service onBind");
		return mBinder;
	}

	private void manageLogic() {
		new Thread() {
			public void run() {
				try {
					sleep(1000);
					mHandler.sendEmptyMessage(SUCCEE);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					mHandler.sendEmptyMessage(FAIL);
				}
			};
		}.start();
	}
	
	private void updateCallbackStation(boolean state) {
		// 返回boardcast中的回调函数的个数
		final int N = mCallbacks.beginBroadcast();
		for (int i = 0; i < N; i++) {
			try {
				mCallbacks.getBroadcastItem(i).updateState(state);
				Log.v("lmjssjj", "service:callback");
			} catch (RemoteException e) {
				// The RemoteCallbackList will take care of removing
				// the dead object for us.
			}
		}
		mCallbacks.finishBroadcast();
	}

}
