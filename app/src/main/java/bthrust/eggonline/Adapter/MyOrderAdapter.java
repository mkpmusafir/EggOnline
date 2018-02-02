package bthrust.eggonline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;
import bthrust.eggonline.eggOnline.MyOrder_ItemsList_Activity;
import bthrust.eggonline.eggOnline.OrderViewActivity;

/**
 * Created by win-3 on 1/22/2018.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private Context context;
    private  List<MainBeen> items;
    private String totalCart;
    MainBeen mainBeen;

    int slNo = 1;
    public MyOrderAdapter(Context context, List<MainBeen> items) {
        this.context = context;
        this.items = items;
        this.totalCart = totalCart;
    }

    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_list_items , parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyOrderAdapter.ViewHolder holder, int position) {
        mainBeen = items.get(position);

        String date = items.get(position).getMy_orderDate();
        String[] dateformet = date.split("T");
        String[] newdateFormet = dateformet[0].split("-");
        String newdate1 = newdateFormet[0]+"/"+newdateFormet[1]+"/"+newdateFormet[2];
        Date dates = new Date(newdate1);
        SimpleDateFormat dt1 = new SimpleDateFormat("EEE, dd  MMM");

        holder.myOrder_Date.setText(""+dt1.format(dates));
        holder.myOrder_quantity.setText(items.get(position).getMy_OrderTotalCart());
        holder.myOrder_Amount.setText("$"+items.get(position).getMy_OrderTotalAmount());
        holder.myorder_slNo.setText(""+slNo);
        slNo++;
        holder.itemView.setTag(mainBeen);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView myorder_slNo , myOrder_quantity , myOrder_Date , myOrder_Amount;
        public LinearLayout my_orderSection;
        public ViewHolder(final View itemView) {
            super(itemView);

            myorder_slNo = itemView.findViewById(R.id.myorder_slNo);
            myOrder_quantity = itemView.findViewById(R.id.myOrder_quantity);
            myOrder_Date = itemView.findViewById(R.id.myOrder_Date);
            myOrder_Amount = itemView.findViewById(R.id.myOrder_Amount);
            my_orderSection = itemView.findViewById(R.id.my_orderSection);

            my_orderSection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = itemView.getContext();
                    MainBeen mainBeen = (MainBeen) itemView.getTag();


                    Intent intent = new Intent(context, MyOrder_ItemsList_Activity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("id" , mainBeen.getMy_OrderId());
                    context.startActivity(intent);
                }
            });
        }
    }
}
