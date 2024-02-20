package com.wenh.baselibrary.mqtt;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wenh.baselibrary.callback.Callback;

import org.apache.log4j.Logger;
import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class MqttHelper implements SubscribeCallback<String>, Callback<Boolean> {
    private static final String TAG = "MqttHelper";
    private MqttClient mqttClient;
    private Logger logger = Logger.getLogger(MqttHelper.class);
    private JsonParser parser = new JsonParser();
    private Gson gson = new Gson();
    private Callback<Boolean> connectCallback;
    private Map<String, Class> classMap = new HashMap<>();

    private MqttHelper() {
    }

    public void setConnectCallback(Callback<Boolean> connectCallback) {
        this.connectCallback = connectCallback;
    }

    @Override
    public void onCallback(Boolean result) {
        logger.info("mqtt连接状态：" + result);
        if (connectCallback != null) {
            connectCallback.onCallback(result);
        }
    }

    @Override
    public void onSubscribe(String name, String content) {
        JsonObject messageObject = parser.parse(content).getAsJsonObject();
        try {
            String json = messageObject.getAsJsonObject("args").toString();
            Class className = classMap.get(name);
            if (className == null) return;
            logger.info("消息推送：" + className.getName() + json);
            Object object = gson.fromJson(json, className);
            EventBus.getDefault().post(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final class InstanceHolder {
        static final MqttHelper instance = new MqttHelper();
    }

    public static MqttHelper getInstance() {
        return InstanceHolder.instance;
    }

    public void connect(Context context, String clientId, String username, String serverIp, String tcpPort) {
        if (mqttClient == null) {
            mqttClient = new MqttClient(context, clientId, username, serverIp, tcpPort);
            mqttClient.setSubscribeCallback(this);
            mqttClient.setConnectCallback(this);
        }
        if (!mqttClient.isConnected()) {
            mqttClient.setAutoReConnect(true);
            mqttClient.connect();
        }
    }

    public void subscribe(String topic, Class className) {
        if (mqttClient != null && mqttClient.isConnected()) {
            mqttClient.subscribe(topic);
            classMap.put(topic, className);
        }
    }
}
