package bthrust.eggonline.eggOnline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bthrust.eggonline.Adapter.drawerAdapter;
import bthrust.eggonline.Adapter.productAdapter;
import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.Config.ConfigClass;
import bthrust.eggonline.R;
import bthrust.eggonline.dataBase.sqLiteDB;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //private final String arry[] ={"Aenean Eu Tristique","Aenean Eu Tristique","Aenean Eu Tristique","Aenean Eu Tristique","Aenean Eu Tristique","Aenean Eu Tristique","Aenean Eu Tristique","Aenean Eu Tristique"};
    private DrawerLayout drawer;
    private productAdapter adapter;
    private drawerAdapter drawerAdapter;
    private ArrayList<MainBeen> items;
    private RecyclerView recyclerView;
    private TextView notifCount;
    static int mNotifCount = 10;
    TextView orderList_cout;
    List<MainBeen> cartList;
    private ImageView cartImage;
    private String userEmail , userName;
    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView drawer_RecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        orderList_cout = (TextView) toolbar.findViewById(R.id.orderList_cout);
        cartImage = (ImageView) toolbar.findViewById(R.id.cartImage);

        SharedPreferences sharePref = getSharedPreferences(ConfigClass.SHARE_PREF ,MODE_PRIVATE);
        userEmail = sharePref.getString("userEmail" ,"");
        userName = sharePref.getString("userName" , "");


         drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer_RecyclerView = (RecyclerView) navigationView.findViewById(R.id.drawer_recyclerView);
       // TextView user_Name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName_Text);

       /* if(!userEmail.equals("")) {
            user_Email.setText(userEmail);
            user_Name.setText(userName);

        }*/

        navigationView.setNavigationItemSelectedListener(this);


        sqLiteDB db = new sqLiteDB(HomeActivity.this);
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
                Intent intent = new Intent(HomeActivity.this, AddCart_Class.class);
                intent.putExtra("clickStatus",1);
                startActivity(intent);
                HomeActivity.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoadJsonData();
                        homeListView();
                    }
                }, 3000);
            }
        });

        LoadJsonData();
        homeListView();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    private void homeListView() {
        items = new ArrayList<MainBeen>();
        recyclerView = (RecyclerView) findViewById(R.id.main_recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new productAdapter(getApplicationContext() , items);
        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager1 = new GridLayoutManager(getApplicationContext(), 1);
        drawer_RecyclerView.setLayoutManager(layoutManager1);
        drawerAdapter = new drawerAdapter(getApplicationContext() , items);
        drawer_RecyclerView.setAdapter(drawerAdapter);


    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            // Handle the camera action
            Intent home = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(home);
            HomeActivity.this.finish();
        } else if (id == R.id.nav_account) {

            if(!userEmail.equals("")){
                Intent intent = new Intent(HomeActivity.this, UserProfile_Activity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(HomeActivity.this, Login.class);
                startActivity(intent);
            }

        }else if(id == R.id.nav_myorder){

            if(!userEmail.equals("")){
                Intent myorder = new Intent(getApplicationContext() , MyOrderActivity.class);
                startActivity(myorder);
            }else{
                Intent intent = new Intent(HomeActivity.this, Login.class);
                startActivity(intent);
            }



       }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        setTitle(item.getTitle());
        return true;
    }


    private void LoadJsonData() {

        final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Please Wait...!");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConfigClass.CATEGORY_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0; i < jsonArray.length(); i++){
                        MainBeen mainBeen = new MainBeen();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        mainBeen.setId(jsonObject1.getString("id"));
                        mainBeen.setProductCategoryName(jsonObject1.getString("name"));
                        Log.e("id==> " , jsonObject1.getString("id"));
                        Log.e("name==> " , jsonObject1.getString("name"));

                        JSONObject jsonObject2 = jsonObject1.getJSONObject("image");
                        mainBeen.setImage(jsonObject2.getString("src"));

                        items.add(mainBeen);
                    }
                    adapter.notifyDataSetChanged();
                    drawerAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
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
                Toast.makeText(HomeActivity.this,"Slow internet connection..!",Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(HomeActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
                           String name= orderBeen.getProductCategoryName().toLowerCase();
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



}
