package com.weilun.uniplugin_pay.wxapi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.weilun.uniplugin_pay.WxPayModule;
import com.weilun.uniplugin_pay.receiver.WxPayBroadcastReceiver;
import com.weilun.uniplugin_pay.utils.Constant;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = "MicroMsg.WXEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //动态获取appid
            ApplicationInfo appInfo = getApplication().getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String wxsdk_appid = appInfo.metaData.getString("wxsdk_appid");
            api = WXAPIFactory.createWXAPI(this, wxsdk_appid, false);
            api.handleIntent(getIntent(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.e(TAG, "req: " + req.toString());
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_LAUNCH_WX_MINIPROGRAM) {
            //extMsg  errCode;
            WXLaunchMiniProgram.Resp launchMiniProResp = (WXLaunchMiniProgram.Resp) resp;
            String extMsg = launchMiniProResp.extMsg; //对应小程序组件 <button open-type="launchApp"> 中的 app-parameter 属性
            int errCode = launchMiniProResp.errCode;
            //success：支付成功
            //code 响应码   success：支付成功; cancel：取消支付; fail：支付失败
            //extMsg 响应内容  success：支付成功;cancel：取消支付;其他：支付失败，内容为失败原因
            Intent intentSend = new Intent(Constant.ACTION_PAY_WX);
            intentSend.putExtra("code", String.valueOf(errCode));
            intentSend.putExtra("success", extMsg.contains("success"));
            intentSend.putExtra("errmsg", extMsg);
            intentSend.setComponent(new ComponentName(this, WxPayBroadcastReceiver.class));
            sendBroadcast(intentSend);
        }
        finish();
    }
}