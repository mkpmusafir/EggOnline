package bthrust.eggonline.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;
import bthrust.eggonline.dataBase.sqLiteDB;
import bthrust.eggonline.eggOnline.AddCart_Class;
import bthrust.eggonline.eggOnline.OrderViewActivity;
import bthrust.eggonline.eggOnline.Product_CategoryActivity;


/**
 * Created by win-3 on 12/1/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<MainBeen> items;
    private MainBeen mainBeen;
    private String productId , categoriesName;
    private sqLiteDB db;
    AdapterCallback mAdapterCallback;
    boolean cartStatus = true;



    public CategoryAdapter(Context context, List<MainBeen> items, String productId ,String categoriesName, boolean cartStatus) {
        this.context = context;
        this.items = items;
        this.productId = productId;
        this.categoriesName = categoriesName;
        this.cartStatus = cartStatus;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_list_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {

        mainBeen = items.get(position);
        holder.productName_text.setText(items.get(position).getProductName());
        holder.price_text.setText("$" + items.get(position).getPrice());
        String category_Id = items.get(position).getCategoryID();

        boolean onSale = items.get(position).isOn_sale();
        if (onSale == true) {
            holder.regular_price_text.setPaintFlags(holder.regular_price_text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
        }

        try {
            sqLiteDB db = new sqLiteDB(context);
            List<MainBeen> cartList = db.getAddCart();
            for (MainBeen mBeen : cartList) {

                String ctID = mBeen.getCategoryID();
                String ctCartNo = mBeen.getCartNumber();

                if (ctID.equals(category_Id)) {
                    holder.addCart_text.setVisibility(View.GONE);
                    holder.addMoreCart_Text.setVisibility(View.VISIBLE);
                    holder.cart_list.setBackgroundResource(R.drawable.border_shape);
                    holder.add_CardNumber.setText(ctCartNo);
                }
            }

        }catch(Exception e){}

        Picasso.with(context).load(items.get(position).getImage()).into(holder.image);
        holder.itemView.setTag(mainBeen);
        holder.add_Cart.setTag(mainBeen);
        holder.addCart_text.setTag(position);
        holder.cart_list.setTag(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setFilter(ArrayList<MainBeen> newaddSearch) {

        items = new ArrayList<MainBeen>();
        items.addAll(newaddSearch);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView price_text, addCart_text, add_CardNumber, productName_text, regular_price_text;
        public ImageView less_cart, add_Cart, image;
        public LinearLayout addMoreCart_Text, cart_list;
        public boolean status = true;
        public int cartNumber;

        public ViewHolder(final View itemView) {
            super(itemView);

            price_text = (TextView) itemView.findViewById(R.id.price_text);
            regular_price_text = (TextView) itemView.findViewById(R.id.regular_price_text);
            productName_text = (TextView) itemView.findViewById(R.id.productName_text);
            addCart_text = (TextView) itemView.findViewById(R.id.addCart_text);
            less_cart = (ImageView) itemView.findViewById(R.id.less_cart);
            image = (ImageView) itemView.findViewById(R.id.image);
            add_CardNumber = (TextView) itemView.findViewById(R.id.add_CardNumber);
            add_Cart = (ImageView) itemView.findViewById(R.id.add_Cart);
            addMoreCart_Text = (LinearLayout) itemView.findViewById(R.id.addMoreCart_Text);
            cart_list = (LinearLayout) itemView.findViewById(R.id.cart_list);

            cartNumber = Integer.parseInt(add_CardNumber.getText().toString());

            addCart_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (status) {
                        addCart_text.setVisibility(View.GONE);
                        addMoreCart_Text.setVisibility(View.VISIBLE);
                        cart_list.setBackgroundResource(R.drawable.border_shape);

                        Context context = itemView.getContext();
                        MainBeen mainBeen1 = (MainBeen) itemView.getTag();
                        db = new sqLiteDB(context);
                        MainBeen mainBeen = new MainBeen();
                        String carNo = add_CardNumber.getText().toString();
                        mainBeen.setCartNumber(carNo);
                        mainBeen.setProductName(mainBeen1.getProductName());
                        mainBeen.setPrice(mainBeen1.getPrice());
                        mainBeen.setImage(mainBeen1.getImage());
                        mainBeen.setCategoryID(mainBeen1.getCategoryID());
                        Log.e("actual price == > " , mainBeen1.getActual_price());
                        mainBeen.setActual_price(mainBeen1.getActual_price());
                        db.insertCartData(mainBeen);

                        if(cartStatus == true){
                            if(context instanceof Product_CategoryActivity){
                                ((Product_CategoryActivity)context).onCartCallback(carNo);
                            }
                        }else{
                            if(context instanceof OrderViewActivity){
                                ((OrderViewActivity)context).onCartCallback(carNo);
                            }
                        }


                        status = true;
                    } else {
                        addCart_text.setVisibility(View.VISIBLE);
                        addMoreCart_Text.setVisibility(View.GONE);
                        status = true;
                    }
                }
            });

            less_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = itemView.getContext();
                    MainBeen mainBeen1 = (MainBeen) itemView.getTag();

                    String ct_Id = mainBeen1.getCategoryID();
                    double price = Double.parseDouble(mainBeen1.getPrice());
                    double total_price = 00.00;
                    String sumprice = null;


                    double ctPrice = 00.00;
                    sqLiteDB db = new sqLiteDB(context);
                    String catID = null;
                    List<MainBeen> cartList = db.getAddCart();
                    for (MainBeen mBeen : cartList) {

                        String ctID = mBeen.getCategoryID();

                        if (ctID.equals(ct_Id)) {
                            ctPrice = Double.parseDouble(mBeen.getPrice());
                            cartNumber = Integer.parseInt(mBeen.getCartNumber());
                            catID = mBeen.getCategoryID();
                        }

                    }

                    if (cartNumber > 1) {
                        cartNumber--;
                        add_CardNumber.setText("" + cartNumber);
                    } else {
                       db.carDelete(catID);
                        addCart_text.setVisibility(View.VISIBLE);
                        addMoreCart_Text.setVisibility(View.GONE);
                        cart_list.setBackgroundResource(R.drawable.border_white_shape);
                    }


                    total_price = ctPrice - price;
                    sumprice = String.valueOf(total_price);


                    String cnumbrt = String.valueOf(cartNumber);
                    db.updatecartTable(ct_Id, sumprice, cnumbrt);

                    if(context instanceof Product_CategoryActivity){
                        ((Product_CategoryActivity)context).onCartCallback(ct_Id);
                    }


                }
            });

            add_Cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = itemView.getContext();
                    MainBeen mainBeen1 = (MainBeen) itemView.getTag();


                    String ct_Id = mainBeen1.getCategoryID();
                    double price = Double.parseDouble(mainBeen1.getPrice());
                    double total_price = 00.00;
                    String sumprice = null;

                    double ctPrice = 00.00;
                    sqLiteDB db = new sqLiteDB(context);
                    List<MainBeen> cartList = db.getAddCart();
                    for (MainBeen mBeen : cartList) {

                        String ctID = mBeen.getCategoryID();

                        if (ctID.equals(ct_Id)) {
                            ctPrice = Double.parseDouble(mBeen.getPrice());
                            cartNumber = Integer.parseInt(mBeen.getCartNumber());

                        }

                    }


                    if (cartNumber >= 1) {
                        cartNumber++;
                        add_CardNumber.setText("" + cartNumber);
                    }

                    total_price = ctPrice + price;
                    sumprice = String.valueOf(total_price);
                    String cnumbrt = String.valueOf(cartNumber);
                    db.updatecartTable(ct_Id, sumprice, cnumbrt);

                }
            });

            cart_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = itemView.getContext();
                    MainBeen mainBeen = (MainBeen) itemView.getTag();

                    Intent intent = new Intent(context, OrderViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("productName", mainBeen.getProductName());
                    intent.putExtra("image", mainBeen.getImage());
                    intent.putExtra("price", mainBeen.getPrice());
                    intent.putExtra("description", mainBeen.getDescription());
                    intent.putExtra("productId", productId);
                    intent.putExtra("categoriesName", categoriesName);
                    intent.putExtra("categoryID", mainBeen.getCategoryID());
                    context.startActivity(intent);
                    ((Activity)context).finish();



                }
            });

        }
    }

    public static interface AdapterCallback {
        void onCartCallback(String  ct_Id);
    }
}
