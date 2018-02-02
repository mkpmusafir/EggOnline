package bthrust.eggonline.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;
import bthrust.eggonline.dataBase.sqLiteDB;
import bthrust.eggonline.eggOnline.AddCart_Class;


/**
 * Created by win-3 on 12/12/2017.
 */

public class AddCartAdapter extends RecyclerView.Adapter<AddCartAdapter.ViewHolder> {

    private List<MainBeen> items;
    private Context context;
    private MainBeen mainBeen;
    private AdapterCallback mAdapterCallback;
    public AddCartAdapter(Context context, List<MainBeen> items) {

        this.context = context;
        this.items = items;

    }


    @Override
    public AddCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_cart_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AddCartAdapter.ViewHolder holder, int position) {

        mainBeen = items.get(position);

        double cost = Double.parseDouble(items.get(position).getPrice());
        NumberFormat formatter = new DecimalFormat(".##");
        double  totalPrice = Double.parseDouble(formatter.format(cost));
        holder.totalPrice.setText("$"+totalPrice);
        holder.productNameText.setText(items.get(position).getProductName());
        holder.cartNumber_Text.setText(items.get(position).getCartNumber());
        Picasso.with(context).load(items.get(position).getImage()).into(holder.productImage);
        holder.itemView.setTag(mainBeen);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView totalPrice , productNameText , cartNumber_Text;
        public ImageView productImage , add_cart_text ,less_cart_text;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;
        public ViewHolder(final View itemView) {
            super(itemView);

            totalPrice = itemView.findViewById(R.id.totalPrice_text);
            productNameText = itemView.findViewById(R.id.productNameText);
            cartNumber_Text = itemView.findViewById(R.id.cartNumber_Text);
            productImage = itemView.findViewById(R.id.productImage);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            add_cart_text = itemView.findViewById(R.id.add_cart_text);
            less_cart_text = itemView.findViewById(R.id.less_cart_text);


            add_cart_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Context context = itemView.getContext();
                    MainBeen mainBeen = (MainBeen) itemView.getTag();
                    String  ct_Id = mainBeen.getCategoryID();
                    double   actual_price = Double.parseDouble(mainBeen.getActual_price());
                    Log.e("dddd=-===> " , mainBeen.getActual_price());

                    double total_price = 00.00;
                    String sumprice = null;
                    double ctPrice = 00.00;
                    sqLiteDB db = new sqLiteDB(context);
                    String catID = null;
                    int cartNo = 0;
                    List<MainBeen> cartList = db.getAddCart();
                    for (MainBeen mBeen : cartList) {

                        String ctID = mBeen.getCategoryID();
                        if (ctID.equals(ct_Id)) {
                            ctPrice = Double.parseDouble(mBeen.getPrice());
                            cartNo = Integer.parseInt(mBeen.getCartNumber());
                            catID = mBeen.getCategoryID();

                        }

                    }

                    if(cartNo >= 1){
                        cartNo++;
                        Log.e("dddd=-22=22==> " , String.valueOf(cartNo));
                        cartNumber_Text.setText(""+cartNo);
                    }else{
                        Log.e("dddd=-22=33==> " , String.valueOf(cartNo));
                    }

                    total_price = ctPrice + actual_price;
                    sumprice = String.valueOf(total_price);

                    NumberFormat formatter = new DecimalFormat(".##");
                    double  t_Price = Double.parseDouble(formatter.format(total_price));
                    totalPrice.setText("$"+t_Price);

                    String cnumbrt = String.valueOf(cartNo);
                    db.updatecartTable(ct_Id, sumprice, cnumbrt);

                if(context instanceof AddCart_Class){
                       ((AddCart_Class)context).onMethodCallback(actual_price);
                    }

                }
            });

            less_cart_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = itemView.getContext();
                    MainBeen mainBeen = (MainBeen) itemView.getTag();
                    String  ct_Id = mainBeen.getCategoryID();
                    double   actual_price = Double.parseDouble(mainBeen.getActual_price());
                    Log.e("dddd=-===> " , mainBeen.getActual_price());

                    double total_price = 00.00;
                    String sumprice = null;
                    double ctPrice = 00.00;
                    sqLiteDB db = new sqLiteDB(context);
                    String catID = null;
                    int cartNo = 0;
                    List<MainBeen> cartList = db.getAddCart();
                    for (MainBeen mBeen : cartList) {

                        String ctID = mBeen.getCategoryID();
                        if (ctID.equals(ct_Id)) {
                            ctPrice = Double.parseDouble(mBeen.getPrice());
                            cartNo = Integer.parseInt(mBeen.getCartNumber());
                            catID = mBeen.getCategoryID();

                        }

                    }

                    if(cartNo > 1){
                        cartNo--;
                        Log.e("dddd=-22===> " , String.valueOf(cartNo));
                        cartNumber_Text.setText(""+cartNo);
                    }else{
                        Log.e("dddd=-2211===> " , String.valueOf(cartNo));
                        db.carDelete(catID);
                        removeItem(getAdapterPosition());
                    }

                    total_price = ctPrice - actual_price;
                    sumprice = String.valueOf(total_price);

                    NumberFormat formatter = new DecimalFormat(".##");
                    double  t_Price = Double.parseDouble(formatter.format(total_price));
                    totalPrice.setText("$"+t_Price);

                    String cnumbrt = String.valueOf(cartNo);
                    db.updatecartTable(ct_Id, sumprice, cnumbrt);

                    if(context instanceof AddCart_Class){
                        ((AddCart_Class)context).onMethodCallback(actual_price);
                    }

                }
            });

        }
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(MainBeen item, int position) {
        items.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public static interface AdapterCallback {
        void onMethodCallback(double price);
    }


}
