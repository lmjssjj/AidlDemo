package com.lmjssjj.aidl;
import com.lmjssjj.aidl.IClientCallback; 
interface IService {

	void play();
	
    void registerCallback(IClientCallback cb);     
    void unregisterCallback(IClientCallback cb);  
}

