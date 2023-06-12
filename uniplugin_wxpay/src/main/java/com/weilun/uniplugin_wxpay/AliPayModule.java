package com.weilun.uniplugin_wxpay;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.weilun.uniplugin_wxpay.receiver.ScanCodeBroadcastReceiver;
import com.weilun.uniplugin_wxpay.utils.Constant;
import com.weilun.uniplugin_wxpay.utils.SybUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import io.dcloud.feature.uniapp.common.UniModule;

/**
 * @author 李浩
 * @version 1.0
 * @description: TODO
 * @date 2023/6/8 11:55
 */
public class AliPayModule extends UniModule implements ScanCodeBroadcastReceiver.OnReceiveCode {
    private static String TAG = "weilun.WxPayModule";
    private ScanCodeBroadcastReceiver scanCodeBroadcastReceiver;

    /*
     * String json = "{" +
             "\"cusid\": \"平台分配的商户号\"," +
             "\"appid\": \"平台分配的appid\"," +
             "\"orgid\": \"平台分配的机构号\"," +
             "\"version\": \"12\"," +
             "\"trxamt\": \"1\"," +
             "\"reqsn\": \"商户唯一订单号\"," +
             "\"notify_url\": \"服务器异步通知页面路径\"," +
             "\"body\": \"标题\"," +
             "\"remark\": \"备注\"," +
             "\"validtime\": \"订单有效时间\"," +
             "\"limit_pay\": \"no_credit\"," +
             "\"randomstr\": \"随机字符串\"," +
             "\"paytype\": \"A02\"," +
             "\"sign\": \"签名，参考2.1\"" +
             "}";
     String query = URLEncoder.encode("payinfo=" + URLEncoder.encode(json, "UTF-8"), "UTF-8");
     String url =
             "alipays://platformapi/startapp?appId=2021001104615521&page=pages/orderDetail/orderDetail&thirdPartSchema="
                     +  URLEncoder.encode("APP接收跳转的schemeurl", "UTF-8")
                     + "&query=" + query;*/
    @JSMethod(uiThread = false)
    public void startPay(JSONObject options, JSCallback jsCallback) throws ParseException {
//        scanCodeBroadcastReceiver = new ScanCodeBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constant.ACTION_PAY_BRE);
//        mUniSDKInstance.getContext().registerReceiver(scanCodeBroadcastReceiver, intentFilter);
//        scanCodeBroadcastReceiver.setOnReceive(this::scanCode);
        JSONObject result = new JSONObject();
        if (options == null) {
            result.put("code", 500);
            result.put("msg", "支付参数不能为空");
        } else {
            String trxamt = options.getString("trxamt");
            String body = options.getString("body");
            String remark = options.getString("remark");
            if (trxamt == null) {
                result.put("code", 500);
                result.put("msg", "支付金额不能为空");
            } else {
                buildPayParam(trxamt, body, remark);
            }
        }
        jsCallback.invoke(result);
    }

    private void buildPayParam(String trxamt, String body, String remark) {
        try {
            //动态获取appid和allinpay_mpid
            //path=pages/orderDetail/orderDetail?cusid=平台分配的商户号&appid=平台分配的appid&
            // orgid=平台分配的机构号&version=12&trxamt=1&
            // reqsn=商户唯一订单号&notify_url=服务器异步通知页面路径
            // &body=标题&remark=备注
            // &validtime=订单有效时间&limit_pay=no_credit&randomstr=随机字符串&paytype=W06&sign=签名
            ApplicationInfo appInfo = mUniSDKInstance.getContext().getPackageManager().getApplicationInfo(mUniSDKInstance.getContext().getPackageName(), PackageManager.GET_META_DATA);
            String aliappid = appInfo.metaData.getString("ali_appid").replaceFirst("a", "");
            String sybcusid = appInfo.metaData.getString("syb_cusid");
            String syb_appids = appInfo.metaData.getString("syb_appid").replaceFirst("a", "");
            String sybrsa = appInfo.metaData.getString("syb_rsa");
            String callbackurl = appInfo.metaData.getString("call_back_url");
            TreeMap<String, String> params = new TreeMap<String, String>();
            //商户号
            params.put("cusid", sybcusid);
            //appid
            params.put("appid", syb_appids);
            //orgid
//            params.put("orgid", syb_appids);
            //sub_appid
            params.put("sub_appid", syb_appids);
            //版本
            params.put("version", "12");
            //金额（分）
            params.put("trxamt", trxamt);
            //唯一订单号
            params.put("reqsn", "dh" + System.currentTimeMillis());
            params.put("notify_url", callbackurl);
            //订单标题
            if (body != null) {
                params.put("body", body);
            }
            //订单备注信息
            if (remark != null) {
                params.put("remark", remark);
            }

            //订单有效时间
            params.put("validtime", "5");
            params.put("limit_pay", "no_credit");
            //随机字符串
            params.put("randomstr", SybUtil.getValidatecode(8));
            params.put("paytype", "A02");
//            params.put("signtype", "MD5");
            String sign = SybUtil.unionSign(params, sybrsa, "MD5");
            params.put("sign", sign);
            toPay(JSONObject.toJSONString(params), aliappid);
        } catch (Exception e) {
            Log.e(TAG, "toAliPay: " + e.getMessage());
        }

    }

    private void toPay(String payStr, String appId) throws UnsupportedEncodingException {
        String query = URLEncoder.encode("payinfo=" + URLEncoder.encode(payStr, "UTF-8"), "UTF-8");
        String url = "alipays://platformapi/startapp?appId=" + appId + "&page=pages/orderDetail/orderDetail&thirdPartSchema="
                + URLEncoder.encode("leehorallinpaysdk://sdk/", "UTF-8")
                + "&query=" + query;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
        Log.e(TAG, "action是:" + intentUri);
        mUniSDKInstance.getContext().startActivity(intent);
    }

    private void registerReceiver() {
        scanCodeBroadcastReceiver = new ScanCodeBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_PAY_BRE);
        mUniSDKInstance.getContext().registerReceiver(scanCodeBroadcastReceiver, intentFilter);
        scanCodeBroadcastReceiver.setOnReceive(this::scanCode);
    }

    @Override
    public void onActivityCreate() {
        super.onActivityCreate();
        registerReceiver();
    }

    @Override
    public void onActivityDestroy() {
        super.onActivityDestroy();
        unRegisterReceiver();
    }

    /**
     * 取消注册红外广播
     */
    private void unRegisterReceiver() {
        if (scanCodeBroadcastReceiver != null) {
            mUniSDKInstance.getContext().unregisterReceiver(scanCodeBroadcastReceiver);
        }
        scanCodeBroadcastReceiver = null;
    }

    @Override
    public void scanCode(String code, String errmsg, boolean success) {
        Log.e("TAG", "code===" + code + ";errmsg===" + errmsg + ";success===" + success);
        Map<String, Object> params = new HashMap<>();
        params.put("success", success);
        params.put("code", code);
        params.put("errmsg", errmsg);
        mUniSDKInstance.fireGlobalEventCallback("allinPay", params);
    }
}
