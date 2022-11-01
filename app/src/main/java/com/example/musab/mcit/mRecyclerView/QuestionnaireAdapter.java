package com.example.musab.mcit.mRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musab.mcit.Activities.Browser;
import com.example.musab.mcit.R;
import com.example.musab.mcit.mFragment.LoginFragment;

import java.util.ArrayList;

/**
 * Created by musab on 7/16/2017.
 */
public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.ViewHolder> {

    private ArrayList<QuestionnaireList> questionnaireLists;
    private Context context;
    private FragmentManager fm;
    LoginFragment loginFragment;

    public QuestionnaireAdapter(ArrayList<QuestionnaireList> questionnaireLists, Context context, FragmentManager fragmentManager) {
        this.questionnaireLists = questionnaireLists;
        this.context = context;
        this.fm=fragmentManager;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.questionnair_list_item,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(QuestionnaireAdapter.ViewHolder holder, int position) {
        final QuestionnaireList questionnaireList= questionnaireLists.get(position);
        holder.textViewHead.setText(questionnaireList.getTitle());
        holder.textViewLink.setText(R.string.press_here);
        holder.textViewDate.setText(questionnaireList.getDate());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginFragment.qu ==0) {
                    open_browser(questionnaireList.getLink());
                }else{
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.logout)
                            .setTitle(R.string.attention_dialog_title)
                            .setMessage(R.string.you_have_already_submit_qu)
                            .setNegativeButton(R.string.yes, null)
                            .show();
                }
            }
        });
    }

    public void open_browser(String link){
        //Bundle
        Intent intent=new Intent(context,Browser.class);
        intent.putExtra("LINK_KEY",link);
        context.startActivity(intent);
    }
////////////////////////////////////////////////////////////////////////////////////////////////
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewHead,textViewLink,textViewDate;
        LinearLayout linearLayout;
          public ViewHolder(View itemView) {
            super(itemView);
            textViewHead=(TextView)itemView.findViewById(R.id.textViewHead);
            textViewLink=(TextView)itemView.findViewById(R.id.textViewLink);
            textViewDate=(TextView)itemView.findViewById(R.id.textViewDate);
            linearLayout=(LinearLayout) itemView.findViewById(R.id.linearQ);

          }

}/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
     public int getItemCount() {
         return questionnaireLists.size();
     }
    public void setFilter(ArrayList<QuestionnaireList> newList)
    {
        questionnaireLists = new ArrayList<>();
        questionnaireLists.addAll(newList );
        notifyDataSetChanged();
    }
}
