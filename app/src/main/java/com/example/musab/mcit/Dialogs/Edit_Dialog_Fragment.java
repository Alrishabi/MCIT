package com.example.musab.mcit.Dialogs;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.musab.mcit.R.array.Broadcast_type;

/**
 * Created by musab on 8/21/2017.
 */

public class Edit_Dialog_Fragment extends AppCompatDialogFragment {
    URL url;
    EditText lable, content, dateP;
     Spinner typeSpinner;
     Button saveB, cancelB,datebtn;
    final Calendar cel= Calendar.getInstance();

    DatePickerDialog.OnDateSetListener ddate =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //TO
            cel.set( year,monthOfYear,dayOfMonth);
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String myFormat=sdf.format(cel.getTime());
            dateP.setText(myFormat);        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(getResources().getString(R.string.edit_broadcast));
        View rootView = inflater.inflate(R.layout.edit_dialog, container, false);

        lable = (EditText) rootView.findViewById(R.id.editTextLable);
        typeSpinner = (Spinner) rootView.findViewById(R.id.spinnnype);
        content = (EditText) rootView.findViewById(R.id.editTextContent);
        dateP = (EditText) rootView.findViewById(R.id.editTextDate);
        dateP.setEnabled(false);
        datebtn = (Button) rootView.findViewById(R.id.dateBtn);
        saveB = (Button) rootView.findViewById(R.id.save);
        cancelB = (Button) rootView.findViewById(R.id.c);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getContext(),
                Broadcast_type, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        //Receive DATA
        final String title = this.getArguments().getString("TITLE_KEY");
        final String type = this.getArguments().getString("TYPE_KEY");
        final String bContent = this.getArguments().getString("CONTENT_KEY");
        final String date = this.getArguments().getString("DATE_KEY");
        final String id = this.getArguments().getString("ID_KEY");

      typeSpinner.setSelection(typeAdapter.getPosition(type));

        //BINA DATA
       lable.setText(title);
        content.setText(bContent);
        dateP.setText(date);
        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),ddate,cel.get(Calendar.YEAR)
                        ,cel.get(Calendar.MONTH),cel.get(Calendar.DAY_OF_MONTH)).show();            }
        });
        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               upDateMethod(id);
            }
        });
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return rootView;
    }

    private void upDateMethod(final String i ) {
        if(lable.length() > 0  && !typeSpinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_broadcast))
                && content.length() > 0 && dateP.length()>0) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(getResources().getString(R.string.update_progress_message));
            progressDialog.show();

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.UPDATE_BROADCAST_URL,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.trim().equals("Error updating record:")) {
                        Toast.makeText(getContext(),R.string.sorry+"\n"+getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(getContext(), response.trim(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        getDialog().dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), getResources().getString(R.string.sorry)+"\n"
                            +getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("broadcast_id",String.valueOf(i).trim());
                    params.put("broadcast_type",typeSpinner.getSelectedItem().toString().trim());
                    params.put("broadcast_title", lable.getText().toString().trim());
                    params.put("broadcast_content",content.getText().toString().trim());
                    params.put("broadcast_date",dateP.getText().toString().trim());

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            if(lable.length() == 0){
                lable.setError(getResources().getString(R.string.type_broadcast_lable));
            }else if(typeSpinner.getSelectedItem().toString().equalsIgnoreCase(getResources().getString(R.string.ignore_case_for_broadcast))){
                Toast.makeText(getContext(),R.string.choose_broadcast_type, Toast.LENGTH_SHORT).show();
            }else if(content.length() == 0){
                content.setError(getResources().getString(R.string.type_broadcast_contanet));
            }
        }
    }
}
