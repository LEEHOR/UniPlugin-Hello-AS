package com.weilun.uniplugin_wxpay;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.text.ParseException;

import io.dcloud.feature.uniapp.common.UniModule;

/**
 * @author 李浩
 * @version 1.0
 * @description: TODO
 * @date 2023/6/8 11:55
 */
public class WxPayModule extends UniModule {
    private static String TAG = "weilun.WxPayModule";

    //    String appId = "wxd930ea5d5a258f4f"; // 填移动应用(App)的 AppId，非小程序的 AppID
//    IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
//
//    WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
//    req.userName = "gh_e64a1a89a0ad"; // 填小程序原始id
//    req.path = path;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
//    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
//api.sendReq(req);
    @JSMethod(uiThread = false)
    public void startPay(JSONObject options, JSCallback jsCallback) throws ParseException {
        JSONObject result = new JSONObject();
        if (options == null) {
            result.put("code", 500);
            result.put("msg", "支付参数不能为空");
        } else {
            String path = options.getString("path");
            result.put("code", 200);
            result.put("msg", "正在支付");
            toWxPay(path);
        }
        jsCallback.invoke(result);
    }

    private void toWxPay(String path) {
        try {
            //动态获取appid和allinpay_mpid
            ApplicationInfo appInfo = mUniSDKInstance.getContext().getPackageManager().getApplicationInfo(mUniSDKInstance.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String wxsdk_appid = appInfo.metaData.getString("wxsdk_appid");
            String allinpay_mpid = appInfo.metaData.getString("allinpay_mpid");
            IWXAPI api = WXAPIFactory.createWXAPI(mUniSDKInstance.getContext(), wxsdk_appid);
//            api.registerApp(wxsdk_appid);
            WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
            req.userName = allinpay_mpid; // 填小程序原始id
            req.path =path; ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
            api.sendReq(req);
        } catch (Exception e) {
            Log.e(TAG, "toWxPay: " + e.getMessage());
        }

    }
}
