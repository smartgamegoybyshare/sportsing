package com.sport.sport3sing.Post_Get.ChangePassword;

import android.content.Context;
import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword {

    private String TAG = "ChangePassword";
    private Context context;

    public ChangePassword(Context context) {
        this.context = context;
    }

    public void setConnect(String company, String account, String old_password, String new_password, PostChangePassword postChangePassword) {

        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());

        String url = "http://api-kz.zyue88.com/api/change_password";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        postChangePassword.setChange(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e(TAG, "VolleyError = " + error)) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("code", company);
                params.put("identity", account);
                params.put("old_password", old_password);
                params.put("new_password", new_password);
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(stringRequest);
    }
}