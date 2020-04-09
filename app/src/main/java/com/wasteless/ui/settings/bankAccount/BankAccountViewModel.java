package com.wasteless.ui.settings.bankAccount;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wasteless.repository.TransactionRepository;
import com.wasteless.repository.WalletRepository;
import com.wasteless.roomdb.entities.BankAccount;
import com.wasteless.roomdb.entities.Transaction;
import com.wasteless.roomdb.entities.Wallet;
import com.wasteless.ui.settings.SettingsViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BankAccountViewModel extends AndroidViewModel {

    private BankAccountFragment bankAccountFragment;
    private WalletRepository walletRepository;
    private TransactionRepository transactionRepository;
    private com.wasteless.ui.settings.SettingsViewModel SettingsViewModel;
    public void insertWallet(String name, double initialBalance, boolean isBank){
        walletRepository.insertWallet( new Wallet(name, initialBalance, isBank) );
    }
    public BankAccountViewModel(@NonNull Application application) {
        super(application);
        walletRepository = WalletRepository.getWalletRepository(application.getApplicationContext());
        transactionRepository = TransactionRepository.getTransactionRepository(application.getApplicationContext());
    }

    public long getWalletId(String name) {
        Wallet wallet = walletRepository.getWalletByName(name);
        return wallet.walletId;
    }

    public void requestToOP(TextView result) {
        if(walletRepository.getWalletByName("OP_bank") == null)
        insertWallet("OP_bank", 0.0, true);
        final Double[] balanceGlobal = {0.0};
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        String url = "https://sandbox.apis.op-palvelut.fi/accounts/v3/accounts";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("accounts");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject account = jsonArray.getJSONObject(i);
                        JSONObject _links = account.getJSONObject("_links");
                        JSONObject transactions = _links.getJSONObject("transactions");
                        String transactionLink = transactions.getString("href");
                        fetchTransactions(transactionLink);
                        String name = account.getString("name");
                        Double balance = account.getDouble("balance");
                        balanceGlobal[0] += balance;
                        Log.i("bankInfo", String.valueOf(balanceGlobal[0]));
                        result.append(name + " " +balance+"\n\n");
                    }
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

    private void fetchTransactions(String transactionLink) {
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        String url = transactionLink;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("transactions");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject transaction = jsonArray.getJSONObject(i);
                                String amountStringBank = transaction.getString("amount");
                                Double amount = Double.parseDouble(amountStringBank);
                                String dateBank = transaction.getString("valueDateTime");
                                String day = dateBank.substring(8, 10);
                                String month = dateBank.substring(5, 7);
                                String year = dateBank.substring(0, 4);
                                String date = day+"."+month+"."+year;
                                Long walletId = getWalletId("OP_bank");
                                String type = transaction.getString("creditDebitIndicator");
                                ArrayList<String> tags = null;
                                if (amount < 0){
                                    JSONObject creditor = transaction.getJSONObject("creditor");
                                    String description = creditor.getString("accountName");
                                    if(!transactionRepository.checkForTheSameTransaction(new Transaction(date, -amount, description, walletId, false, type))) try {
                                        transactionRepository.insertExpense( new Transaction(date, -amount, description, walletId, false, type), tags);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    String description = "OP bank's income";
                                    if(!transactionRepository.checkForTheSameTransaction(new Transaction(date, amount, description, walletId, true, "OP_bank"))) {try {
                                        transactionRepository.insertIncome( new Transaction(date, amount, description, walletId, true, "OP_bank"), tags);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }}
                                }
                                Log.i("bank", String.valueOf(walletId));

                            }
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
