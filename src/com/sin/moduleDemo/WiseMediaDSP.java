package com.sin.moduleDemo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import cn.wisemedia.mob.WMMobSDK;

import com.sin.cbcode.DSPcbcode;
import com.uzmap.pkg.uzcore.UZWebView;
import com.uzmap.pkg.uzcore.uzmodule.UZModule;
import com.uzmap.pkg.uzcore.uzmodule.UZModuleContext;

public class WiseMediaDSP extends UZModule {
	
	private UZModuleContext mJsCallback;
	
	public WiseMediaDSP(UZWebView webView) {
		super(webView);
	}

	public void jsmethod_sendDSPPoint(final UZModuleContext moduleContext){
		mJsCallback = moduleContext;
		String clientid = moduleContext.optString("clientid");
        String pointid = moduleContext.optString("pointid");
		if(!checkPara(clientid,pointid)){
			clientid = "wt";
			pointid = "dG";
		}
		
		Activity context = getContext();
		WifiManager wifi_mac = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifi_mac.getConnectionInfo().getMacAddress();
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String androidId = android.provider.Settings.Secure.getString(context.getApplication().getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);

		String mac = wifi_mac.getConnectionInfo().getMacAddress();
		String imei = telephonyManager.getDeviceId();
		String udid = androidId;
		if(checkPara(mac, imei, udid)){
			listenLog();
			WMMobSDK sdk = new WMMobSDK(context, clientid, pointid, mac, imei, udid);// 初始化
			sdk.sendPoint();// 请求营销点
		}else{
			execCallBack("error", DSPcbcode._CANTGET);
		}
		
	}
	
	/**
	 * listen to the logcat
	 */
	private void listenLog() {
		new Thread(){
			public void run() {
				Process mLogcatProc = null;
				BufferedReader reader = null;
				try {
					/**
					 * 获取LogCat日志信息
					 */
					mLogcatProc = Runtime.getRuntime().exec(new String[] { "logcat", "WMMOB *:S" });
					reader = new BufferedReader(new InputStreamReader(mLogcatProc.getInputStream()));
					String line = "";
					while ((line = reader.readLine()) != null) {
						if (line.indexOf("发送成功") > 0) {
							break;
						} 
					}
					Message msg = new Message();
					msg.what = 1001;
					msg.obj = line;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1001)
				execCallBack("success", DSPcbcode.setFailureCode(0, msg.obj.toString()));
		}
	};
	
    /**
     * check param
     *
     * @param param
     * @return
     */
    public static boolean checkPara(String... param) {
        if (null == param) {
            return false;
        } else if (param.length > 0) {
            for (String str : param) {
                if (null == str || str.equals("") || str.equals("null")) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * function of callback
     * 
     * @param result
     */
    private void execCallBack(String type, JSONObject result) {
        if(null != mJsCallback){
            if (type.equals("success"))
                mJsCallback.success(result, true);
            else
                mJsCallback.error(null, result, true);
            mJsCallback = null;
        }
    }
}
