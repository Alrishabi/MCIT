package com.example.musab.mcit.AdminFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musab.mcit.R;
import com.example.musab.mcit.URLs.URL;
import com.example.musab.mcit.mRecyclerView.BroadcastAdapter;
import com.example.musab.mcit.mRecyclerView.BroadcastList;
import com.example.musab.mcit.mRecyclerView.QuestionnaireAdapter;
import com.example.musab.mcit.mRecyclerView.QuestionnaireList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class QuestionnaireFragment extends Fragment {
    RecyclerView recyclerview;
    QuestionnaireAdapter adapter;
    ArrayList<QuestionnaireList> questionnaireLists;
    URL url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.questionnaire_fragment, container, false);
        setHasOptionsMenu(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        recyclerview = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        questionnaireLists = new ArrayList<>();
        loadRecycelerViewData();
        return rootView;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void loadRecycelerViewData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url.LINK_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("questionnaire_table");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject o = array.getJSONObject(i);
                                QuestionnaireList item = new QuestionnaireList(
                                        o.getInt("questionnaire_id"),
                                        o.getString("questionnaire_title"),
                                        o.getString("questionnaire_link"),
                                        o.getString("questionnaire_date")
                                );
                                questionnaireLists.add(item);
                            }
                            adapter = new QuestionnaireAdapter(questionnaireLists, getContext(), getFragmentManager());
                            recyclerview.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}
