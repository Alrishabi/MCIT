package com.example.musab.mcit.AdminFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;
/**
 * Created by musab on 7/27/2017.
 */

public class InfoAdminFragment extends android.support.v4.app.Fragment {
    URL url;
    EditText cPass,pass,rPass;
    TextView ID,name;
    Button save_chB;
    public LoginFragment adminFrag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.info_admin_fragment, container , false);

        setHasOptionsMenu(true);

        name= (TextView) rootView.findViewById(R.id.ed_userName);

        cPass= (EditText) rootView.findViewById(R.id.tx_c_pass);

        pass= (EditText) rootView.findViewById(R.id.ed_newPss);
        rPass= (EditText) rootView.findViewById(R.id.ed_reNewPss);
        ID= (TextView) rootView.findViewById(R.id.tx_Id);

        save_chB= (Button) rootView.findViewById(R.id.save_ch);

        name.setText(adminFrag.n);
        ID.setText(String.valueOf(adminFrag.id));
        save_chB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getContext(),adminFrag.p, Toast.LENGTH_SHORT).show();
               if(cPass.length()>0 && pass.length()>0 && rPass.length()>0) {
                    if(cPass.getText().toString().equals(adminFrag.p)){
                        if(pass.getText().toString().equals(rPass.getText().toString())){
                            upDateMethod(adminFrag.id);
                        // Toast.makeText(getContext(),"good", Toast.LENGTH_SHORT).show();
                          }else {

                           if(!adminFrag.p.equals(cPass.getText().toString())){
                                cPass.setError(getResources().getString(R.string.write_current_password));
                           }else {
                                rPass.setError(getResources().getString(R.string.passwords_do_not_match));
                           }
                        }
                   }else {
                       cPass.setError(getResources().getString(R.string.current_pass_is_incorrect));
                  }
                }else{
                   if(cPass.length()==0){cPass.setError(getResources().getString(R.string.write_current_password));}
                    else if(pass.length()==0){pass.setError(getResources().getString(R.string.retype_password));}
                    else if (rPass.length()==0){rPass.setError(getResources().getString(R.string.must_retype_password));}
                }
            }
        });

        return rootView;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void upDateMethod(final int i ) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getResources().getString(R.string.update_progress_message));
            progressDialog.show();

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.UPDATE_ADMIN_INFO_URL,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response){
                    if (response.trim().equals("Error updating record:")) {
                        Toast.makeText(getContext(),R.string.sorry+"\n"+getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(getContext(),getResources().getString(R.string.pass_changed_successfully),Toast.LENGTH_SHORT).show();
                        cPass.setText(null);
                        pass.setText(null);
                        adminFrag.p=rPass.getText().toString();
                        rPass.setText(null);
                        progressDialog.dismiss();
                    }
                 }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error){
                    Toast.makeText(getContext(), getResources().getString(R.string.sorry)+"\n"
                            +getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", String.valueOf(i).trim());
                    params.put("user_password",rPass.getText().toString().trim());

                    return params;
                }
            };
            requestQueue.add(stringRequest);
    }
}
