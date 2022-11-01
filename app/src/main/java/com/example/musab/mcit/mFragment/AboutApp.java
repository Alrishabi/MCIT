package com.example.musab.mcit.mFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musab.mcit.R;

public class AboutApp extends Fragment implements View.OnClickListener{
    TextView dev;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.about_app, container, false);
        dev=(TextView)v.findViewById(R.id.developer);
        dev.setOnClickListener(this);

        return v;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.about_app_title);
    }
    @Override
    public void onClick(View v) {
        if(v==dev){
            try{
                Intent intent=new Intent(Intent.ACTION_SENDTO ,
                        Uri.fromParts("mailto","moosab.zoom245@gmail.com",null));
                startActivity(Intent.createChooser(intent,getResources().getString(R.string.choose_email)));
            }catch (Exception e){}
        }

    }
}
