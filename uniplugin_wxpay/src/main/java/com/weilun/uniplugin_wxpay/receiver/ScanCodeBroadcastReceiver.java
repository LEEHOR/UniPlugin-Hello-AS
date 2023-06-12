package com.weilun.uniplugin_wxpay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * PDA红外扫码广播接收
 */
public class ScanCodeBroadcastReceiver extends BroadcastReceiver {

    public OnReceiveCode onReceive;
    @Override
    public void onReceive(final Context context, final Intent intent) {
        String code = intent.getStringExtra("code");
        String errmsg = intent.getStringExtra("errmsg");
        boolean success = intent.getBooleanExtra("success",false);
        if (onReceive != null) {
            onReceive.scanCode(code,errmsg,success);
        }


    }
    public interface OnReceiveCode {
        void scanCode(String code,String errmsg,boolean success);
    }

    public void setOnReceive(OnReceiveCode onReceive) {
        this.onReceive = onReceive;
    }
}
