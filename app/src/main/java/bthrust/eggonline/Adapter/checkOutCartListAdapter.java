package bthrust.eggonline.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;

/**
 * Created by win-3 on 1/18/2018.
 */

public class checkOutCartListAdapter extends RecyclerView.Adapter<checkOutCartListAdapter.ViewHolder> {
    private Context context;
    private List<MainBeen> items;
    public checkOutCartListAdapter(Context context, List<MainBeen> cartList) {
        this.items = cartList;
        this.context = context;

    }

    @Override
    public checkOutCartListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_cartlist_items , parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(checkOutCartListAdapter.ViewHolder holder, int position) {

        holder.cartList_productNameText.setText(items.get(position).getProductName());
        holder.cart_countText.setText(items.get(position).getCartNumber());
        double price = Double.parseDouble(items.get(position).getPrice());
        NumberFormat formatter = new DecimalFormat(".##");
        double cartPrice = Double.parseDouble(formatter.format(price));

        holder.cartList_Amount.setText("$"+cartPrice);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView cartList_productNameText,cartList_Amount , cart_countText;
        public ViewHolder(View itemView) {
            super(itemView);
            cartList_productNameText = itemView.findViewById(R.id.cartList_productNameText);
            cartList_Amount = itemView.findViewById(R.id.cartList_Amount);
            cart_countText = itemView.findViewById(R.id.cart_countText);

        }
    }
}
