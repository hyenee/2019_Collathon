package com.collathon.jamukja;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginCustomerRequest {
    /*
    String site = NetworkManager.url + "/user/login?id=1&passwd=1111";

    URL url = new URL(site);
    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
    private Map<String, String> parameters;

    public LoginCustomerRequest(String id, String passwd, Response.Listener<String> listner){
        super(Method.POST, URL, listner, null);

        //id, passwd 파라미터로 가져와 서버로 가져감
        parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("passwd", passwd);
        Log.d("들어옴", "LoginRequest" +id+passwd);
    }

    public Map<String, String> getParams(){
        Log.d("들어옴2", "");
        return parameters;
    }

     */
}
