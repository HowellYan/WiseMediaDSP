package com.sin.cbcode;

import org.json.JSONException;
import org.json.JSONObject;

public class DSPcbcode {
	public static JSONObject _ONFAILURE;// error code
	public static JSONObject _SUCCESS;// 0 order success
	public static JSONObject _CANTGET;// 90002 Parameters can not be empty.

	public static JSONObject setFailureCode(int code, String message) {
		if (message.equals("") || (null == message))
			message = "";
		try {
			_ONFAILURE = new JSONObject("{\"code\":" + code + ",\"message\":\"" + message + "\"}");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return _ONFAILURE;
	}

	public DSPcbcode() {
		try {
			_SUCCESS = new JSONObject("{\"code\":0,\"message\":\"success\"}");
			_CANTGET = new JSONObject("{\"code\":90000,\"message\":\"Can not get device information\"}");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
