package com.example.musab.mcit.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.Locale;
import java.util.Map;

public class InsertBroadcastActivity extends AppCompatActivity {
    URL url;
    private EditText lable,content,dateP;
    Spinner typeSpinner;
    final Calendar cel= Calendar.getInstance();
    private Button datePicker;
    public LoginFragment adminFrag;
    private LinearLayout mRoot ;
    Button insert_bc;
    Calendar myCalendar = Calendar.getInstance();

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateP.setText(sdf.format(myCalendar.getTime()));
    }


    DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_fragment);

        final Activity activity = this;
        activity.setTitle(R.string.insert_broadcast);

        mRoot=(LinearLayout)findViewById(R.id.myroot);
        typeSpinner=(Spinner) findViewById(R.id.spinnerType);
        lable=(EditText)findViewById(R.id.editTextLable);
        content=(EditText)findViewById(R.id.editTextContent);
        dateP=(EditText)findViewById(R.id.editTextDate);
        datePicker=(Button)findViewById(R.id.dateBtn);
        insert_bc=(Button)findViewById(R.id.insert_broadcast);

        ArrayAdapter<CharSequence> typeAdapter ;
        typeAdapter = ArrayAdapter.createFromResource(getApplicationContext(),R.array.Broadcast_type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        typeSpinner.setAdapter(typeAdapter);

       dateP.setEnabled(false);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(InsertBroadcastActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        insert_bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMetod();
            }
        });
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void insertMetod(){
        if (lable.length() > 0 && !typeSpinner.getSelectedItem().toString().equalsIgnoreCase(getResources()
                .getString(R.string.ignore_case_for_broadcast))
                && content.length() > 0 && dateP.length()>0 ){
            StringRequest request = new StringRequest(Request.Method.POST,url.INSERT_BROADCAST_URL,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(),R.string.insert_seccfual, Toast.LENGTH_SHORT).show();
                    lable.setText(null);
                    content.setText(null);
                    dateP.  setText(null);
                    typeSpinner.setSelection(0);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                   // Toast.makeText(getApplicationContext(),R.string.insert_failed, Toast.LENGTH_SHORT).show();
                    Snackbar.make(mRoot, R.string.no_internet_connection, Snackbar.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();

                    parameters.put("broadcast_type",typeSpinner.getSelectedItem().toString().trim());
                    parameters.put("broadcast_title",lable.getText().toString().trim());
                    parameters.put("broadcast_content",content.getText().toString().trim());
                    parameters.put("broadcast_date",dateP.getText().toString().trim());
                    parameters.put("user_id", String.valueOf(adminFrag.id));

                    return parameters;}
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        }else {
            if(lable.length() == 0){
                lable.setError(getResources().getString(R.string.type_broadcast_lable));
            }else if(typeSpinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_broadcast))){
                Snackbar.make(mRoot, R.string.choose_broadcast_type, Snackbar.LENGTH_LONG).show();
            }else if(content.length() == 0){
                content.setError(getResources().getString(R.string.type_broadcast_contanet));
            }else {
                dateP.setError(getResources().getString(R.string.type_broadcast_date));
            }
        }
    }
}
