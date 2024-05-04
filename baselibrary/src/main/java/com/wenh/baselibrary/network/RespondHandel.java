package com.wenh.baselibrary.network;

/**
 * @Author Wenh·Wong
 * @Date 2024/5/4 9:09
 */
public class RespondHandel {
    public static <T> T handelBody(HttpResponse<T> response) throws ApiException {
        if (response.getState() == 200 && response.getData() != null) {
            return response.getData();
        } else {
            throw new ApiException(response.getState(), response.getMessage());
        }
    }
}
