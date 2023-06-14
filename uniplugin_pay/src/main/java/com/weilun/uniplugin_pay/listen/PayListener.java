package com.weilun.uniplugin_pay.listen;

public interface PayListener {
    void scanCode(String code,String errmsg,boolean success);
}
