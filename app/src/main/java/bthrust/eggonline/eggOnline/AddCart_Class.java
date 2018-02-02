package bthrust.eggonline.eggOnline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import bthrust.eggonline.Adapter.AddCartAdapter;
import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;
import bthrust.eggonline.dataBase.sqLiteDB;


/**
 * Created by win-3 on 12/12/2017.
 */

public class AddCart_Class extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String arry[] = {"$120", "$99", "$499"};
    private Toolbar toolbar;
    private LinearLayout checkout_payment;
    private ArrayList<MainBeen> items;
    private AddCartAdapter adapter;
    double totalPrice = 00.00;

    List<MainBeen> cartList;
    private double sumAll_cost;
    private double list_cost = 00.00;
    private TextView subtotal_cast, checkOut_TotalAmount;
    private String productId , categoriesName;
    private int clickStatus=0;
    private LinearLayout noCart_section;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_cart_layout);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        if(bundle != null){
            productId = bundle.getString("id");
            categoriesName = bundle.getString("categoriesName");
            clickStatus = bundle.getInt("clickStatus");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //onBackPressed();

                if(clickStatus == 1){
                    Intent intent = new Intent(AddCart_Class.this , HomeActivity.class);
                    startActivity(intent);
                    AddCart_Class.this.finish();
                }else {

                    Intent intent = new Intent(AddCart_Class.this, Product_CategoryActivity.class);
                    intent.putExtra("id", productId);
                    intent.putExtra("categoriesName", categoriesName);
                    startActivity(intent);
                    AddCart_Class.this.finish();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        checkout_payment = (LinearLayout) findViewById(R.id.checkout_payment);
        subtotal_cast = (TextView) findViewById(R.id.subtotal_cast);
        checkOut_TotalAmount = (TextView) findViewById(R.id.checkOut_TotalAmount);
        noCart_section = (LinearLayout) findViewById(R.id.noCart_section);


                sqLiteDB db;
                db = new sqLiteDB(AddCart_Class.this);
                cartList = db.getAddCart();
                for (MainBeen mainBeen : cartList) {
                    list_cost = Double.parseDouble(mainBeen.getPrice());
                    Log.e("list_cost==> " , String.valueOf(list_cost));
                    sumAll_cost = list_cost + sumAll_cost;
                    homeListView();
                }
                NumberFormat formatter = new DecimalFormat(".##");
                totalPrice = Double.parseDouble(formatter.format(sumAll_cost));
                subtotal_cast.setText("$" + totalPrice);
                checkOut_TotalAmount.setText("$" + totalPrice);


        int cartSize =   cartList.size();
        if(cartSize == 0){
            noCart_section.setVisibility(View.VISIBLE);
        }else {
            noCart_section.setVisibility(View.GONE);
        }

        checkout_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              int cartSize =   cartList.size();
                if(cartSize == 0){
                    noCart_section.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Add Cart then you check out...!" , Toast.LENGTH_LONG).show();
                }else {
                    noCart_section.setVisibility(View.GONE);
                    Intent intent = new Intent(AddCart_Class.this, BillingDetailsActivity.class);
                    startActivity(intent);
                }
            }
        });

    }



    private void homeListView() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cartList_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AddCartAdapter(getApplicationContext(), cartList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof AddCartAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = items.get(viewHolder.getAdapterPosition()).getTotalPrice();

            // backup of removed item for undo purpose
            final MainBeen deletedItem = items.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());


        }
    }




    @Override
    public void onMethodCallback(double price) {
        double sumTotal_price = 00.00, listCost = 00.00;
        sqLiteDB db;
        db = new sqLiteDB(AddCart_Class.this);
        cartList = db.getAddCart();
        for (MainBeen mainBeen : cartList) {
            listCost = Double.parseDouble(mainBeen.getPrice());
            sumTotal_price = listCost + sumTotal_price;
            homeListView();
        }
        int size = cartList.size();
        if(size == 0){
            noCart_section.setVisibility(View.VISIBLE);
        }else{
            noCart_section.setVisibility(View.GONE);
        }

        NumberFormat formatter = new DecimalFormat(".##");
        totalPrice = Double.parseDouble(formatter.format(sumTotal_price));
        subtotal_cast.setText("$" + totalPrice);
        checkOut_TotalAmount.setText("$" + totalPrice);
    }
}
