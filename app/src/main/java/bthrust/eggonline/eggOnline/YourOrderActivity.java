package bthrust.eggonline.eggOnline;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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

import bthrust.eggonline.Adapter.checkOutCartListAdapter;
import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.Config.ConfigClass;
import bthrust.eggonline.R;
import bthrust.eggonline.ServerInteractor;
import bthrust.eggonline.dataBase.sqLiteDB;

public class YourOrderActivity extends AppCompatActivity {
    private double sumAll_cost;
    private double list_cost = 00.00;
    private double totalPrice;
    List<MainBeen> cartList;
    private checkOutCartListAdapter adapter;
    private TextView totalAmountText, subtotalAmountText, paymentMode_Text;
    private String fName_str, lName_str, cName_str, hName_sName_str, aptetc_str, twonCity_str, stateCountry_str,
            postcodeZipe_str, phone_str, email_str, country_str;
    private RadioGroup paymentMode_GRP;
    private TextView placeOrder_BTN;
    private String quantity;
    JSONObject jsonObject = new JSONObject();
    JSONArray jsonArray = new JSONArray();
    List<MainBeen> postitems = new ArrayList<MainBeen>();
    private int categoryID;
    private String payment_id, payment_title, set_paid = "";
    private boolean paymentStatus = false;
    private String arrayData;
    private String actualPrice;
    JSONArray array;
    List<MainBeen> viewOrderList;
    private String  userID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order);
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
        userID = sharepref.getString("userID", "");
        if(userID.equals("")){
            userID = "0";
        }


        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            fName_str = bundle.getString("fName_str");
            lName_str = bundle.getString("lName_str");
            country_str = bundle.getString("country_str");
            hName_sName_str = bundle.getString("hName_sName_str");
            aptetc_str = bundle.getString("aptetc_str");
            twonCity_str = bundle.getString("twonCity_str");
            stateCountry_str = bundle.getString("stateCountry_str");
            postcodeZipe_str = bundle.getString("postcodeZipe_str");
            phone_str = bundle.getString("phone_str");
            email_str = bundle.getString("email_str");
        }

        sqLiteDB db;

        int quantity;
        db = new sqLiteDB(YourOrderActivity.this);
        cartList = db.getAddCart();
        for (MainBeen mainBeen : cartList) {
            list_cost = Double.parseDouble(mainBeen.getPrice());
            sumAll_cost = list_cost + sumAll_cost;
            quantity = Integer.parseInt(mainBeen.getCartNumber());
            categoryID = Integer.parseInt(mainBeen.getCategoryID()); // category ID mns ProductID=============
            MainBeen mainBeen1 = new MainBeen();
            mainBeen1.setPostCartNo(quantity);
            mainBeen1.setPostProductID(categoryID);
            postitems.add(mainBeen1);
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        NumberFormat formatter = new DecimalFormat(".##");
        totalPrice = Double.parseDouble(formatter.format(sumAll_cost));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.addcart_list_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new checkOutCartListAdapter(getApplicationContext(), cartList);
        recyclerView.setAdapter(adapter);


        totalAmountText = (TextView) findViewById(R.id.totalAmount);
        subtotalAmountText = (TextView) findViewById(R.id.subtotalAmount);
        paymentMode_Text = (TextView) findViewById(R.id.paymentMode_Text);
        placeOrder_BTN = (TextView) findViewById(R.id.placeOrder_BTN);
        paymentMode_GRP = (RadioGroup) findViewById(R.id.paymentMode_GRP);

        subtotalAmountText.setText("$" + totalPrice);
        totalAmountText.setText("$" + totalPrice);
        actualPrice = String.valueOf(totalPrice);

        paymentMode_GRP.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedId);

                String mode = "Check Payment";
                if (null != radioButton && checkedId > -1) {

                    if (mode.equals(radioButton.getText())) {
                        paymentMode_Text.setVisibility(View.VISIBLE);
                        paymentMode_Text.setText("Please send a check to Store Name, Store Street, Store Town, Store State / County, Store Postcode.");
                        payment_id = "cheque";
                        payment_title = "Cheque payments";
                        paymentStatus = true;

                    } else {
                        paymentMode_Text.setVisibility(View.VISIBLE);
                        paymentMode_Text.setText("Pay with cash upon delivery.");
                        payment_id = "cod";
                        payment_title = "Cash on delivery";
                        paymentStatus = true;

                    }

                }

            }
        });

        placeOrder_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paymentStatus == true) {
                    array = new JSONArray();
                    MainBeen mainBeen;
                    for (int i = 0; i < postitems.size(); i++) {
                        JSONObject obj = new JSONObject();
                        mainBeen = postitems.get(i);
                        try {
                            obj.put("product_id", mainBeen.getPostProductID());
                            obj.put("quantity", mainBeen.getPostCartNo());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        array.put(obj);
                        arrayData = String.valueOf(array);
                    }

                   new loaddata().execute();

                } else {

                    Toast.makeText(getApplicationContext(), "Choose payment Type..!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }


    public class loaddata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(YourOrderActivity.this);
            progressDialog.setMessage("Please Wait...!");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }

        @Override
        protected String doInBackground(String... strings) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("payment_id", payment_id);
                jsonObject.accumulate("user_id", userID);
                jsonObject.accumulate("payment_title", payment_title);
                jsonObject.accumulate("set_paid", set_paid);
                jsonObject.accumulate("first_name", fName_str);
                jsonObject.accumulate("last_name", lName_str);
                jsonObject.accumulate("address_1", hName_sName_str);
                jsonObject.accumulate("address_2", aptetc_str);
                jsonObject.accumulate("city", twonCity_str);
                jsonObject.accumulate("state", stateCountry_str);
                jsonObject.accumulate("postcode", postcodeZipe_str);
                jsonObject.accumulate("country", country_str);
                jsonObject.accumulate("phone", phone_str);
                jsonObject.accumulate("email", email_str);
                jsonObject.accumulate("line_items", array);
                jsonObject.accumulate("total_price", actualPrice);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ServerInteractor.httpServerPost(jsonObject.toString(), ConfigClass.PAYMOD_CHECK);

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progressDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONObject jsonObject1 = jsonObject.getJSONObject("billing");
                String firstname_billing = jsonObject1.getString("first_name");
                String lastname_billing = jsonObject1.getString("last_name");
                String addressOne_billing = jsonObject1.getString("address_1");
                String city_billiing = jsonObject1.getString("city");
                String state_billing = jsonObject1.getString("state");
                String postcode_billing = jsonObject1.getString("postcode");
                String country_billing = jsonObject1.getString("country");
                String email_billing = jsonObject1.getString("email");
                String phone_billing = jsonObject1.getString("phone");

                JSONObject jsonObject2 = jsonObject.getJSONObject("shipping");

                String firstname_shipping = jsonObject2.getString("first_name");
                String lastname_shipping = jsonObject2.getString("last_name");
                String addressOne_shipping = jsonObject2.getString("address_1");
                String city_shipping = jsonObject2.getString("city");
                String state_shipping = jsonObject2.getString("state");
                String postcode_shipping = jsonObject2.getString("postcode");
                String country_shipping = jsonObject2.getString("country");

                viewOrderList = new ArrayList<MainBeen>();
                JSONArray jsonArray = jsonObject.getJSONArray("line_items");
                for(int i = 0; i< jsonArray.length(); i++){
                    MainBeen mainBeen = new MainBeen();
                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                    mainBeen.setProductName(jsonObject3.getString("name"));
                    mainBeen.setCartNumber(jsonObject3.getString("quantity"));
                    mainBeen.setTotalPrice(jsonObject3.getString("total"));
                    viewOrderList.add(mainBeen);
                }


                Intent intent = new Intent(YourOrderActivity.this , OrderPlaceSuccessActivity.class);
                intent.putExtra("firstname_billing" , firstname_billing);
                intent.putExtra("lastname_billing" , lastname_billing);
                intent.putExtra("addressOne_billing" , addressOne_billing);
                intent.putExtra("city_billiing" , city_billiing);
                intent.putExtra("state_billing" , state_billing);
                intent.putExtra("postcode_billing" , postcode_billing);
                intent.putExtra("country_billing" , country_billing);
                intent.putExtra("email_billing" , email_billing);
                intent.putExtra("phone_billing" , phone_billing);
                intent.putExtra("firstname_shipping" , firstname_shipping);
                intent.putExtra("lastname_shipping" , lastname_shipping);
                intent.putExtra("addressOne_shipping" , addressOne_shipping);
                intent.putExtra("city_shipping" , city_shipping);
                intent.putExtra("state_shipping" , state_shipping);
                intent.putExtra("postcode_shipping" , postcode_shipping);
                intent.putExtra("country_shipping" , country_shipping);
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
