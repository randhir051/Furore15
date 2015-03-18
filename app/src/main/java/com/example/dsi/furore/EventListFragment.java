package com.example.dsi.furore;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class EventListFragment extends Fragment {


    private RecyclerView eventListRecyclerView;
    public EventListAdapter adapter;
    public LinearLayoutManager layoutManager;

    List<Event> data = new ArrayList<>();
    View layout;

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_event_list, container, false);
        eventListRecyclerView = (RecyclerView) layout.findViewById(R.id.event_list_recycler);
        adapter = new EventListAdapter(getActivity(), data);
        eventListRecyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        eventListRecyclerView.setLayoutManager(layoutManager);
        eventListRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), eventListRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ((MainActivity) getActivity()).mPager.setCurrentItem(3);
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(), "Long click", Toast.LENGTH_SHORT).show();
            }
        }));
        return layout;

    }

    public void setData(int position) {
        data.clear();
        switch (position){
            case 0:
                data.add(new Event("Fireless Cooking",2));
                data.add(new Event("Hogathon",2));
                data.add(new Event("Cooking",2));
                break;
            case 1:
                data.add(new Event("Face Painting",2));
                data.add(new Event("Mehendi",2));
                data.add(new Event("Nail Art",2));
                data.add(new Event("Best of waste",2));
                data.add(new Event("Spot Painting",2));
                data.add(new Event("Collage",2));
                break;
            case 2:
                data.add(new Event("Shuddh Desi Dance",2));
                data.add(new Event("Break a sweat on the floor",2));
                data.add(new Event("3 ka tadka",2));
                data.add(new Event("Tapangucci aadu machi",2));
                data.add(new Event("Themed performance",2));
                data.add(new Event("Non theme dance",2));
                break;
            case 3:
                data.add(new Event("Counter Strike",2));
                data.add(new Event("Road Rash",2));
        }
        adapter.notifyDataSetChanged();
    }


    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements  RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListener clickListener;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener){
            this.clickListener=clickListener;
            gestureDetector=new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if(child!=null && clickListener!=null)
                    {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child=rv.findChildViewUnder(e.getX(), e.getY());
            if(child!=null && clickListener!=null && gestureDetector.onTouchEvent(e))
            {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }
}
