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
import com.weilun.uniplugin_wxpay.utils.SybUtil;

import java.text.ParseException;
import java.util.Map;
import java.util.TreeMap;

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
            buildPayParam(options);
        }
        jsCallback.invoke(result);
    }

    private void buildPayParam(JSONObject options) {
        try {
            //动态获取appid和allinpay_mpid
            //path=pages/orderDetail/orderDetail?cusid=平台分配的商户号&appid=平台分配的appid&
            // orgid=平台分配的机构号&version=12&trxamt=1&
            // reqsn=商户唯一订单号&notify_url=服务器异步通知页面路径
            // &body=标题&remark=备注
            // &validtime=订单有效时间&limit_pay=no_credit&randomstr=随机字符串&paytype=W06&sign=签名
            ApplicationInfo appInfo = mUniSDKInstance.getContext().getPackageManager().getApplicationInfo(mUniSDKInstance.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String wxsdk_appid = appInfo.metaData.getString("wxsdk_appid");
            String original_mpid = appInfo.metaData.getString("original_mpid");
            String sybcusid = appInfo.metaData.getString("syb_cusid");
            String syb_appids = appInfo.metaData.getString("syb_appid").replaceFirst("a","");
            String sybrsa = appInfo.metaData.getString("syb_rsa");
            String callbackurl = appInfo.metaData.getString("call_back_url");
            TreeMap<String, String> params = new TreeMap<String, String>();
            //商户号
            params.put("cusid", sybcusid);
            //appid
            params.put("appid", syb_appids);
            //orgid
//            params.put("orgid", syb_appids);
            //版本
            params.put("version", "12");
            //金额（分）
            params.put("trxamt", "1");
            //唯一订单号
            params.put("reqsn", "dh" + System.currentTimeMillis());
            params.put("notify_url", callbackurl);
            //订单标题
            params.put("body", "测试小程序支付");
            //订单备注信息
            params.put("remark", "测试小程序支付");
            //订单有效时间
            params.put("validtime", "5");
            params.put("limit_pay", "no_credit");
            //随机字符串
            params.put("randomstr", SybUtil.getValidatecode(8));
            params.put("paytype", "W06");
//            params.put("signtype", "MD5");
            String sign = SybUtil.unionSign(params, sybrsa, "MD5");
            params.put("sign", sign);
            toPay(SybUtil.strAppend(params), sign, wxsdk_appid, original_mpid);
        } catch (Exception e) {
            Log.e(TAG, "toWxPay: " + e.getMessage());
        }

    }

    private void toPay(String payStr, String sign, String wxsdk_appid, String original_mpid) {
        String payParms = payStr;
        String path = "pages/orderDetail/orderDetail";
        IWXAPI api = WXAPIFactory.createWXAPI(mUniSDKInstance.getContext(), wxsdk_appid);
//        api.registerApp(wxsdk_appid);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = original_mpid; // 填小程序原始id
        req.path = path; ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }
}
