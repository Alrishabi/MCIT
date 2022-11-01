package com.example.musab.mcit.mFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.musab.mcit.Activities.AdminTabeActivity;
import com.example.musab.mcit.Activities.UserTabeActivity;
import com.example.musab.mcit.R;
import com.example.musab.mcit.URLs.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by musab on 7/18/2017.
 */

public class LoginFragment extends Fragment {
    private URL url;
    private Button LoginButton;
    private EditText editPass;
    private EditText editName;
    private TextView show,hide;
    private LinearLayout mRoot;

    SharedPreferences sh;
    SharedPreferences.Editor she;

    public static String n="",p="";
    public static int  id,v_state,qu;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.login_fragment, container , false);
        LoginButton=(Button) rootView.findViewById(R.id.btnLogin);
        editName=(EditText) rootView.findViewById(R.id.editTextName);
        editPass=(EditText) rootView.findViewById(R.id.editTextPass);
        mRoot=(LinearLayout)rootView.findViewById(R.id.email_login_form);
        show=(TextView) rootView.findViewById(R.id.show);
        hide=(TextView) rootView.findViewById(R.id.hide);

        sh=getActivity().getPreferences(Context.MODE_PRIVATE);
        she=sh.edit();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPass.setTransformationMethod(null);            }
        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { editPass.setTransformationMethod(new PasswordTransformationMethod()); }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             LoginFun();
            }
        });
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.login_title);
    }
////////////////////////////////////////////////////////////////////////////////////////////
    private void LoginFun(){
        if(editName.getText().length() > 0 && editPass.getText().length() > 0){
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getResources().getString(R.string.logging_progress_message));
            progressDialog.show();
            JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET,
                    url.LOGIN_ADMIN_URL+editName.getText()+"&user_password="+editPass.getText(),
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(0);
                                if(jsonObject.getString("code").equals("Login_Failed")){
                                    Toast.makeText(getActivity(),R.string.Login_failed, Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }else {
                                    if(jsonObject.getString("privileges").equals("admin")){
                                        startActivity(new Intent(getActivity(),AdminTabeActivity.class));
                                        Toast.makeText(getContext(),R.string.Login_Success,Toast.LENGTH_LONG).show();
                                        /////////////////////////////get admin info///////////////////////////////
                                        id =jsonObject.getInt("user_id");
                                        qu =jsonObject.getInt("questionnaire_state");
                                        v_state =jsonObject.getInt("voting_state");
                                        n =jsonObject.getString("user_name");
                                        p=jsonObject.getString("user_password");
                                    }else {
                                        startActivity(new Intent(getActivity(),UserTabeActivity.class));
                                        Toast.makeText(getContext(),R.string.Login_Success,Toast.LENGTH_LONG).show();
                                        /////////////////////////////get admin info///////////////////////////////
                                        id =jsonObject.getInt("user_id");
                                        qu =jsonObject.getInt("questionnaire_state");
                                        v_state =jsonObject.getInt("voting_state");
                                        n =jsonObject.getString("user_name");
                                        p=jsonObject.getString("user_password");
                                    }

                                    ///////////////////////////////////////////////////////////////////////////
                                    editName.setText(null);
                                    editPass.setText(null);
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Snackbar.make(mRoot, R.string.connection_failed, Snackbar.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
            requestQueue.add(stringRequest);
        }else {
            if(editName.getText().length() == 0){
                editName.setError(getResources().getString(R.string.Type_Admin_Name));
            }else {
                editPass.setError(getResources().getString(R.string.Type_Admin_Password));
            }
        }
    }
}

