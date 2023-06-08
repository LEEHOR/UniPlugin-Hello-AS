package com.weilun.uniplugin_mqtt;

import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.weilun.uniplugin_mqtt.mqttUtils.MqttUtils;

import java.text.ParseException;

import io.dcloud.feature.uniapp.common.UniModule;

/**
 * @author 李浩
 * @version 1.0
 * @description: mqtt初始化
 * @date 2023/4/18 15:44
 */
public class MqttModule extends UniModule {

    private MqttUtils mqttUtils;

    private void initMqttUtils(String serverURI, String clientId, String userName, String password) {
        mqttUtils = MqttUtils.getInstance(mUniSDKInstance.getContext());
        mqttUtils.initMqtt(serverURI, clientId, userName, password);
    }

    /**
     * 初始化 并连接mqtt
     *
     * @param options
     * @param jsCallback
     * @throws ParseException
     */
    @JSMethod(uiThread = false)
    public void initMqtt(JSONObject options, JSCallback jsCallback) throws ParseException {
        if (options == null) {
            JSONObject result = new JSONObject();
            result.put("code", 500);
            result.put("msg", "mqtt初始化失败");
            jsCallback.invoke(result);
        }
        String serverURI = options.getString("serverURI");
        String clientId = options.getString("clientId");
        String userName = options.getString("userName");
        String password = options.getString("password");
        this.initMqttUtils(serverURI, clientId, userName, password);
    }

    @JSMethod(uiThread = false)
    public void sendMessage(JSONObject options, JSCallback jsCallback) throws ParseException {
        if (mqttUtils != null) {
            String topic = options.getString("topic");
            String message = options.getString("message");
            if (topic == null || message == null) {
                return;
            }
            mqttUtils.publishMessagePlus(topic, message);
        }
    }

    /**
     * 判断是否连接
     *
     * @param options
     * @param jsCallback
     * @throws ParseException
     */
    @JSMethod(uiThread = false)
    public void mqttIsConnect(JSONObject options, JSCallback jsCallback) throws ParseException {
        JSONObject result = new JSONObject();
        result.put("code", 200);
        if (mqttUtils != null) {
            result.put("isConnect", mqttUtils.mqttIsConnect());
        } else {
            result.put("isConnect", false);
        }
        jsCallback.invoke(result);
    }

    /**
     * 断开连接
     *
     * @param options
     * @param jsCallback
     * @throws ParseException
     */
    @JSMethod(uiThread = false)
    public void disConnectMqtt(JSONObject options, JSCallback jsCallback) throws ParseException {
        if (mqttUtils != null) {
            mqttUtils.mqttDisConnect();
        }
    }
}
