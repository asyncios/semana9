package com.example.josuecadillo.semana9material;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.example.josuecadillo.semana9material.adapter.CustomItemAdapter;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerFragment extends Fragment {

    private RecyclerView recyclerView;
    private CustomItemAdapter adapter;

    public static final String PREF_FILE_NAME = "textpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;

    private boolean mUserLearnedDrawer;
    private boolean mFromSaveInstanceState;
    private View containerView;


    public NavigationDrawerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,"false"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new CustomItemAdapter(getActivity(),getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public void setUp(int fragmentID,DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentID);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_open){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer){
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset<0.5){
                    toolbar.setAlpha(1-slideOffset);
                }
            }
        };

        if (!mUserLearnedDrawer && !mFromSaveInstanceState){
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static List<CustomItem> getData(){
        List<CustomItem> data = new ArrayList<>();
        int[] iconList = {R.drawable.ic_number1,R.drawable.ic_number2,R.drawable.ic_number3,R.drawable.ic_number4};
        String[] stringList = {"Opcion 1","Opcion 2","Opcion 3","Opcion 4"};
        for (int i = 0; i < iconList.length && i< stringList.length; i++) {
            CustomItem current = new CustomItem();
            current.setTitle(stringList[i]);
            current.setIconId(iconList[i]);
            data.add(current);
        }

        return data;

    }
    public static void saveToPreferences(Context context,String preferenceName,String preferenceValue){
        SharedPreferences sharedPrefences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.commit();
    }
    public static String readFromPreferences(Context context,String preferenceName,String defaultValue){
        SharedPreferences sharedPrefences = context.getSharedPreferences(PREF_FILE_NAME,context.MODE_PRIVATE);
        return sharedPrefences.getString(preferenceName,defaultValue);
    }
}