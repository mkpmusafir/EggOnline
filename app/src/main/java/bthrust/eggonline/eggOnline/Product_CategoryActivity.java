package bthrust.eggonline.eggOnline;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


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

import bthrust.eggonline.Adapter.CategoryAdapter;
import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.Config.ConfigClass;
import bthrust.eggonline.R;
import bthrust.eggonline.dataBase.sqLiteDB;


public class Product_CategoryActivity extends AppCompatActivity{

   // private final String[] all_arry = {"Default Sorting","Sort by popularity","Sort by average rating","Sort by newness","Sort by price: low to high" , "Sort by price: high to low"};
    private Toolbar toolbar;
    private ArrayList<MainBeen> items;
    private CategoryAdapter adapter;
    private String productId , categoriesName;
    TextView orderList_cout , categoriesName_text;
    List<MainBeen> cartList;
    private ImageView cartImage;
    private boolean mIsBackVisible = false;
    private boolean cartAddStatus = true;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if(bundle != null){
            productId = bundle.getString("id");
            categoriesName = bundle.getString("categoriesName");
        }

        Log.e("ffff==> ",categoriesName+"  ==  "+productId);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        orderList_cout = (TextView) toolbar.findViewById(R.id.orderList_cout);
        categoriesName_text = (TextView) toolbar.findViewById(R.id.categoriesName_text);
        cartImage = (ImageView) toolbar.findViewById(R.id.cartImage);
        categoriesName_text.setText(categoriesName);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Product_CategoryActivity.this , HomeActivity.class);
                startActivity(intent);
                Product_CategoryActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            sqLiteDB db;
            db = new sqLiteDB(Product_CategoryActivity.this);
            cartList = db.getAddCart();
            String cartlistcout = String.valueOf(cartList.size());


            if (cartlistcout.equals("0")) {
                orderList_cout.setVisibility(View.GONE);
            } else {
                orderList_cout.setVisibility(View.VISIBLE);
                orderList_cout.setText(cartlistcout);

            }



        cartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Product_CategoryActivity.this, AddCart_Class.class);
                intent.putExtra("id" , productId);
                intent.putExtra("categoriesName" , categoriesName);
                startActivity(intent);
                Product_CategoryActivity.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });




        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoadCatgoryJsonData();
                        homeListView();
                    }
                }, 3000);
            }
        });

        LoadCatgoryJsonData();
        homeListView();
    }



    private void homeListView() {
      /*  Spinner filterList = (Spinner) findViewById(R.id.filterList);
        ArrayAdapter filter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,all_arry);
        filter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterList.setAdapter(filter);*/

        items = new ArrayList<MainBeen>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.product_categoryList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CategoryAdapter(getApplicationContext() , items , productId  , categoriesName, cartAddStatus);
        recyclerView.setAdapter(adapter);
    }




    private void LoadCatgoryJsonData() {
        final ProgressDialog progressDialog = new ProgressDialog(Product_CategoryActivity.this);
        progressDialog.setMessage("Please wait...!");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Log.e("volley request ===> " , "re===========");
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigClass.PRODUCT_CATEGORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                Log.e("volley request ===> " , "re====11======="+response);

                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length();i++){
                        MainBeen mainBeen = new MainBeen();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        mainBeen.setCategoryID(jsonObject1.getString("id"));
                        mainBeen.setProductName(jsonObject1.getString("name"));
                        mainBeen.setPrice(jsonObject1.getString("price"));
                        mainBeen.setActual_price(jsonObject1.getString("price"));
                        mainBeen.setRegular_price(jsonObject1.getString("regular_price"));
                        mainBeen.setOn_sale(jsonObject1.getBoolean("on_sale"));
                        mainBeen.setDescription(jsonObject1.getString("description"));

                        JSONArray jsonArray1 =  jsonObject1.getJSONArray("images");
                        int x = 0;
                        for(int j = 0; j < 1; j++){
                            JSONObject jsonObject = jsonArray1.getJSONObject(j);
                            mainBeen.setImage(jsonObject.getString("src"));
                        }


                        items.add(mainBeen);

                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();

                params.put("category_id", productId);
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
                Toast.makeText(Product_CategoryActivity.this,"Slow internet connection..!",Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Product_CategoryActivity.this);
        requestQueue.add(stringRequest);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_search:
                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
                MenuItemCompat.setActionView(item, searchView);

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {


                        ArrayList<MainBeen> newArrayList= new ArrayList<>();
                        for(MainBeen orderBeen: items){
                            String name= orderBeen.getProductName().toLowerCase();
                            Log.e("asasaa=======:-   ",name);
                            if(name.contains(newText))
                                newArrayList.add(orderBeen);
                        }

                        adapter.setFilter(newArrayList);

                        return false;
                    }
                });

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onCartCallback(String ct_id) {

        sqLiteDB db;
        db = new sqLiteDB(Product_CategoryActivity.this);
        List<MainBeen>  cartList = db.getAddCart();
        String cartlistcout = String.valueOf(cartList.size());


        if(cartlistcout.equals("0")){
            orderList_cout.setVisibility(View.GONE);
        }else{
            orderList_cout.setVisibility(View.VISIBLE);
            orderList_cout.setText(cartlistcout);


        }
    }



}
