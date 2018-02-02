package bthrust.eggonline.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;

/**
 * Created by win-3 on 1/24/2018.
 */

public class MyOrderItem_ListAdapter extends RecyclerView.Adapter<MyOrderItem_ListAdapter.ViewHolder> {

    private List<MainBeen> items;
    private Context context;
    private int slno = 1;

    public MyOrderItem_ListAdapter(Context context, List<MainBeen> items) {
        this.items = items;
        this.context = context;

    }

    @Override
    public MyOrderItem_ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_list_items, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyOrderItem_ListAdapter.ViewHolder holder, int position) {

        holder.myOrder_name.setText(items.get(position).getProductName());
        holder.myOrderQuantity_Text.setText(items.get(position).getMy_OrderTotalCart());
        holder.myOrder_Amount_Text.setText("$"+items.get(position).getMy_OrderTotalAmount());
        holder.slNumber_text.setText(""+slno);
        slno++;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView myOrder_name , myOrderQuantity_Text , myOrder_Amount_Text , slNumber_text;
        public ViewHolder(View itemView) {
            super(itemView);

            myOrder_name = itemView.findViewById(R.id.myOrder_name);
            myOrderQuantity_Text = itemView.findViewById(R.id.myOrderQuantity_Text);
            myOrder_Amount_Text = itemView.findViewById(R.id.myOrder_Amount_Text);
            slNumber_text = itemView.findViewById(R.id.slNumber_text);

        }
    }
}
