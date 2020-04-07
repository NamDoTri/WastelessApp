package com.wasteless.ui.settings.bankAccount;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BankAccountViewModel extends AndroidViewModel {
    private BankAccountFragment bankAccountFragment;
    public BankAccountViewModel(@NonNull Application application) {
        super(application);
    }
//    private BankAccountFragment bankAccountFragment = new BankAccountFragment();
    public void requestWithSomeHttpHeaders() {
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        String url = "https://sandbox.apis.op-palvelut.fi/accounts/v3/accounts";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray value = response.getJSONArray("accounts");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
                Log.i("response", error.toString());

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer 6c18c234b1b18b1d97c7043e2e41135c293d0da9");
                params.put("x-api-key", "GbwA2cD6xa86SYJ8qOvHwT1bgheFYhs3");
                params.put("accept", "application/hal+json");
                return params;
            }
        };
        queue.add(jsonObjectRequest);

    }
}
