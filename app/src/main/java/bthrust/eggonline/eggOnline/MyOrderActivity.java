package bthrust.eggonline.eggOnline;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bthrust.eggonline.Adapter.MyOrderAdapter;
import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.Config.ConfigClass;
import bthrust.eggonline.R;

public class MyOrderActivity extends AppCompatActivity {

    private List<MainBeen> items;
    private RecyclerView myOrder_recyclerView;
    private MyOrderAdapter myOrderAdapter;
    private String totalCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharepref = getSharedPreferences(ConfigClass.SHARE_PREF , MODE_PRIVATE);
       String  userID = sharepref.getString("userID", "");

        myOrder_recyclerView = (RecyclerView) findViewById(R.id.myOrder_recyclerView);

        items = new ArrayList<MainBeen>();
        loadMyOrder(userID);
    }

    private void loadMyOrder(final String userID) {

        final ProgressDialog progressDialog = new ProgressDialog(MyOrderActivity.this);
        progressDialog.setMessage("Please wait...!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigClass.MYORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for(int i = 0; i < jsonArray.length(); i++){

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        MainBeen mainBeen = new MainBeen();
                        mainBeen.setMy_OrderId(jsonObject1.getString("id"));
                        mainBeen.setMy_orderDate(jsonObject1.getString("date_created"));
                        mainBeen.setMy_OrderTotalAmount(jsonObject1.getString("total"));

                       JSONArray jsonArray1 = jsonObject1.getJSONArray("line_items");

                        totalCart = String.valueOf(jsonArray1.length());
                        mainBeen.setMy_OrderTotalCart(totalCart);
                        items.add(mainBeen);
                    }

                    myOrder_recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext() ,1);
                    myOrder_recyclerView.setLayoutManager(layoutManager);
                    myOrderAdapter = new MyOrderAdapter(getApplicationContext() , items);
                    myOrder_recyclerView.setAdapter(myOrderAdapter);
                    myOrderAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            public Map<String , String > getParams() throws AuthFailureError{
                Map<String  , String> params = new HashMap<String  , String>();

                params.put("user_id" ,userID);

                return params;
            }
        };
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MyOrderActivity.this);
        requestQueue.add(stringRequest);
    }


}
