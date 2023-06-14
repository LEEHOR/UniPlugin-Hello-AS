package com.weilun.uniplugin_pay.utils;

import io.dcloud.feature.uniapp.AbsSDKInstance;

/**
 * @author 李浩
 * @version 1.0
 * @description: TODO
 * @date 2023/6/12 16:14
 */
public class Constant {
    //支付后的广播
    public static final String ACTION_PAY_WX="action_pay_wx";

    public static final String ACTION_PAY_ALI="action_pay_ali";

    public static AbsSDKInstance mAliUniSDKInstances;
    public static AbsSDKInstance mWxiUniSDKInstances;
}
