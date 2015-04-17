package com.cse.dsi.furore;

/**
 * Created by Randhir on 2/6/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsy.android.grid.util.DynamicHeightImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Randhir on 1/29/2015.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Event> data = new ArrayList<>();
    Context c;
    Random rand = new Random();

    public EventListAdapter(Context context, List<Event> data) {
        c = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_list_element, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event current = data.get(position);
        int resId = 0;
        try {
            Field idField = R.drawable.class.getDeclaredField("event" + current.id);
            resId = idField.getInt(idField);
            holder.image.setImageResource(resId);
        } catch (Exception e) {
            Log.d("No resource ID found:", "" + resId + " / " + e);
        }
        holder.name.setText(current.name);
        holder.image.setHeightRatio(getRandomHeight());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        DynamicHeightImageView image;
        TextView name;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (DynamicHeightImageView) itemView.findViewById(R.id.event_image);
            name = (TextView) itemView.findViewById(R.id.event_name);

        }
    }


    private double getRandomHeight() {
        int randomNum = rand.nextInt(40);
        return 0.80+randomNum/100.0;
    }
}
