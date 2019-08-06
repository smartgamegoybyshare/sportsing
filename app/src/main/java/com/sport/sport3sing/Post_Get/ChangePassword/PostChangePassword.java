package com.sport.sport3sing.Post_Get.ChangePassword;

import org.json.JSONObject;

public class PostChangePassword {

    private ChangePasswordListener changePasswordListener;

    public void setListener(ChangePasswordListener mChangePasswordListener) {
        changePasswordListener = mChangePasswordListener;
    }

    void setChange(JSONObject responseJson) {
        if (changePasswordListener != null && responseJson != null) {
            changePasswordListener.getChange(responseJson);
        }
    }
}
