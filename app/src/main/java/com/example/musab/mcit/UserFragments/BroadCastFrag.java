package com.example.musab.mcit.UserFragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musab.mcit.R;
import com.example.musab.mcit.URLs.URL;
import com.example.musab.mcit.mFragment.LoginFragment;
import com.example.musab.mcit.mRecyclerView.BroadcastAdapter;
import com.example.musab.mcit.mRecyclerView.BroadcastList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by musab on 7/18/2017.
 */

public class BroadCastFrag extends android.support.v4.app.Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerview;
    private BroadcastAdapter adapter;
    private LoginFragment loginFragment;
    URL url;
    SwipeRefreshLayout swipeRefreshLayout;


    private ArrayList<BroadcastList> listItems;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.broad_cast_fragment, container, false);
        setHasOptionsMenu(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.vote_swap);

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        listItems = new ArrayList<>();
        loadRecycelerViewData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                loadRecycelerViewData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.hi_greeting)+" "+ LoginFragment.n.substring(0, LoginFragment.n.indexOf(" ")));
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(this);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        super.onCreateOptionsMenu(menu,inflater);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void loadRecycelerViewData() {
       StringRequest stringRequest =new StringRequest(Request.Method.GET,
               URL.DISPLAY_BROADCAST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("broadcast_table");
                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);
                                BroadcastList item= new BroadcastList(
                                        o.getString("broadcast_title"),
                                        o.getString("broadcast_content"),
                                        o.getString("broadcast_date"),
                                        o.getString("broadcast_type"),
                                        o.getInt("broadcast_id"));
                                listItems.add(item);
                            }
                            adapter =new BroadcastAdapter(listItems,getContext(),getFragmentManager());
                            recyclerview.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),R.string.connection_failed,Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        try {
            if (newText.equals(null)) {
                Toast.makeText(getContext(),R.string.not_item_found, Toast.LENGTH_LONG).show();
            } else {
                newText = newText.toLowerCase();
                ArrayList<BroadcastList> newList = new ArrayList<>();
                for (BroadcastList listItem : listItems) {
                    String name = listItem.getHead().toLowerCase();
                    if (name.contains(newText)) {
                        newList.add(listItem);
                    }
                }adapter.setFilter(newList);
            }
        }catch (Exception e){
            Toast.makeText(getContext(),R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
        }
        return true;
        }
}


