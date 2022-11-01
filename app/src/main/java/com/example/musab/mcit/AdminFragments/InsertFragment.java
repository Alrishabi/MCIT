package com.example.musab.mcit.AdminFragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

/**
 * Created by musab on 7/27/2017.
 */

public class InsertFragment extends Fragment {
    URL url;
    private EditText lable,content,dateP;
    Spinner typeSpinner;
    final Calendar cel= Calendar.getInstance();
    private Button datePicker;
    public LoginFragment adminFrag;
    private LinearLayout mRoot ;
    Button insert_bc;


    DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //TO
            cel.set( year,monthOfYear,dayOfMonth);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String myFormat=sdf.format(cel.getTime());
            dateP.setText(myFormat);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.insert_fragment,container,false);

        setHasOptionsMenu(true);
        mRoot=(LinearLayout)rootView.findViewById(R.id.myroot);
        typeSpinner=(Spinner) rootView.findViewById(R.id.spinnerType);
        lable=(EditText)rootView.findViewById(R.id.editTextLable);
        content=(EditText)rootView.findViewById(R.id.editTextContent);
        dateP=(EditText)rootView.findViewById(R.id.editTextDate);

        datePicker=(Button) rootView.findViewById(R.id.dateBtn);
        insert_bc=(Button) rootView.findViewById(R.id.insert_broadcast);

        ArrayAdapter<CharSequence> typeAdapter ;
                 typeAdapter = ArrayAdapter.createFromResource(getContext(),R.array.Broadcast_type, android.R.layout.simple_spinner_item);
                typeAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        typeSpinner.setAdapter(typeAdapter);


        dateP.setEnabled(false);

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext() , date,cel.get(Calendar.YEAR)
                        ,cel.get(Calendar.MONTH),cel.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        insert_bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMetod();
            }
        });

        return rootView;
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void insertMetod(){
        if (lable.length() > 0 && !typeSpinner.getSelectedItem().toString().equalsIgnoreCase(getResources()
                .getString(R.string.ignore_case_for_broadcast))
                && content.length() > 0 && dateP.length()>0 ){
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest request = new StringRequest(Request.Method.POST, url.INSERT_BROADCAST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getContext(),R.string.insert_seccfual, Toast.LENGTH_SHORT).show();
                    lable.setText(null);
                    content.setText(null);
                    dateP.  setText(null);
                    typeSpinner.setSelection(0);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(),R.string.insert_failed, Toast.LENGTH_SHORT).show();
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
