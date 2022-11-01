package com.example.musab.mcit.UserFragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musab.mcit.R;
import com.example.musab.mcit.URLs.URL;
import com.example.musab.mcit.mFragment.LoginFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import static java.lang.String.valueOf;


public class UserVoteFragment extends Fragment {
    public Spinner SpinnerV1,SpinnerV2;
    public TextView txtV1,txtV2,votNo;
    public SwipeRefreshLayout swipeRefreshLayout;

    public ImageView votingBtn;
    public URL url;
    public LoginFragment adminFrag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_vote_fragment, container, false);
        setHasOptionsMenu(true);

        SpinnerV1 = (Spinner) rootView.findViewById(R.id.spinnerV1);
        SpinnerV2 = (Spinner) rootView.findViewById(R.id.spinnerV2);


        votNo = (TextView) rootView.findViewById(R.id.votingNo);
        txtV1 = (TextView) rootView.findViewById(R.id.textVoting1);
        txtV2 = (TextView) rootView.findViewById(R.id.textVoting2);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.vote_swap);

        votingBtn = (ImageView) rootView.findViewById(R.id.buttonV);

        ArrayAdapter<CharSequence> degreeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Degree, android.R.layout.simple_spinner_item);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SpinnerV1.setAdapter(degreeAdapter);
        SpinnerV2.setAdapter(degreeAdapter);

        showCandidates();

        votingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adminFrag.v_state > 0) {
                    new AlertDialog.Builder(getContext())
                            .setIcon(R.drawable.ic_attention)
                            .setTitle(R.string.attention_dialog_title)
                            .setMessage(R.string.attention_message)
                            .setPositiveButton(R.string.dialog_quit_positive_button, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else {
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

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                showCandidates();

                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return rootView;
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showCandidates(){
        StringRequest stringRequest =new StringRequest(Request.Method.POST,
                url.DISPLAY_CANDIDATES_URL,
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
                }){
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void submitVoting(){
        if(!SpinnerV1.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_degree))
            &&!SpinnerV2.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_degree))
            &&SpinnerV1.getSelectedItem()!=SpinnerV2.getSelectedItem())
           {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));
            progressDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest request = new StringRequest(Request.Method.POST,url.SUBMIT_VOTING_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                    SpinnerV1.setSelection(0);
                    SpinnerV2.setSelection(0);

                    adminFrag.v_state=1;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();

                    parameters.put("vote_id",votNo.getText().toString().trim());
                    parameters.put("user_id",valueOf(adminFrag.id).trim());
                    parameters.put("degree_no1",SpinnerV1.getSelectedItem().toString().trim());
                    parameters.put("degree_no2",SpinnerV2.getSelectedItem().toString().trim());

                    return parameters;
                }
            };
            requestQueue.add(request);
        }else{
            if(  SpinnerV1.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_degree))){
                Toast.makeText(getContext(),R.string.select_degree_for_first,Toast.LENGTH_LONG).show();
            }else if(SpinnerV2.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_degree))){
                Toast.makeText(getContext(),R.string.select_degree_for_second,Toast.LENGTH_LONG).show();
            }else if( SpinnerV1.getSelectedItem()== SpinnerV2.getSelectedItem()
                    ){
                Toast.makeText(getContext(),R.string.can_not_give_same_degree,Toast.LENGTH_LONG).show();
            }
        }
    }
}
