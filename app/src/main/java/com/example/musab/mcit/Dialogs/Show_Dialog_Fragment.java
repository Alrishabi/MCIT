package com.example.musab.mcit.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musab.mcit.R;

/**
 * Created by MUSAB on 11/28/2017.
 */

public class Show_Dialog_Fragment extends AppCompatDialogFragment {
    private Button  cancelB;
    private TextView tit,typ,dat,con;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.show_dialog_fragment, container, false);

        tit = (TextView) rootView.findViewById(R.id.txtTitel);
        typ = (TextView) rootView.findViewById(R.id.txtType);
        dat = (TextView) rootView.findViewById(R.id.txtDate);
        con = (TextView) rootView.findViewById(R.id.txtCont);

        cancelB = (Button) rootView.findViewById(R.id.btnOk);

        //Receive DATA
        final String title = this.getArguments().getString("TITLE_KEY");
        final String type = this.getArguments().getString("TYPE_KEY");
        final String content = this.getArguments().getString("CONTENT_KEY");
        final String date = this.getArguments().getString("DATE_KEY");


        tit.setText(title);
        typ.setText("تعميم"+" "+type);
        dat.setText(date);
        con.setText(content);



        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

            }
        });

        return rootView;
    }
}
