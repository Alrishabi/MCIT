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
public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.ViewHolder> {

    private ArrayList<CandidateList> candidateLists;
    private Context context;
    private FragmentManager fm;

    public CandidateAdapter(ArrayList<CandidateList> listItems, Context context,FragmentManager fragmentManager) {
        this.candidateLists = listItems;
        this.context = context;
        this.fm=fragmentManager;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textCandiName;
        RelativeLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            textCandiName=(TextView)itemView.findViewById(R.id.candidateName);
            linearLayout=(RelativeLayout) itemView.findViewById(R.id.linearB);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.candidates_modle,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(CandidateAdapter.ViewHolder holder, int position) {
        final CandidateList listItem= candidateLists.get(position);
        holder.textCandiName.setText(listItem.getCandiName());

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
        return candidateLists.size();
    }

}
