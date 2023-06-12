package com.weilun.uniplugin_wxpay.wxapi;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.weilun.uniplugin_wxpay.receiver.ScanCodeBroadcastReceiver;
import com.weilun.uniplugin_wxpay.utils.Constant;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Set;

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
        String action = intent.getAction();
        String scheme = intent.getScheme();
        Set<String> categories = intent.getCategories();
        Log.e("TAG", "data===========" + data);
        Log.e("TAG", "action===========" + action);
        Log.e("TAG", "categories===========" + categories);
        Log.e("TAG", "DataString===========" + intent.getDataString());
        Log.e("TAG", "==============================");
        Log.e("TAG", "scheme===========" + scheme);
        Log.e("TAG", "id ===========" + data.getQueryParameterNames());
        Log.e("TAG", "encodedQuery ===========" + data.getEncodedQuery());
        Log.e("TAG", "host===========" + data.getHost());
        Log.e("TAG", "path===========" + data.getPath());
        Log.e("TAG", "port===========" + data.getPort());
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Constant.ACTION_PAY_BRE);
        //code=cancel&errmsg=%E6%94%AF%E4%BB%98%E5%B7%B2%E5%8F%96%E6%B6%88
        String encodedQuery = data.getEncodedQuery();
        if (encodedQuery != null) {
            try {
                Intent intentSend = new Intent(Constant.ACTION_PAY_BRE);
                String[] split = encodedQuery.split("&");
                String[] splitCode = split[0].split("=");
                intentSend.putExtra("code", splitCode[1]);
                String[] splitErrmsg = split[1].split("=");
                String errmsg = URLDecoder.decode(splitErrmsg[1], "utf-8");
                intentSend.putExtra("success", splitCode[1].equals("success"));
                intentSend.putExtra("errmsg", errmsg);
                intentSend.setComponent(new ComponentName(this, ScanCodeBroadcastReceiver.class));
                sendBroadcast(intentSend);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        finish();
    }

}
