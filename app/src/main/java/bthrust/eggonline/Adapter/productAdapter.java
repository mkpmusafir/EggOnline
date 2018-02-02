package bthrust.eggonline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;
import bthrust.eggonline.eggOnline.Product_CategoryActivity;


/**
 * Created by win-3 on 12/2/2017.
 */

public class productAdapter extends RecyclerView.Adapter<productAdapter.ViewHolder> {

    private ArrayList<MainBeen> items;
    private Context context;
    MainBeen mainBeen;

    public productAdapter(Context context, ArrayList<MainBeen> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public productAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(productAdapter.ViewHolder holder, int position) {

        mainBeen = items.get(position);

       // Log.e("product Name :-  " , items.get(position).getProductCategoryName());
        holder.product_name.setText(items.get(position).getProductCategoryName());
        Picasso.with(context).load(items.get(position).getImage()).into(holder.product_CetagoryImage);
        holder.itemView.setTag(mainBeen);

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
        public TextView product_name;
        public  ImageView product_CetagoryImage;
        public RelativeLayout productList;
        public ViewHolder(final View itemView) {
            super(itemView);

            product_name = (TextView) itemView.findViewById(R.id.product_name);
            product_CetagoryImage = (ImageView) itemView.findViewById(R.id.product_CategoryImage);
            productList = (RelativeLayout) itemView.findViewById(R.id.productList);


            productList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Context context = itemView.getContext();
                    MainBeen mainBeen = (MainBeen) itemView.getTag();

                    Intent intent = new Intent(context , Product_CategoryActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("categoriesName" , mainBeen.getProductCategoryName());
                    intent.putExtra("id" , mainBeen.getId());
                    context.startActivity(intent);
                }
            });

        }
    }
}
