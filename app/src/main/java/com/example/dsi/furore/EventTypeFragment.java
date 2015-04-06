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
public class EventTypeFragment extends Fragment {
    private RecyclerView eventTypeRecyclerView;
    public EventTypeAdapter adapter;
    public LinearLayoutManager layoutManager;

    List<EventType> data = new ArrayList<>();
    View layout;


    public EventTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_event_type, container, false);
        eventTypeRecyclerView = (RecyclerView) layout.findViewById(R.id.event_type_recycler);
        DBEventDetails get = new DBEventDetails(getActivity());
        get.open();
        data = get.getCategories();
        get.close();
        adapter = new EventTypeAdapter(getActivity(), data);

        eventTypeRecyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity());
        eventTypeRecyclerView.setLayoutManager(layoutManager);
        eventTypeRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), eventTypeRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ((MainActivity) getActivity()).list.setData(adapter.data.get(position).name);
                //((MainActivity) getActivity()).list.adapter.notifyDataSetChanged();
                ((MainActivity) getActivity()).mPager.setCurrentItem(1, true);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        return layout;
    }

    public void setData(){
        DBEventDetails get = new DBEventDetails(getActivity());
        get.open();
        adapter.data = get.getCategories();
        get.close();
        adapter.notifyDataSetChanged();
    }
    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
    }

}
