package com.weilun.uniplugin_mqtt.mqttUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author 李浩
 * @version 1.0
 * @description: mqtt 客户端
 * @date 2023/4/18 15:03
 */
public class MqttUtils {
    private static MqttUtils instance;
    private Handler handler = null;
    private Context mContext;
    private MqttAndroidClient mqttAndroidClient;
    private ScheduledExecutorService scheduler;
    private String mUserName, mPassword;
    //是否连接成功
    private boolean isConnect;
    private MqttConnectOptions mqttConnectOptions;

    public static MqttUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (MqttUtils.class) {
                if (instance == null) {
                    instance = new MqttUtils(context);
                }
            }
        }

        return instance;
    }

    public MqttUtils() {
    }

    public MqttUtils(Context context) {
        this.mContext = context;
        handler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1: //开机校验更新回传
                        break;
                    case 2:  // 反馈回传
                        break;
                    case 3:  //MQTT 收到消息回传   UTF8Buffer msg=new UTF8Buffer(object.toString());
//                        System.out.println(msg.obj.toString());   // 显示MQTT数据
                        break;
                    case 30:  //连接失败
                        Toast.makeText(mContext, "连接失败", Toast.LENGTH_SHORT).show();
                        isConnect = false;
                        break;
                    case 31:   //连接成功
                        Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
                        isConnect = true;
                        break;
                    default:
                        break;
                }
            }
        };
    }

    /**
     * 初始化mqtt
     *
     * @param serverURI 连接地址
     */
    public void initMqtt(String serverURI, String clientId, String userName, String password) {
        this.mUserName = userName;
        this.mPassword = password;
        if (mqttAndroidClient == null) {
            Log.e("mqtt","初始化");
            mqttAndroidClient = new MqttAndroidClient(mContext, serverURI, clientId);
        }

        mqttConnect();
        mqttAndroidClient.setCallback(mqttCallback);
    }

    /**
     * mqtt配置参数
     *
     * @return
     */
    public MqttConnectOptions initMqttOptions() {
        if (mqttConnectOptions != null) {
            Log.e("mqtt","连接参数");
            mqttConnectOptions = new MqttConnectOptions();
            // 设置是否清空Session，true表示每次链接都会是新的状态
            mqttConnectOptions.setCleanSession(true);
            if (mUserName != null && !mUserName.equals("")) {
                // 账户
                mqttConnectOptions.setUserName(mUserName);
                // 密码
                mqttConnectOptions.setPassword(mPassword.toCharArray());
            }
            // 设置超时时间 单位为秒
            mqttConnectOptions.setConnectionTimeout(30);
            // 设置断开后重新连接
            mqttConnectOptions.setAutomaticReconnect(true);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            mqttConnectOptions.setKeepAliveInterval(10);
        }
        return mqttConnectOptions;
    }

    /**
     * mqtt连接
     */
    public void mqttConnect() {
        if (mqttAndroidClient != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(mqttAndroidClient.isConnected()))  //如果还未连接
                        {
                            Log.e("mqtt","开始连接");
                            mqttAndroidClient.connect(initMqttOptions());
                            Message msg = new Message();
                            msg.what = 31;
                            handler.sendMessage(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Message msg = new Message();
                        msg.what = 30;
                        handler.sendMessage(msg);
                    }
                }
            }).start();
        }
    }

    /**
     * mqtt重连
     */
    public void mqttReconnect() {
        if (mqttAndroidClient != null) {
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if (!mqttAndroidClient.isConnected()) {
                        Log.e("mqtt","重新连接");
                        mqttConnect();
                    }
                }
            }, 0, 10 * 1000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * 发布消息
     *
     * @param topic
     * @param message2
     */
    public void publishMessagePlus(String topic, String message2) {
        if (mqttAndroidClient == null || !mqttAndroidClient.isConnected()) {
            return;
        }
        MqttMessage message = new MqttMessage();
        message.setPayload(message2.getBytes());
        try {
            mqttAndroidClient.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否连接
     *
     * @return
     */
    public boolean mqttIsConnect() {
        if (mqttAndroidClient != null) {
            return mqttAndroidClient.isConnected();
        }
        return false;
    }

    /**
     * 断开连接
     */
    public void mqttDisConnect() {
        if (mqttAndroidClient != null) {
            try {
                mqttAndroidClient.disconnect(0);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void connectionLost(Throwable cause) {
            //丢失链接
            Log.e("mqtt","丢失连接:"+cause.getMessage());
            mqttReconnect();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            //publish后会执行到这里
            Log.e("mqtt","发布成功:"+message.getPayload().toString());
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            //subscribe后得到的消息会执行到这里面
            try {
                Log.e("mqtt","订阅消息:"+token.getMessage().getPayload().toString());
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    };


}
