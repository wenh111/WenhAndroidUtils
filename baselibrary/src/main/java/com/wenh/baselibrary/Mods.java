package com.wenh.baselibrary;

import com.wenh.baselibrary.mqtt.MqttHelper;
import com.wenh.baselibrary.network.Apis;
import com.wenh.baselibrary.network.HttpModule;
import com.wenh.baselibrary.prefs.Prefers;

/**
 * @Author Wenh·Wong
 * @Date 2024/2/27 9:00
 */
public class Mods {

    private static Apis apis;
    private static HttpModule httpModule;
    private static MqttHelper mqttModel;
    public static Apis apis() {
        if (apis == null) {
            httpModule = new HttpModule();
            apis = httpModule.provideService();
        }
        return apis;
    }

    public static HttpModule httpModule() {
        apis();
        return httpModule;
    }

    public static Prefers prefers() {
        return Prefers.ins();
    }

    public static MqttHelper mqttModel(){
        if(mqttModel == null){
            mqttModel = new MqttHelper();
        }
        return mqttModel;
    }



}
