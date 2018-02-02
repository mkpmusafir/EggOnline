package bthrust.eggonline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;
import bthrust.eggonline.eggOnline.Product_CategoryActivity;

/**
 * Created by win-3 on 2/1/2018.
 */

public class drawerAdapter extends RecyclerView.Adapter<drawerAdapter.ViewHolder> {

    private Context context;
    private List<MainBeen> items;
    MainBeen mainBeen;

    public drawerAdapter(Context context, ArrayList<MainBeen> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public drawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_list_items , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(drawerAdapter.ViewHolder holder, int position) {
        mainBeen = items.get(position);
        holder.categoriesName.setText(items.get(position).getProductCategoryName());
        holder.itemView.setTag(mainBeen);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoriesName;
        public ViewHolder(final View itemView) {
            super(itemView);

            categoriesName = itemView.findViewById(R.id.categories_Text);
            categoriesName.setOnClickListener(new View.OnClickListener() {
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
