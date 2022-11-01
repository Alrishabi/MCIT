package com.example.musab.mcit.mFragment;

import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musab.mcit.R;

import java.util.List;

/**
 * Created by musab on 7/18/2017.
 */

public class ContactUsFrag extends android.support.v4.app.Fragment implements View.OnClickListener{
    ImageView fcBtn,infoBtn,twitBtn,youBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View rootView= inflater.inflate(R.layout.contactus_fragment, container , false);
        infoBtn= (ImageView) rootView.findViewById(R.id.info_inbox);
        fcBtn= (ImageView) rootView.findViewById(R.id.fc);
        twitBtn= (ImageView) rootView.findViewById(R.id.tw);
        youBtn= (ImageView) rootView.findViewById(R.id.you);
        //////////////////onclick//////////////////////////
        infoBtn.setOnClickListener(this);
        fcBtn.setOnClickListener(this);
        twitBtn.setOnClickListener(this);
        youBtn.setOnClickListener(this);

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.contact_us_title);
    }
    @Override
    public void onClick(View v) {
        if(v==infoBtn){
            try{
              Intent intent=new Intent(Intent.ACTION_SENDTO ,
                      Uri.fromParts("mailto","info@mcit.gov.sd",null));
              startActivity(Intent.createChooser(intent,getResources().getString(R.string.choose_email)));
            }catch (Exception e){}
        }else if(v==fcBtn){
            startActivity(getOpenFacebookIntent(getActivity()));
        }else if(v==twitBtn){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/")));
        }else if(v==youBtn){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://www.youtube.com/channel/UCarLn2Yh08If4H99ngWrsFQ"));
            startActivity(intent);
            }
         }
    public static Intent getOpenFacebookIntent(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/mscsudan"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mscsudan"));
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
