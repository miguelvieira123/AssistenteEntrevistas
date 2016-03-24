package com.museupessoa.maf.assistenteentrevistas.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.units.InterviewUnit;

import java.util.List;


public  class RVInterviewAdapter extends   RecyclerView.Adapter<RVInterviewAdapter.InterviewViewHolder> {

    public static class InterviewViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView interviewName;

        InterviewViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.CV_Interview);
            interviewName = (TextView)itemView.findViewById(R.id.InterviewName);

        }
    }
    List<InterviewUnit> interviews;

    public RVInterviewAdapter(List<InterviewUnit> interviews){
        this.interviews =  interviews;
    }
    @Override
    public RVInterviewAdapter.InterviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.interview_item_cv, parent, false);
        InterviewViewHolder ivh = new InterviewViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(RVInterviewAdapter.InterviewViewHolder holder, int position) {
        holder.interviewName.setText( interviews.get(position).name);
    }

    @Override
    public int getItemCount() {
        return interviews.size();
    }
}
