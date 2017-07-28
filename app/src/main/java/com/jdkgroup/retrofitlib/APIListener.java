package com.jdkgroup.retrofitlib;

import retrofit2.Response;

public interface APIListener
{
     void onSuccess(int from, Response response, Object res);
     void onFailure(int from, Throwable t);
     void onNetworkFailure(int from);
}
