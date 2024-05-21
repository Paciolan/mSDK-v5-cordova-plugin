package com.paciolan.paciolanmsdk;

import android.util.Log;
import android.content.Context;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.paciolan.mobileSDK.MobileSDK;
import com.paciolan.mobileSDK.TokenCallback;
import com.paciolan.mobileSDK.MobileSDKActivity;
import com.paciolan.mobileSDK.PaciolanReactPackage;
import com.paciolan.mobileSDK.PaciolanSDKAndroidUtils;

public class PaciolanSDK extends CordovaPlugin implements TokenCallback {

  private static final String TAG = "PaciolanSDKPlugin";
  private CallbackContext callbackPtr;
  private Boolean hasCallbackPtr = false;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    Log.d(TAG, "Initializing " + TAG);
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) {
    try {

      Method method = getClass().getMethod(action, JSONArray.class, CallbackContext.class);
      try {
        method.invoke(this, args, callbackContext);
        return true;

      } catch (IllegalAccessException e) {

        JSONObject error = errorResult(e);
        callbackContext.error(error);

        e.printStackTrace();
      } catch (IllegalArgumentException e) {

        JSONObject error = errorResult(e);
        callbackContext.error(error);

        e.printStackTrace();

      } catch (InvocationTargetException e) {

        JSONObject error = errorResult(e);
        callbackContext.error(error);

        e.printStackTrace();
      }
    } catch (NoSuchMethodException e) {

      String message = String.format("Method with name: %s was not found on: %s\n Reason: %s", action, getClass().getName(), e.getMessage());

      Log.d(TAG, message);

      HashMap<String, Object> data = new HashMap<String, Object>();
      data.put("message", message);

      JSONObject error = errorResult(e);
      callbackContext.error(error);

      e.printStackTrace();
    }

    return false;
  }

  public void show(JSONArray args, final CallbackContext callbackContext) throws JSONException {
    cordova.getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        try {
          MobileSDK.startPaciolanActivity(cordova.getActivity(), args.optString(0));
          JSONObject success = new JSONObject();
          success.put("success", true);
          success.put("message", "Paciolan SDK started");

          callbackContext.success(success);
        } catch (Exception e) {
          JSONObject error = errorResult(e);
          callbackContext.error(error);
          e.printStackTrace();
        }
      }
    });
  }
  
  
  public void onTokenChanged(JSONArray args, CallbackContext callbackContext) throws JSONException {
    this.callbackPtr = callbackContext;
    this.hasCallbackPtr = true;
    MobileSDK.setTokenListener(this);
  }

  @Override
  public void onTokenChange(String token){
    if(hasCallbackPtr){
      this.callbackPtr.success(token);
    }
  }

  protected JSONObject errorResult(Exception e) {
    HashMap<String, Object> data = new HashMap<String, Object>();
    data.put("success", false);
    data.put("name", e.getClass().getName());
    data.put("message", e.getMessage() != null ? e.getMessage() : e.getCause().getMessage());

    JSONObject error = new JSONObject(data);
    return error;
  }

  public static void appLaunched() {
      PaciolanSDKAndroidUtils module = PaciolanReactPackage.getMReactContext()
        .getNativeModule(PaciolanSDKAndroidUtils.class);
      if (module != null) {
          module.emitEvent("AppLaunchedEvent", "");
      }
  }


  public static void navAwayFromPac(callback: UserChoiceCallback) {
    PaciolanSDKAndroidUtils module = PaciolanReactPackage.getMReactContext()
      .getNativeModule(PaciolanSDKAndroidUtils.class);
      if (module != null) {
          module.emitEvent("navAwayEvent", "message from canNavAway on Tab Pressed")
      }
    this.userChoiceCallback = callback
  }
}
