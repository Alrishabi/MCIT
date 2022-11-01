package com.example.musab.mcit.Activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import static java.lang.String.valueOf;

public class Browser extends AppCompatActivity {
    private WebView webView;
    private LoginFragment loginfragment;
    private URL url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browser_activity);
         webView = (WebView) findViewById(R.id.browse);
        String qustionlink = getIntent().getExtras().getString("LINK_KEY");
        openURL(qustionlink);
    }
    @Override
    public void onBackPressed() {
            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.logout)
                .setTitle(R.string.attintion_dialog_gu)
                .setMessage(R.string.did_u_comp_qu)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        loginfragment.qu=1;
                       update_qu_state();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
           // super.onBackPressed();
        }
    public void openURL(String link){
       final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));
        progressDialog.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                progressDialog.dismiss();
            }
        }, 15000); //15000 milliseconds delay
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
        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
    }
    private void update_qu_state(){
       final ProgressDialog progressDialog = new ProgressDialog(this);
       progressDialog.setMessage(getResources().getString(R.string.loading_progress_dialog_message));
       progressDialog.show();
       RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
       StringRequest request = new StringRequest(Request.Method.POST, url.CHANGE_QU_URL, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               progressDialog.dismiss();
               Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               progressDialog.dismiss();
               Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
           }
       }) {
           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> parameters = new HashMap<String, String>();

               parameters.put("user_id", valueOf(loginfragment.id).trim());
               return parameters;
           }
       };
       requestQueue.add(request);
   }
}
