package com.example.dsi.furore;

/**
 * Created by Randhir on 2/6/2015.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Randhir on 1/29/2015.
 */
public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<EventType> data = new ArrayList<>();

    public EventTypeAdapter(Context context, List<EventType> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.event_type_element, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EventType current = data.get(position);
        switch (current.name) {
            case "Theatre":
                holder.image.setImageResource(R.drawable.theater);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_art);
                break;
            case "Technical":
                holder.image.setImageResource(R.drawable.technical);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
                break;
            case "Sports":
                holder.image.setImageResource(R.drawable.sports);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
                break;
            case "Music":
                holder.image.setImageResource(R.drawable.music);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
                break;
            case "Literary":
                holder.image.setImageResource(R.drawable.literary);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
                break;
            case "Kannada":
                holder.image.setImageResource(R.drawable.kannda);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
                break;
            case "Fashion":
                holder.image.setImageResource(R.drawable.fashion);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
                break;
            case "Elements":
                holder.image.setImageResource(R.drawable.elements);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
                break;
            case "Dance":
                holder.image.setImageResource(R.drawable.dance);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
                break;
            case "Art":
                holder.image.setImageResource(R.drawable.art);
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
                break;
            default:
                holder.image.setImageResource((R.drawable.dance123));
                holder.layout.setBackgroundResource(R.drawable.category_gradient_dance);
        }
        holder.name.setText(current.name);
        holder.number.setText(current.numEvents + " Events");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, number;
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.category_text_layout);
            image = (ImageView) itemView.findViewById(R.id.category_image);
            name = (TextView) itemView.findViewById(R.id.category);
            number = (TextView) itemView.findViewById(R.id.number_of_events);

        }
    }
}
