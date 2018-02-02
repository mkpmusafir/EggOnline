package bthrust.eggonline.eggOnline;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

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
import bthrust.eggonline.Adapter.MyOrderItem_ListAdapter;
import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.Config.ConfigClass;
import bthrust.eggonline.R;

public class MyOrder_ItemsList_Activity extends AppCompatActivity {

    private MyOrderItem_ListAdapter adapter;
    private RecyclerView recyclerView;
    private List<MainBeen> items;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order__items_list_);
       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.myOrderView_recyclerView);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            Log.e("id=====>> " , id);
        }
        items = new ArrayList<MainBeen>();
        loadJsonData(id);
    }

    private void loadJsonData(final String id) {
        final ProgressDialog progressDialog = new ProgressDialog(MyOrder_ItemsList_Activity.this);
        progressDialog.setMessage("Please wait...!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigClass.MYORDERVIEW_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray1 = jsonObject1.getJSONArray("line_items");

                        for (int j = 0; j < jsonArray1.length(); j++) {

                            JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                            MainBeen mainBeen = new MainBeen();
                            Log.e("Name =====> ", jsonObject2.getString("name"));
                            mainBeen.setProductName(jsonObject2.getString("name"));
                            mainBeen.setMy_OrderTotalAmount(jsonObject2.getString("subtotal"));
                            mainBeen.setMy_OrderTotalCart(jsonObject2.getString("quantity"));
                            items.add(mainBeen);
                        }
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new MyOrderItem_ListAdapter(getApplicationContext(), items);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            public  Map<String , String> getParams() throws RemoteViews.ActionException{
                Map<String  , String> params = new HashMap<String , String>();
                params.put("order_id" , id);

                return params;
            }

        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 40000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 40000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

                Toast.makeText(getApplicationContext(), " Slow Network.!", Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MyOrder_ItemsList_Activity.this);
        requestQueue.add(stringRequest);
    }


}
