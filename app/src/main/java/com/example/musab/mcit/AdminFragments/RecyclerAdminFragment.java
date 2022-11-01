package com.example.musab.mcit.AdminFragments;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musab.mcit.Activities.InsertBroadcastActivity;
import com.example.musab.mcit.R;
import com.example.musab.mcit.URLs.URL;
import com.example.musab.mcit.mFragment.LoginFragment;
import com.example.musab.mcit.mRecyclerView.BroadcastAdminAdapter;
import com.example.musab.mcit.mRecyclerView.BroadcastList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by musab on 7/27/2017.
 */

public class RecyclerAdminFragment extends Fragment implements SearchView.OnQueryTextListener {
    URL url;
    private RecyclerView recyclerviewAdmin;
    public LoginFragment adminFrag;
    private BroadcastAdminAdapter adapter;
    private ArrayList<BroadcastList> adimlist;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_admin_fragment, container, false);
        setHasOptionsMenu(true);


        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());


        linearLayoutManager.setReverseLayout(true);recyclerviewAdmin = (RecyclerView) rootView.findViewById(R.id.ArecyclerView);
        recyclerviewAdmin.setHasFixedSize(true);
        recyclerviewAdmin.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        adimlist = new ArrayList<>();

        loadRecycelerViewData();

        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.admin_menu_insert, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(this);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_insert:
                startActivity(new Intent(getActivity(),InsertBroadcastActivity.class));
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
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
                for (BroadcastList listItem : adimlist) {
                    String name = listItem.getHead().toLowerCase();
                    if (name.contains(newText)) {
                        newList.add(listItem);
                    }
                }
                adapter.setFilter(newList);
            }
        }catch (Exception e){
            Toast.makeText(getContext(),R.string.nothing_to_search_it, Toast.LENGTH_LONG).show();
        }
        return true;
    }
/////////////////////////////////////////////////////////load Admin data////////////////////////////////////////////////////////////////
private void loadRecycelerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));
        progressDialog.show();
        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                url.DISPLAY_ADMIN_BC_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
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
                                        adimlist.add(item);
                            }
                            adapter =new BroadcastAdminAdapter(adimlist,getContext(),getFragmentManager());
                            recyclerviewAdmin.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(),R.string.sorry+"\n"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
               @Override
                  protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String, String> params= new HashMap<>();
                   params.put("user_id", String.valueOf(adminFrag.id));
                  return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
