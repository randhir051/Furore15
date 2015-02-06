package com.example.dsi.furore;

/**
 * Created by Randhir on 2/6/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Randhir on 1/29/2015.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<Event> data = new ArrayList<>();

    public EventListAdapter(Context context, List<Event> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_list_element,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event current = data.get(position);
        holder.image.setImageResource(current.image);
        holder.name.setText(current.name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.joined_group_image);
            name = (TextView) itemView.findViewById(R.id.joined_group_name);

        }
    }
}
