package com.wenh.baselibrary.network;

import com.wenh.baselibrary.bean.DeviceBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Apis {
    @POST("/api/device/login")
    Call<HttpResponse<DeviceBean>> longin(@Query("mac") String mac);

}
