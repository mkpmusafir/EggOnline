package bthrust.eggonline.eggOnline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bthrust.eggonline.Adapter.checkOutCartListAdapter;
import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;
import bthrust.eggonline.dataBase.sqLiteDB;

public class OrderPlaceSuccessActivity extends AppCompatActivity {

    private String firstname_billing, lastname_billing, addressOne_billing, city_billiing, state_billing, postcode_billing, country_billing, email_billing, phone_billing;
    private String firstname_shipping, lastname_shipping, addressOne_shipping, city_shipping, state_shipping, postcode_shipping, country_shipping, email_shipping, phone_shipping;

    private TextView fullNamebilling, addressbilling, emailbilling, phonebilling;
    private TextView fullnameshipping, addressshipping, emailshiping, phoneshipping , your_Order_Text;
    private List<MainBeen> items;
    private checkOutCartListAdapter adapter;
    private LinearLayout order_section , bellingSection;
    private boolean status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place_success);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sqLiteDB db = new sqLiteDB(OrderPlaceSuccessActivity.this);
                db.deleteCartTable();

                Intent intent = new Intent(OrderPlaceSuccessActivity.this, HomeActivity.class);
                startActivity(intent);
                OrderPlaceSuccessActivity.this.finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fullNamebilling = (TextView) findViewById(R.id.fullName_billing);
        addressbilling = (TextView) findViewById(R.id.address_billing);
        emailbilling = (TextView) findViewById(R.id.email_billing);
        phonebilling = (TextView) findViewById(R.id.phone_billing);

        fullnameshipping = (TextView) findViewById(R.id.fullname_shipping);
        addressshipping = (TextView) findViewById(R.id.address_shipping);

        your_Order_Text = (TextView) findViewById(R.id.your_Order_Text);
        order_section = (LinearLayout) findViewById(R.id.order_section);
        bellingSection = (LinearLayout) findViewById(R.id.bellingSection);

        your_Order_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(status == true){
                    order_section.setVisibility(View.VISIBLE);
                    bellingSection.setVisibility(View.GONE);
                    status = false;
                }else{
                    order_section.setVisibility(View.GONE);
                    bellingSection.setVisibility(View.VISIBLE);
                    status = true;
                }

            }
        });


        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            firstname_billing = bundle.getString("firstname_billing");
            lastname_billing = bundle.getString("lastname_billing");
            addressOne_billing = bundle.getString("addressOne_billing");
            city_billiing = bundle.getString("city_billiing");
            state_billing = bundle.getString("state_billing");
            postcode_billing = bundle.getString("postcode_billing");
            country_billing = bundle.getString("country_billing");
            email_billing = bundle.getString("email_billing");
            phone_billing = bundle.getString("phone_billing");

            firstname_shipping = bundle.getString("firstname_shipping");
            lastname_shipping = bundle.getString("lastname_shipping");
            addressOne_shipping = bundle.getString("addressOne_shipping");
            city_shipping = bundle.getString("city_shipping");
            state_shipping = bundle.getString("state_shipping");
            postcode_shipping = bundle.getString("postcode_shipping");
            country_shipping = bundle.getString("country_shipping");



        }

        fullNamebilling.setText(firstname_billing + "  " + lastname_billing);
        addressbilling.setText(addressOne_billing + ", " + city_billiing + " " + state_billing + ", " + country_billing);
        emailbilling.setText(email_billing);
        phonebilling.setText(phone_billing);

        fullnameshipping.setText(firstname_shipping + "  " + lastname_shipping);
        addressshipping.setText(addressOne_shipping + ", " + city_shipping + " " + state_billing + ", " + country_shipping);




        sqLiteDB db;

        int quantity;
        db = new sqLiteDB(OrderPlaceSuccessActivity.this);
        items = db.getAddCart();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.orderplace_recyclerview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new checkOutCartListAdapter(getApplicationContext(), items);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sqLiteDB db = new sqLiteDB(OrderPlaceSuccessActivity.this);
        db.deleteCartTable();
    }
}
