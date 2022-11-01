package com.example.musab.mcit.mRecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.musab.mcit.Dialogs.Edit_Dialog_Fragment;
import com.example.musab.mcit.Dialogs.Show_Dialog_Fragment;
import com.example.musab.mcit.R;
import com.example.musab.mcit.URLs.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by musab on 8/12/2017.
 */

public class BroadcastAdminAdapter extends RecyclerView.Adapter<BroadcastAdminAdapter.AdminViewHolder> {
    URL url;
    private ArrayList<BroadcastList> broadcastLists;
    private Context context;
    private FragmentManager fm;


    public BroadcastAdminAdapter(ArrayList<BroadcastList> listItems, Context context, FragmentManager fm) {
        this.broadcastLists = listItems;
        this.context = context;
        this.fm=fm;
    }
       public class AdminViewHolder extends RecyclerView.ViewHolder {

           public TextView textViewHead,textViewDesc,textViewDate;
           public ImageView deleteBtn,editBtn;
           public RelativeLayout linearLayout;

           public AdminViewHolder(View itemView) {
              super(itemView);
               textViewHead=(TextView)itemView.findViewById(R.id.AdmintextViewHead);
               textViewDesc=(TextView)itemView.findViewById(R.id.AdmintextViewDes);
               textViewDate=(TextView)itemView.findViewById(R.id.AdmintextViewDate);
               deleteBtn=(ImageView) itemView.findViewById(R.id.x_btn);
               editBtn=(ImageView) itemView.findViewById(R.id.edit_btn);
               linearLayout=(RelativeLayout)itemView.findViewById(R.id.linearL);
           }

       }
    @Override
    public AdminViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_list_item,parent,false);
        return new AdminViewHolder(v);
    }
    @Override
    public void onBindViewHolder(BroadcastAdminAdapter.AdminViewHolder holder, final int position) {
        final BroadcastList listItem= broadcastLists.get(position);
        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText("تعميم "+listItem.getType());
        holder.textViewDate.setText(listItem.getDate());
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setIcon(R.drawable.ic_action_delete)
                        .setTitle(R.string.attention_dialog_title)
                        .setMessage(context.getResources().getString(R.string.delete_qution))
                        .setPositiveButton(context.getResources().getString(R.string.delete_hint),new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletItemFromDB(position);
                            }
                        }).setNegativeButton(R.string.cancel_button,null)
                        .show();
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               openDialogFragmentShow(listItem.getHead(),
                                         listItem.getType(),
                                         listItem.getDesc(),
                                         listItem.getDate());

            }
        });
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogFragment(listItem.getHead(),
                        listItem.getBroadcast_id(),
                        listItem.getType(),
                        listItem.getDesc(),
                        listItem.getDate());


            }
        });
    }

    public void openDialogFragment(String title,
                                   int id,
                                   String type,
                                   String content,
                                   String date){
        //Bundle
        Bundle bundle=new Bundle();
        bundle.putString("TITLE_KEY",title);
        bundle.putInt("ID_KEY",id);
        bundle.putString("TYPE_KEY",type);
        bundle.putString("CONTENT_KEY",content);
        bundle.putString("DATE_KEY",date);

        Edit_Dialog_Fragment edit_dialog_fragment = new Edit_Dialog_Fragment();
        edit_dialog_fragment.setArguments(bundle);
        edit_dialog_fragment.show(fm,"eTag");
    }
    public void openDialogFragmentShow(String title,String type,String content,String date){
        //Bundle
        Bundle bundle=new Bundle();
        bundle.putString("TITLE_KEY",title);
        bundle.putString("TYPE_KEY",type);
        bundle.putString("CONTENT_KEY",content);
        bundle.putString("DATE_KEY",date);

        Show_Dialog_Fragment show_dialog_fragment = new Show_Dialog_Fragment();
        show_dialog_fragment.setArguments(bundle);

        show_dialog_fragment.show(fm,"mTag");
    }
    private void deletItemFromDB(final int position) {
        // String url="http://192.168.208.2/mcit_php/deleteBroadcast.php";
        // String url="http://192.168.43.147/mcit_php/deleteBroadcast.php";
        final BroadcastList listItem= broadcastLists.get(position);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage( context.getResources().getString(R.string.deleting_progress));
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.DELETE_BROADCAST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                if(response.trim().equals("Record deleted successfully")){
                    Toast.makeText(context,R.string.delete_successfully, Toast.LENGTH_SHORT).show();
                    broadcastLists.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,broadcastLists.size());
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(context,response.trim().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        } ,new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,R.string.sorry+"\n" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params= new HashMap<>();
                params.put("broadcast_id", String.valueOf(listItem.getBroadcast_id()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    @Override
    public int getItemCount() {
        return broadcastLists.size();
    }
    public void setFilter(ArrayList<BroadcastList> newList) {
        broadcastLists = new ArrayList<>();
        broadcastLists.addAll(newList );
        notifyDataSetChanged();
    }
}
