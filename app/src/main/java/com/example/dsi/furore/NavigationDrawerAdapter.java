package com.example.dsi.furore;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private String mNavTitles[] = {"Events", "Furore Timeline", "Selfie", "Facebook", "About Us"};
    private int mIcons[] = {R.drawable.ic_action,
            R.drawable.ic_action_time, R.drawable.ic_action, R.drawable.ic_action,
            R.drawable.ic_action_about};


    private String name;
    private int profile;
    private String email;

    private ClickListener clickListener;


    NavigationDrawerAdapter(String Name, String Email, int Profile) {
        name = Name;
        email = Email;
        profile = Profile;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;
        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView email;


        public ViewHolder(View itemView, int ViewType) {
            super(itemView);

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText);
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);
                Holderid = 1;
            } else {
                Name = (TextView) itemView.findViewById(R.id.name);
                email = (TextView) itemView.findViewById(R.id.email);
                //TODO
                //profile = (ImageView) itemView.findViewById(R.id.circleView);
                Holderid = 0;
            }

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }

        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item_row, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;

        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            ViewHolder vhHeader = new ViewHolder(v, viewType);
            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.Holderid == 1) {
            holder.textView.setText(mNavTitles[position - 1]);
            holder.imageView.setImageResource(mIcons[position - 1]);
        } else {
            //TODO
            //holder.profile.setImageResource(profile);
            holder.Name.setText(name);
            holder.email.setText(email);
        }
    }


    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public interface ClickListener {
        public void itemClicked(View view, int position);
    }
}