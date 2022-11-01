package com.example.musab.mcit.AdminFragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musab.mcit.Activities.InsertCandidates_Activity;
import com.example.musab.mcit.R;
import com.example.musab.mcit.URLs.URL;
import com.example.musab.mcit.mFragment.LoginFragment;
import com.example.musab.mcit.mRecyclerView.BroadcastAdminAdapter;
import com.example.musab.mcit.mRecyclerView.BroadcastList;
import com.example.musab.mcit.mRecyclerView.CandidateList;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoteAdminFragment extends Fragment {

    private RecyclerView recyclerviewAdmin;
    private BroadcastAdminAdapter adapter;
     ArrayList<BroadcastList> broadcastLists;
    private Spinner SpinnerV1,SpinnerV2;
    private TextView txtV1,txtV2,votNo,voters_counter;
    private ImageView votingBtn;
    private SwipeRefreshLayout swipeRefreshLayout;
    public URL url;
    private LinearLayout mRoot;
    private ArrayList yAxis = null;
    private ArrayList yValues;
    private ArrayList xAxis1;
    public BarEntry values ;
    public BarChart chart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.vote_admin_fragment, container, false);
        setHasOptionsMenu(true);
        mRoot=(LinearLayout) rootView.findViewById(R.id.mroot);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());

        recyclerviewAdmin=(RecyclerView) rootView.findViewById(R.id.candidateRecyclerView);


        SpinnerV1 = (Spinner) rootView.findViewById(R.id.spinnerV1);
        SpinnerV2 = (Spinner) rootView.findViewById(R.id.spinnerV2);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.vote_swap);


        votNo = (TextView) rootView.findViewById(R.id.votingNo);
        txtV1 = (TextView) rootView.findViewById(R.id.textVoting1);
        txtV2 = (TextView) rootView.findViewById(R.id.textVoting2);

        voters_counter = (TextView) rootView.findViewById(R.id.voter_counter);


        votingBtn=(ImageView) rootView.findViewById(R.id.buttonV);
        chart = (BarChart) rootView.findViewById(R.id.chart);


        ArrayAdapter<CharSequence> degreeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Degree, android.R.layout.simple_spinner_item);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerV1.setAdapter(degreeAdapter);
        SpinnerV2.setAdapter(degreeAdapter);

        recyclerviewAdmin.setHasFixedSize(true);
        recyclerviewAdmin.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                showCandidates();
                load_data_from_server();
                voters_counter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        votingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginFragment.v_state > 0 ){
                    new AlertDialog.Builder(getContext())
                            .setIcon(R.drawable.ic_action_delete)
                            .setTitle(R.string.attention_dialog_title)
                            .setMessage(R.string.attention_message)
                            .setPositiveButton(R.string.dialog_quit_positive_button,new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }else {
                    new AlertDialog.Builder(getContext())
                            .setIcon(R.drawable.ic_attention)
                            .setTitle(R.string.attention_dialog_title)
                            .setMessage(R.string.sure_message)
                            .setPositiveButton(R.string.dialog_quit_positive_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    submitVoting();
                                }
                            }).setNegativeButton(R.string.cancel_button,null)
                            .show();
                }

            }
        });
////////////////////////////////////////////////////////////////////////////////////////

      //  loadCandidateRecyclerViewData();
        loadRecycelerViewData();
        showCandidates();
        load_data_from_server();
        voters_counter();

        return rootView;
    }
    public void loadRecycelerViewData() {
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
                                broadcastLists.add(item);
                            }
                            adapter =new BroadcastAdminAdapter(broadcastLists,getContext(),getFragmentManager());
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
          /*  @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params= new HashMap<>();
                params.put("user_id", String.valueOf(adminFrag.id));
                return params;
            }*/
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void loadCandidateRecyclerViewData() {
      /*  StringRequest stringRequest =new StringRequest(Request.Method.POST,
                URL.DISPLAY_CANDIDATES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("voting_table");
                            for(int i=0;i<array.length();i++){
                                JSONObject o = array.getJSONObject(i);*/
                                CandidateList item= new CandidateList(
                                        "employee_no1");
                                //candidateLists.add(item);
                        /*    }
                          // JSONObject o = array.getJSONObject(array.length()-1);
                          //  votNo.setText(o.getString("vote_id"));
                          //  txtV1.setText(o.getString("employee_no1"));
                          //  txtV2.setText(o.getString("employee_no2"));
                          //  candidateAdapter =new CandidateAdapter(candidateLists,getContext(),getFragmentManager());
                            candidateRecyclerView.setAdapter(candidateAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),R.string.sorry+"\n"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.insert_candidates, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insert_candidates:
                startActivity(new Intent(getActivity(),InsertCandidates_Activity.class));
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void submitVoting(){
    if(       !SpinnerV1.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_degree))
            &&!SpinnerV2.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_degree))
            &&SpinnerV1.getSelectedItem()!=SpinnerV2.getSelectedItem())
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, URL.SUBMIT_VOTING_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                SpinnerV1.setSelection(0);
                SpinnerV2.setSelection(0);

                LoginFragment.v_state =1;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<>();

                parameters.put("vote_id", votNo.getText().toString().trim());
                parameters.put("user_id", valueOf(LoginFragment.id).trim());
                parameters.put("degree_no1", SpinnerV1.getSelectedItem().toString().trim());
                parameters.put("degree_no2", SpinnerV2.getSelectedItem().toString().trim());

                return parameters;
            }
        };
        requestQueue.add(request);
    }else{
        if(      SpinnerV1.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_degree))){
            Toast.makeText(getContext(),R.string.select_degree_for_first,Toast.LENGTH_LONG).show();
        }else if(SpinnerV2.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_degree))){
            Toast.makeText(getContext(),R.string.select_degree_for_second,Toast.LENGTH_LONG).show();
        }else if( SpinnerV1.getSelectedItem()==SpinnerV2.getSelectedItem())
            Toast.makeText(getContext(),R.string.can_not_give_same_degree,Toast.LENGTH_LONG).show();
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void load_data_from_server(){
        xAxis1 = new ArrayList<>();
        yAxis = null;
        yValues = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                url.SHOW_CHART_URL,
                new Response.Listener() {
                    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Object response) {
                        Log.d("string", (String) response);
                        try {
                            JSONObject jsonObject = new JSONObject((String) response);
                            JSONArray array = jsonObject.getJSONArray("chart_table");

                            JSONObject jsonobject = array.getJSONObject(0);

                            String name1 = txtV1.getText().toString();
                            String name2 = txtV2.getText().toString();

                            int score1 = jsonobject.getInt("SUM(d.degree_no1)");
                            int score2 = jsonobject.getInt("SUM(d.degree_no2)");

                            xAxis1.add(name1.substring(0, name1.indexOf(" ")));
                            xAxis1.add(name2.substring(0, name2.indexOf(" ")));

                            values = new BarEntry(score1,0);
                            yValues.add(values);
                            values = new BarEntry(score2,1);
                            yValues.add(values);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BarDataSet barDataSet1 = new BarDataSet(yValues,getResources().getString(R.string.degrees));
                        barDataSet1.setColor(Color.rgb(0,204,204));

                        yAxis = new ArrayList<>();
                        yAxis.add(barDataSet1);
                        String names[]= (String[]) xAxis1.toArray(new String[xAxis1.size()]);
                        BarData data = new BarData(names, yAxis);
                        chart.setData(data);
                        chart.setDescription(getResources().getString(R.string.vote));
                        chart.animateXY(2000, 2000);
                        chart.invalidate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error != null){
                            Toast.makeText(getContext(), R.string.sorry, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showCandidates(){
        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                URL.DISPLAY_CANDIDATES_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("voting_table");

                            JSONObject o = array.getJSONObject(array.length()-1);
                            votNo.setText(o.getString("vote_id"));
                            txtV1.setText(o.getString("employee_no1"));
                            txtV2.setText(o.getString("employee_no2"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),R.string.sorry+"\n"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void voters_counter(){
    StringRequest stringRequest =new StringRequest(Request.Method.POST,
            URL.VOTERS_COUNTER_URL,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {
                    try {
                        JSONObject jsonObject= new JSONObject(s);
                        JSONArray array = jsonObject.getJSONArray("counter");

                        JSONObject o = array.getJSONObject(0);
                        voters_counter.setText(o.getString("NumberOfVoters"));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),R.string.sorry+"\n"+error.toString(),Toast.LENGTH_LONG).show();
                }
            });
    RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
    requestQueue.add(stringRequest);
}
}
