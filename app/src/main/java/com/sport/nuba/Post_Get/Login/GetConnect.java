package com.sport.nuba.Post_Get.Login;

import org.json.JSONObject;

public class GetConnect {

    private ConnectListener connectListener;

    public void setListener(ConnectListener mConnectListener){
        connectListener = mConnectListener;
    }

    void connected(JSONObject responseJson){
        if(connectListener != null && responseJson != null){
            connectListener.isConnected(responseJson);
        }
    }
}
