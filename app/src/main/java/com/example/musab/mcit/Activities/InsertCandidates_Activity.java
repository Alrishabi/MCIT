package com.example.musab.mcit.Activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InsertCandidates_Activity extends AppCompatActivity {
    private EditText cand1,cand2,cand3,v_date;
    private Button ok,cal;
    private URL url;
    private LoginFragment adminFrag;
    Calendar cel= Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //TO
            cel.set( year,monthOfYear,dayOfMonth);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String myFormat=sdf.format(cel.getTime());
            v_date.setText(myFormat);        }
    };

    @Override
  protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_candidates_activity);

        cand1=(EditText) findViewById(R.id.candidate1);
        cand2=(EditText) findViewById(R.id.candidate2);
        cand3=(EditText) findViewById(R.id.candidate3);
        v_date=(EditText) findViewById(R.id.voteDate);
        v_date.setEnabled(false);

        ok=(Button) findViewById(R.id.ok);
        cal=(Button) findViewById(R.id.calender);
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InsertCandidates_Activity.this , date,cel.get(Calendar.YEAR)
                        ,cel.get(Calendar.MONTH),cel.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCandidatesMethod();
            }
        });
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void insertCandidatesMethod() {
        if (cand1.length()>0 && cand2.length()>0 && cand3.length()>0 && v_date.length()>0 ){
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
            StringRequest request = new StringRequest(Request.Method.POST,url.INSERT_CANDIDATES_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplication(),response, Toast.LENGTH_SHORT).show();
                    cand1.setText(null);
                    cand2.setText(null);
                    cand3.setText(null);
                    v_date.setText(null);
                    adminFrag.v_state=0;
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplication(),R.string.connection_failed, Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("user_id", String.valueOf(adminFrag.id));
                    parameters.put("employee_no1",cand1.getText().toString().trim());
                    parameters.put("employee_no2",cand2.getText().toString().trim());
                    parameters.put("employee_no3",cand3.getText().toString().trim());
                    parameters.put("voting_date",v_date.getText().toString().trim());

                    return parameters;}
            };
            requestQueue.add(request);
        }else {
            if (cand1.length() == 0) {
                cand1.setError(getResources().getString(R.string.type_first_name));
            } else if (cand2.length()== 0) {
                cand2.setError(getResources().getString(R.string.type_second_name));
            } else if (cand3.length() == 0) {
                cand3.setError(getResources().getString(R.string.type_third_name));
            } else {
                v_date.setError(getResources().getString(R.string.type_date));
            }
        }
    }
}
