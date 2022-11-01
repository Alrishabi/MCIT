package com.example.musab.mcit.mRecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.musab.mcit.Dialogs.Edit_Dialog_Fragment;
import com.example.musab.mcit.Dialogs.Show_Dialog_Fragment;
import com.example.musab.mcit.R;
import java.util.ArrayList;

/**
 * Created by musab on 7/16/2017.
 */
public class BroadcastAdapter extends RecyclerView.Adapter<BroadcastAdapter.ViewHolder> {

    private ArrayList<BroadcastList> broadcastLists;
    private Context context;
    private FragmentManager fm;

    public BroadcastAdapter(ArrayList<BroadcastList> listItems, Context context,FragmentManager fragmentManager) {
        this.broadcastLists = listItems;
        this.context = context;
        this.fm=fragmentManager;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewHead,textViewDesc,textViewDate;
        RelativeLayout linearLayout;
          public ViewHolder(View itemView) {
            super(itemView);
            textViewHead=(TextView)itemView.findViewById(R.id.textViewHead);
            textViewDesc=(TextView)itemView.findViewById(R.id.textViewDes);
            textViewDate=(TextView)itemView.findViewById(R.id.textViewDate);
            linearLayout=(RelativeLayout) itemView.findViewById(R.id.linearB);
          }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(BroadcastAdapter.ViewHolder holder, int position) {
        final BroadcastList listItem= broadcastLists.get(position);
        holder.textViewHead.setText(listItem.getHead());
        holder.textViewDesc.setText("تعميم "+listItem.getType());
        holder.textViewDate.setText(listItem.getDate());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogFragmentShow(listItem.getHead(),
                                       listItem.getType(),
                                       listItem.getDesc(),
                                       listItem.getDate());
            }
        });
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

    @Override
    public int getItemCount() {
        return broadcastLists.size();
    }
    public void setFilter(ArrayList<BroadcastList> newList)
    {
        broadcastLists = new ArrayList<>();
        broadcastLists.addAll(newList );
        notifyDataSetChanged();
    }
}
