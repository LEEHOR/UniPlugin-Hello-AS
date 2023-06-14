package com.weilun.uniplugin_pay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.weilun.uniplugin_pay.utils.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 李浩
 * @version 1.0
 * @description: TODO
 * @date 2023/6/14 11:54
 */
public class AliPayBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String code = intent.getStringExtra("code");
        String errmsg = intent.getStringExtra("errmsg");
        boolean success = intent.getBooleanExtra("success", false);
        Log.e("TAG", "code===" + code + ";errmsg===" + errmsg + ";success===" + success);
        Map<String, Object> params = new HashMap<>();
        params.put("success", success);
        params.put("code", code);
        params.put("errmsg", errmsg);
        if (Constant.mAliUniSDKInstances != null) {
            Constant.mAliUniSDKInstances.fireGlobalEventCallback("allinPayEvent", params);
        }

//        context.fireGlobalEventCallback("allinPay", params);
    }
}
