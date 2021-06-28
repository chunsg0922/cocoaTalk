package com.samil.cocoatalk;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final static private String URL = "http://chunsg0922.cafe24.com/Login.php";
    private Map<String, String> parameters;

    public LoginRequest(String memberID, String memberPassword, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("memberID", memberID);
        parameters.put("memberPassword", memberPassword);
    }

    @Override
    public Map<String, String> getParams() {return parameters;}
}
