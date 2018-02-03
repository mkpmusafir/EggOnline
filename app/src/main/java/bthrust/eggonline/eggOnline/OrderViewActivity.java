package bthrust.eggonline.eggOnline;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bthrust.eggonline.Adapter.CategoryAdapter;
import bthrust.eggonline.Adapter.MyAdapter;
import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.Config.ConfigClass;
import bthrust.eggonline.R;
import bthrust.eggonline.dataBase.sqLiteDB;
import me.relex.circleindicator.CircleIndicator;


public class OrderViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private boolean status = true;
    int cartNumber;
    private String productName;
    private String image;
    private String price;
    private String description;
    private List<MainBeen> items;
    CategoryAdapter adapter;
    private String productId;
    ImageView imageTest;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.egg1, R.drawable.egg_logo, R.drawable.newone};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    private List<MainBeen> imageList;
    private String categoryID , categoriesName;
    private sqLiteDB db;
    private double total_price;
    private List<MainBeen> cartList;
    private int cart_No;
    private double actual_price;
    private String  mainCatID;
    TextView orderList_cout;
    private ImageView cartImage;
    private boolean cartStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);



        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            productName = bundle.getString("productName");
            image = bundle.getString("image");
            price = bundle.getString("price");
            description = bundle.getString("description");
            productId = bundle.getString("productId");
            categoriesName = bundle.getString("categoriesName");
            categoryID = bundle.getString("categoryID");
        }


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        orderList_cout = (TextView) toolbar.findViewById(R.id.orderList_cout);
        cartImage = (ImageView) toolbar.findViewById(R.id.cartImage);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();

                Intent intent = new Intent(OrderViewActivity.this , Product_CategoryActivity.class);
                intent.putExtra("id" , productId);
                intent.putExtra("categoriesName" , categoriesName);
                startActivity(intent);
                OrderViewActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



            sqLiteDB db = new sqLiteDB(OrderViewActivity.this);
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
                Intent intent = new Intent(OrderViewActivity.this, AddCart_Class.class);
                startActivity(intent);
                OrderViewActivity.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });


        for (MainBeen mainBeen : cartList) {

            String catId = mainBeen.getCategoryID();
           actual_price = Double.parseDouble(mainBeen.getPrice());
            if(catId.equals(categoryID)){
                cart_No = Integer.parseInt(mainBeen.getCartNumber());
                mainCatID = mainBeen.getCategoryID();
            }

        }

        if(mainCatID == null){
            mainCatID = categoryID;
        }


        LoadSameCategoryData();
        controlView();

    }

    private void controlView() {
        final TextView addCart_text = (TextView) findViewById(R.id.addCart_text);
        final ImageView less_cart = (ImageView) findViewById(R.id.less_cart);
        final ImageView add_Cart = (ImageView) findViewById(R.id.add_Cart);
        final LinearLayout addMoreCart_Text = (LinearLayout) findViewById(R.id.addMoreCart_Text);
        final TextView viewPrice_text = (TextView) findViewById(R.id.viewPrice_text);
        final TextView product_NameText = (TextView) findViewById(R.id.product_NameText);
        final TextView add_CardNumberView = (TextView) findViewById(R.id.add_CardNumberView);
       // final TextView savetolist_text = (TextView) findViewById(R.id.savetolist_text);
        final TextView loadDescription = (TextView) findViewById(R.id.loadDescription);


        // Load View Data ===========================

        viewPrice_text.setText("$" + price);
        product_NameText.setText(productName);
        loadDescription.setText(""+Html.fromHtml(description));

        cartNumber = Integer.parseInt(add_CardNumberView.getText().toString());

        //  priceText.setPaintFlags(priceText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if(cart_No != 0){

            add_CardNumberView.setText(""+cart_No);
            addCart_text.setVisibility(View.GONE);
            addMoreCart_Text.setVisibility(View.VISIBLE);
        }


        addCart_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                addCart_text.setVisibility(View.GONE);
                addMoreCart_Text.setVisibility(View.VISIBLE);

               sqLiteDB db1 = new sqLiteDB(OrderViewActivity.this);
                MainBeen mainBeen = new MainBeen();
                mainBeen.setCartNumber(String.valueOf(cartNumber));
                mainBeen.setProductName(productName);
                mainBeen.setPrice(price);
                mainBeen.setImage(image);
                mainBeen.setCategoryID(categoryID);
                mainBeen.setActual_price(price);
                db1.insertCartData(mainBeen);

                sqLiteDB db = new sqLiteDB(OrderViewActivity.this);
                cartList = db.getAddCart();

                String cartlistcout = String.valueOf(cartList.size());

                if(cartlistcout.equals("0")){
                    orderList_cout.setVisibility(View.GONE);
                }else{
                    orderList_cout.setVisibility(View.VISIBLE);
                    orderList_cout.setText(cartlistcout);
                }

            }
        });


        add_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sqLiteDB db1 = new sqLiteDB(OrderViewActivity.this);
                double total_price = 00.00;
                String sumprice = null;

                double ctPrice = 00.00;
                List<MainBeen> cartList = db1.getAddCart();
                for (MainBeen mBeen : cartList) {
                    String ctID = mBeen.getCategoryID();

                if(mainCatID.equals(ctID)) {
                    ctPrice = Double.parseDouble(mBeen.getPrice());
                    cartNumber = Integer.parseInt(mBeen.getCartNumber());
                }
            }


            if (cartNumber >= 1) {

                    cartNumber++;
                    add_CardNumberView.setText("" + cartNumber);
                }

                double oneCartPrice = Double.parseDouble(price);
                total_price = ctPrice + oneCartPrice;
                sumprice = String.valueOf(total_price);

                String cnumbrt = String.valueOf(cartNumber);
                db1.updatecartTable(mainCatID, sumprice, cnumbrt);


            }
        });


        less_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sqLiteDB db1 = new sqLiteDB(OrderViewActivity.this);
                double total_price = 00.00;
                String sumprice = null;

                double ctPrice = 00.00;
                String ctID = null;
                List<MainBeen> cartList = db1.getAddCart();
                for (MainBeen mBeen : cartList) {
                    ctID = mBeen.getCategoryID();
                    if(mainCatID.equals(ctID)) {
                        ctPrice = Double.parseDouble(mBeen.getPrice());
                        cartNumber = Integer.parseInt(mBeen.getCartNumber());
                    }
                }


                if (cartNumber > 1) {
                    cartNumber--;
                    add_CardNumberView.setText("" + cartNumber);
                }else{
                    db1.carDelete(ctID);
                    addMoreCart_Text.setVisibility(View.GONE);
                   addCart_text.setVisibility(View.VISIBLE);
                    orderList_cout.setVisibility(View.GONE);
                }

                double oneCartPrice = Double.parseDouble(price);
                total_price = ctPrice - oneCartPrice;
                sumprice = String.valueOf(total_price);
                String cnumbrt = String.valueOf(cartNumber);
                db1.updatecartTable(mainCatID, sumprice, cnumbrt);
            }
        });


      /*  savetolist_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db = new sqLiteDB(OrderViewActivity.this);
                MainBeen mainBeen = new MainBeen();
                mainBeen.setCartNumber(String.valueOf(cartNumber));
                mainBeen.setProductName(productName);
                mainBeen.setPrice(String.valueOf(total_price));
                mainBeen.setImage(image);
                mainBeen.setCategoryID(categoryID);
                db.insertCartData(mainBeen);

                Intent intent = new Intent(OrderViewActivity.this, AddCart_Class.class);
                startActivity(intent);

            }
        });*/
    }

    private void LoadSameCategoryData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, ConfigClass.PRODUCT_CATEGORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {

                    items = new ArrayList<MainBeen>();
                    imageList = new ArrayList<MainBeen>();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        MainBeen mainBeen = new MainBeen();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        mainBeen.setCategoryID(jsonObject1.getString("id"));
                        mainBeen.setProductName(jsonObject1.getString("name"));
                        mainBeen.setPrice(jsonObject1.getString("price"));
                        mainBeen.setRegular_price(jsonObject1.getString("regular_price"));
                        mainBeen.setOn_sale(jsonObject1.getBoolean("on_sale"));
                        mainBeen.setDescription(jsonObject1.getString("description"));
                        mainBeen.setActual_price(jsonObject1.getString("price"));


                        JSONArray jsonArray1 = jsonObject1.getJSONArray("images");
                        for (int j = 0; j < 1; j++) {
                            JSONObject jsonObject = jsonArray1.getJSONObject(j);
                            mainBeen.setImage(jsonObject.getString("src"));
                        }
                        if (categoryID.equals(jsonObject1.getString("id"))) {
                            for (int k = 0; k < jsonArray1.length(); k++) {
                                JSONObject jsonObject = jsonArray1.getJSONObject(k);
                                MainBeen mainBeen1 = new MainBeen();
                                mainBeen1.setViewImage(jsonObject.getString("src"));
                                imageList.add(mainBeen1);
                            }
                        }


                        items.add(mainBeen);

                    }
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.sameBrand_list);
                    recyclerView.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new CategoryAdapter(getApplicationContext(), items, productId  , categoriesName, cartStatus);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    init();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
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
                Toast.makeText(OrderViewActivity.this,"Slow internet connection..!",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(OrderViewActivity.this);
        requestQueue.add(stringRequest);

    }


    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        MyAdapter myAdapter = new MyAdapter(OrderViewActivity.this, imageList);
        mPager.setAdapter(myAdapter);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        myAdapter.notifyDataSetChanged();


        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == imageList.size()) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_view_main, menu);
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
        db = new sqLiteDB(OrderViewActivity.this);
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
