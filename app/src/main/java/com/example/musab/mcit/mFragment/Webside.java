package com.example.musab.mcit.mFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.musab.mcit.R;


public class Webside extends Fragment {
    private static WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView= inflater.inflate(R.layout.webside_fragment, container, false);
        webView=(WebView) rootView.findViewById(R.id.mcitwebside);
        openURL();
        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(R.string.website_title);
    }
   public void openURL(){
       final ProgressDialog progressDialog = new ProgressDialog(getActivity());
       progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));
       progressDialog.show();

       Handler handler = new Handler();
       handler.postDelayed(new Runnable() {
           public void run() {
               progressDialog.dismiss();
           }
       }, 20000); // 10000 milliseconds delay
       //Enable JavaScript
       webView.getSettings().setJavaScriptEnabled(true);
       webView.setFocusable(true);
       webView.setFocusableInTouchMode(true);
       //SET RENDER PRIORTY TO HIGH
       webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
       webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
       webView.getSettings().setDomStorageEnabled(true);
       webView.getSettings().setDatabaseEnabled(true);
       webView.getSettings().setAppCacheEnabled(true);
       webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
       //Load Url
       webView.loadUrl("http://mcit.gov.sd/index.php/ar");
       webView.setWebViewClient(new WebViewClient());
   }
}
