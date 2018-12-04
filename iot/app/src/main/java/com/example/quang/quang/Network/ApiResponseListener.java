package com.example.quang.quang.Network;

/**
 * Created by Le Trong Nhan on 08/06/2018.
 */

public interface ApiResponseListener<T> {

    void onSuccess(T response);

    void onError(String error);
}
