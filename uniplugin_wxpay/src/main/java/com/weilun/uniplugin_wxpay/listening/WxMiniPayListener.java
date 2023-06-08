package com.weilun.uniplugin_wxpay.listening;

import java.util.Map;

/**
 * @author 李浩
 * @version 1.0
 * @description: TODO
 * @date 2023/6/8 15:18
 */
public interface WxMiniPayListener {
    void paySuccessResult(Map<String, String> map);

    void payErrorResult(Map<String, String> map);
}
