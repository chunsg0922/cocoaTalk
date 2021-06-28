package com.samil.cocoatalk;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = "http://chunsg0922.cafe24.com/Register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String memberID, String memberPassword, String memberName, String memberPhone, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("onErrorResponse", error.getMessage());
            }
        });
        parameters = new HashMap<>();
        parameters.put("memberID", memberID);
        parameters.put("memberPassword", memberPassword);
        parameters.put("memberName", memberName);
        parameters.put("memberPhone", memberPhone);

    }

    @Override
    public Map<String, String> getParams() {return parameters;}
}
