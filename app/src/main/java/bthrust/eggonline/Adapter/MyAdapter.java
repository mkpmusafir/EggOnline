package bthrust.eggonline.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import bthrust.eggonline.Been.MainBeen;
import bthrust.eggonline.R;
import bthrust.eggonline.eggOnline.OrderViewActivity;

public class MyAdapter extends PagerAdapter {

    private List<MainBeen> images;
    private LayoutInflater inflater;
    private Context context;
    MainBeen mainBeen;



    public MyAdapter(Context context, List<MainBeen> imageList) {
        this.context = context;
        this.images=imageList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);

        mainBeen = images.get(position);
        Picasso.with(context).load(mainBeen.getViewImage()).into(myImage);
        Log.e("sssssss=====> " , String.valueOf(images.get(position).getViewImage()));
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}