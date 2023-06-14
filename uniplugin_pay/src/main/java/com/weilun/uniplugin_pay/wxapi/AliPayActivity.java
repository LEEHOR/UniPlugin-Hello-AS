package com.weilun.uniplugin_pay.wxapi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.weilun.uniplugin_pay.AliPayModule;
import com.weilun.uniplugin_pay.receiver.AliPayBroadcastReceiver;
import com.weilun.uniplugin_pay.utils.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author 李浩
 * @version 1.0
 * @description: TODO
 * @date 2023/6/12 13:55
 */
public class AliPayActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri data = intent.getData();  //
//        String action = intent.getAction();
//        String scheme = intent.getScheme();
//        Set<String> categories = intent.getCategories();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constant.ACTION_PAY_BRE);
        //code=cancel&errmsg=%E6%94%AF%E4%BB%98%E5%B7%B2%E5%8F%96%E6%B6%88
        String encodedQuery = data.getEncodedQuery();
        if (encodedQuery != null) {
            try {
                Intent intentSend = new Intent(Constant.ACTION_PAY_ALI);
                String[] split = encodedQuery.split("&");
                String[] splitCode = split[0].split("=");
                intentSend.putExtra("code", splitCode[1]);
                String[] splitErrmsg = split[1].split("=");
                String errmsg = URLDecoder.decode(splitErrmsg[1], "utf-8");
                intentSend.putExtra("success", splitCode[1].contains("success"));
                intentSend.putExtra("errmsg", errmsg);
                intentSend.setComponent(new ComponentName(this, AliPayBroadcastReceiver.class));
                sendBroadcast(intentSend);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
       finish();
    }

}
