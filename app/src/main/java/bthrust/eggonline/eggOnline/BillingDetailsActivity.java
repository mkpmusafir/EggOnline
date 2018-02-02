package bthrust.eggonline.eggOnline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import bthrust.eggonline.Adapter.AddCartAdapter;
import bthrust.eggonline.Adapter.checkOutCartListAdapter;
import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;
import bthrust.eggonline.dataBase.sqLiteDB;

public class BillingDetailsActivity extends AppCompatActivity {
    List<MainBeen> cartList;
    private checkOutCartListAdapter adapter;
    String totalAmount;

    private EditText fName_Text , lName_Text , cName_Text , hName_sName_Text , aptetc_Text,twonCity_Text , stateCountry_Text,
                     postcodeZipe_Text , phone_Text , email_Text , country_Text;
    private String  fName_str , lName_str , cName_str , hName_sName_str , aptetc_str,twonCity_str , stateCountry_str,
                    postcodeZipe_str , phone_str , email_str , country_str;
    private double sumAll_cost;
    private double list_cost = 00.00;
    private double totalPrice;
    private TextView nextOrder_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewLayoutControl();
    }

    private void viewLayoutControl() {

        fName_Text = (EditText) findViewById(R.id.fName_Text);
        lName_Text = (EditText) findViewById(R.id.lName_Text);
        country_Text = (EditText) findViewById(R.id.country_Text);
        hName_sName_Text = (EditText) findViewById(R.id.hName_sName_Text);
        aptetc_Text = (EditText) findViewById(R.id.aptetc_Text);
        twonCity_Text = (EditText) findViewById(R.id.twonCity_Text);
        stateCountry_Text = (EditText) findViewById(R.id.stateCountry_Text);
        postcodeZipe_Text = (EditText) findViewById(R.id.postcodeZipe_Text);
        phone_Text = (EditText) findViewById(R.id.phone_Text);
        email_Text = (EditText) findViewById(R.id.email_Text);

        nextOrder_BTN = (TextView) findViewById(R.id.nextOrder_BTN);

        nextOrder_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fName_str = fName_Text.getText().toString();
                lName_str = lName_Text.getText().toString();
                country_str = country_Text.getText().toString();
                hName_sName_str = hName_sName_Text.getText().toString();
                aptetc_str = aptetc_Text.getText().toString();
                twonCity_str = twonCity_Text.getText().toString();
                stateCountry_str = stateCountry_Text.getText().toString();
                postcodeZipe_str = postcodeZipe_Text.getText().toString();
                phone_str = phone_Text.getText().toString();
                email_str = email_Text.getText().toString();

                if(fName_str.isEmpty() || fName_str.matches("")){
                    fName_Text.setError("Enter first Name.!");
                }else if(lName_str.isEmpty() || lName_str.matches("")){
                    lName_Text.setError("Enter last Name.!");
                }else if(country_str.isEmpty() || country_str.matches("")){
                    country_Text.setError("Enter Country.!");
                }else if(hName_sName_str.isEmpty() || hName_sName_str.matches("")){
                    hName_sName_Text.setError("Enter Street Address.!");
                }else if(twonCity_str.isEmpty() || twonCity_str.matches("")){
                    twonCity_Text.setError("Enter town/city");
                }else if(stateCountry_str.isEmpty() || stateCountry_str.matches("")){
                    stateCountry_Text.setError("Enter state/country");
                }else if(phone_str.isEmpty() || phone_str.matches("")){
                    phone_Text.setError("Enter phone no.");
                }else if(email_str.isEmpty() || email_str.matches("")){
                    email_Text.setError("Enter Email.");
                }else{

                    Intent intent = new Intent(BillingDetailsActivity.this , YourOrderActivity.class);
                    intent.putExtra("fName_str" , fName_str);
                    intent.putExtra("lName_str" , lName_str);
                    intent.putExtra("country_str" , country_str);
                    intent.putExtra("hName_sName_str" , hName_sName_str);
                    intent.putExtra("aptetc_str" , aptetc_str);
                    intent.putExtra("twonCity_str" , twonCity_str);
                    intent.putExtra("stateCountry_str" , stateCountry_str);
                    intent.putExtra("postcodeZipe_str" , postcodeZipe_str);
                    intent.putExtra("phone_str" , phone_str);
                    intent.putExtra("email_str" , email_str);
                    startActivity(intent);
                }


            }
        });
    }


}
