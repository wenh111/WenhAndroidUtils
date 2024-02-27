package com.wenh.baselibrary.mqtt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;


import com.wenh.baselibrary.Base;
import com.wenh.baselibrary.callback.Callback;

import org.apache.log4j.Logger;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@SuppressLint("StaticFieldLeak")
public class MqttClient {

    private Callback<Boolean> connectCallback;
    private SubscribeCallback<String> subscribeCallback;
    public final String TAG = MqttClient.class.getSimpleName();

    private static MqttAndroidClient mqttAndroidClient;
    private MqttConnectOptions mMqttConnectOptions;
    public String HOST = "";//服务器地址（协议+地址+端口号）
    public String USERNAME;
    public String PASSWORD = "123456";//密码
    public static String PUBLISH_TOPIC = "room_ZVnFdLiDR8VUtVTBVSERDFWlZo9-conference-register";//发布主题
    public static String RESPONSE_TOPIC = "message_arrived";//响应主题
    public String CLIENT_ID;//客户端ID，一般以客户端唯一标识符表示，这里用设备序列号表示

    private boolean autoReConnect = true;

    public static Logger logger = Logger.getLogger(MqttClient.class);

    public void setAutoReConnect(boolean autoReConnect) {
        this.autoReConnect = autoReConnect;
    }

    public MqttClient(String clientId, String username, String serverIp, String tcpPort) {
        CLIENT_ID = clientId;
        USERNAME = username;
        setHost(serverIp, tcpPort);
    }

    public void setHost(String serverIp, String tcpPort) {
        this.HOST = "tcp://" + serverIp + ":" + tcpPort;
    }

    /**
     * 发布 （模拟其他客户端发布消息）
     *
     * @param message 消息
     */
    public static void publish(String topic, String message) {
        //String topic = PUBLISH_TOPIC;
        int qos = 1;
        boolean retained = false;
        logger.info(message);
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(topic, message.getBytes(), qos, retained);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 响应 （收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等）
     *
     * @param message 消息
     */
    public void response(String message) {
        String topic = RESPONSE_TOPIC;
        int qos = 1;
        boolean retained = false;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mqttAndroidClient.publish(topic, message.getBytes(), qos, retained);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     */
    private void init() {
        String serverURI = HOST; //服务器地址（协议+地址+端口号）
        mqttAndroidClient = new MqttAndroidClient(Base.getContext(), serverURI, CLIENT_ID);
        mqttAndroidClient.setCallback(mqttCallback); //设置监听订阅消息的回调
        mMqttConnectOptions = new MqttConnectOptions();
        mMqttConnectOptions.setCleanSession(true); //设置是否清除缓存
        //mMqttConnectOptions.setConnectionTimeout(3); //设置超时时间，单位：秒
        //mMqttConnectOptions.setKeepAliveInterval(4); //设置心跳包发送间隔，单位：秒
        mMqttConnectOptions.setUserName(USERNAME); //设置用户名
        mMqttConnectOptions.setPassword(PASSWORD.toCharArray()); //设置密码

        // last will message
        boolean doConnect = true;
        String message = "{\"terminal_uid\":\"" + CLIENT_ID + "\"}";
        int qos = 1;
        Boolean retained = false;
        // 最后的遗嘱
        try {
            //服务器获取设备是否离线topic
            String EXCEPTION_TOPIC = "ForTest";
            mMqttConnectOptions.setWill(EXCEPTION_TOPIC, message.getBytes(), (int) qos, retained.booleanValue());
        } catch (Exception e) {
            Log.i(TAG, "Exception Occured", e);
            doConnect = false;
            iMqttActionListener.onFailure(null, e);
        }
        if (doConnect) {
            connect();
        }
    }

    /**
     * 连接MQTT服务器
     */
    public void connect() {
        if (mqttAndroidClient == null) {
            init();
            return;
        }

        if (!mqttAndroidClient.isConnected() && isNetwork()) {
            try {
                mqttAndroidClient.connect(mMqttConnectOptions, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return mqttAndroidClient != null && mqttAndroidClient.isConnected();
    }

    /**
     * 判断网络是否连接
     */
    private boolean isNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) Base.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "没有可用网络");
            /*没有可用网络的时候，延迟3秒再尝试重连*/
            reconnect();
            return false;
        }
    }


    private void reconnect() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!autoReConnect) return;

                connect();
            }
        }, 3000);
    }


    /**
     * QoS0，At most once，至多一次；
     * QoS1，At least once，至少一次；
     * QoS2，Exactly once，确保只有一次（开销大）。
     */
    public void subscribe(String topic) {
        if (mqttAndroidClient == null) {
            return;
        }
        try {
            mqttAndroidClient.subscribe(topic, 1);//订阅主题，参数：主题、服务质量
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe(String topic) {
        if (mqttAndroidClient == null) return;
        try {
            mqttAndroidClient.unsubscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    //MQTT是否连接成功的监听
    private final IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            if (connectCallback != null) connectCallback.onCallback(true);
            //连接成功，开始订阅消息
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            if (connectCallback != null) connectCallback.onCallback(false);
            Log.i(TAG, "连接失败 ");
            reconnect();//连接失败，重连（可关闭服务器进行模拟）
        }
    };

    //订阅主题的回调
    private final MqttCallback mqttCallback = new MqttCallback() {

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            //收到消息，这里弹出Toast表示。如果需要更新UI，可以使用广播或者EventBus进行发送
            if (subscribeCallback != null) {
                subscribeCallback.onSubscribe(topic, new String(message.getPayload()));
            }
            //收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等
            //response("message arrived");
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            Log.i(TAG, "连接断开 ");
            if (connectCallback != null) connectCallback.onCallback(false);
            if (autoReConnect) connect();//连接断开，重连
        }
    };

    public synchronized void disconnect() {
        try {
            if (mqttAndroidClient != null) {
                mqttAndroidClient.disconnect(); //断开连接
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void setConnectCallback(Callback<Boolean> connectCallback) {
        this.connectCallback = connectCallback;
    }


    public void setSubscribeCallback(SubscribeCallback<String> subscribeCallback) {
        this.subscribeCallback = subscribeCallback;
    }
}
