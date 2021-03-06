package com.tonyodev.dispatchretrofit;

import okhttp3.Request;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Interface used by DispatchQueueCallAdapterFactory to relay errors.
 * */
@FunctionalInterface
public interface OnErrorCallback {

    /**
     * Called when an error occur when executing a request.
     * @param exception the error.
     * @param request the request.
     * @param response the response.
     * */
    void onError(HttpException exception, Request request, Response response);

}