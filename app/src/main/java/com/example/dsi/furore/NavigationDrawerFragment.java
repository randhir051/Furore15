package com.example.dsi.furore;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationDrawerAdapter.ClickListener {


    RecyclerView recyclerView;
    NavigationDrawerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout mDrawerLayout;

    SharedPreferences getPrefs;
    ActionBarDrawerToggle mDrawerToggle;
    String NAME = "My name";
    String EMAIL = "my email sddress";
    String ACTIONS[] = {
            "com.broda.snup.Profile",
            "com.broda.snup.CreateGroup",
            "com.broda.snup.CreateGroup",
            "com.broda.snup.AboutUs"
    };
    int PROFILE = R.drawable.ic_launcher;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
//        getPrefs = getActivity().getApplication().getSharedPreferences(Utility.SHARED_PREFS,0);
//        NAME = getPrefs.getString("name"," ");
//        EMAIL = getPrefs.getString("email"," ");
        recyclerView = (RecyclerView) layout.findViewById(R.id.nav_drawer_recycler_view);
        recyclerView.setHasFixedSize(true);
        mAdapter = new NavigationDrawerAdapter(NAME, EMAIL, PROFILE);
        mAdapter.setClickListener(this);

        recyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        return layout;
    }

    public void setUp(DrawerLayout drawerLayout, final Toolbar toolbar) {

        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }



        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
//        mDrawerLayout.post(new Runnable() {
//            @Override
//            public void run() {
                mDrawerToggle.syncState();
//            }
//        });


    }

    @Override
    public void itemClicked(View view, int position) {


        final int pos = position;
        mDrawerLayout.closeDrawers();
        mDrawerLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pos != 1) {
                    String selectedAction = ACTIONS[pos];

                    try {
                        startActivity(new Intent(getActivity(),Class.forName(selectedAction)));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        },200);


    }

}