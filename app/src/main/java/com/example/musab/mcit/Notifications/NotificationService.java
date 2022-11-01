package com.example.musab.mcit.Notifications;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musab.mcit.R;
import com.example.musab.mcit.URLs.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
/**
 * Created by MUSAB on 9/9/2017.
 */
public class NotificationService extends IntentService {
    URL url;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    static int broadcastID=0;
    static int voteNo=0;
    public static boolean serviceIsRun =false;

    public NotificationService() {
        super("MyWebRequstService");
    }
    ////////////////////////////////////////////////////////////
    @Override
    protected void onHandleIntent(Intent workIntent) {
        //  Toast.makeText(getApplicationContext(),"off",Toast.LENGTH_LONG).show();
        while (serviceIsRun) {
            //  Toast.makeText(getApplicationContext(),"Service is Runing",Toast.LENGTH_LONG).show();
            getNewestBroadcast();
            getNewestVote();
            try {
                Thread.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void getNewestBroadcast() {
        StringRequest stringRequest =new StringRequest(Request.Method.GET,
                url.BROADCAST_NOTIFICATION_URL+broadcastID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            String msgTitle = null;
                            String msgType = null;
                            String msgContent = null;
                            JSONObject jsonObject= new JSONObject(s);
                            JSONArray array = jsonObject.getJSONArray("broadcast_table");
                            for(int i=0;i<array.length();i++) {
                                JSONObject o = array.getJSONObject(i);
                                msgTitle = o.getString("broadcast_title") + "\n";
                                msgType = o.getString("broadcast_type") + "\n";
                                msgContent = o.getString("broadcast_content");
                                broadcastID = o.getInt("broadcast_id");

                                Intent intent = new Intent();
                                intent.setAction("com.example.Broadcast");
                                intent.putExtra("msgTitle", msgTitle);
                                intent.putExtra("msgType", msgType);
                                intent.putExtra("msgContent", msgContent);

                                sendBroadcast(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // Toast.makeText(getApplicationContext(),"NO INTERNET CONNECTION",Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplication());
        requestQueue.add(stringRequest);
    }
 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public void getNewestVote() {
     StringRequest stringRequest =new StringRequest(Request.Method.GET,
             url.VOTE_NOTIFICATION_URL+voteNo,
             new Response.Listener<String>() {
                 @Override
                 public void onResponse(String s) {
                     try {
                         String msgTitle = null;
                         String msgType = null;
                         String msgContent = null;
                         JSONObject jsonObject= new JSONObject(s);
                         JSONArray array = jsonObject.getJSONArray("candidates_table");
                         for(int i=0;i<array.length();i++) {
                             JSONObject o = array.getJSONObject(i);
                             msgTitle = getResources().getString(R.string.new_candidates_added)+"\n";
                             msgType = "Vote new" + "\n";
                             msgContent = o.getString("employee_no1")+"\n"+o.getString("employee_no2")+"\n"+o.getString("employee_no3");
                             voteNo = o.getInt("vote_id");

                             Intent intent = new Intent();
                             intent.setAction("com.example.Vote");
                             intent.putExtra("msgTitle", msgTitle);
                             intent.putExtra("msgType", msgType);
                             intent.putExtra("msgContent", msgContent);

                             sendBroadcast(intent);
                         }
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }
             },
             new Response.ErrorListener() {
                 @Override
                 public void onErrorResponse(VolleyError error) {
                     // Toast.makeText(getApplicationContext(),"NO INTERNET CONNECTION",Toast.LENGTH_LONG).show();
                 }
             });
     RequestQueue requestQueue= Volley.newRequestQueue(getApplication());
     requestQueue.add(stringRequest);
 }
}
