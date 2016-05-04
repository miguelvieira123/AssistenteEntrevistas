package com.museupessoa.maf.assistenteentrevistas.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.units.InterviewUnit;

import java.util.List;


public  class RVInterviewAdapter extends   RecyclerView.Adapter<RVInterviewAdapter.InterviewViewHolder> {
    private OnItemClickListener listener;
    public static class InterviewViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        CardView cv;
        TextView interviewName;
        ImageView interviewPic;
        private OnItemClickListener listener;
        private int position;

        InterviewViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.CV_Interview);
            itemView = cv;
            interviewName = (TextView)itemView.findViewById(R.id.InterviewName);
            interviewPic = (ImageView) itemView.findViewById(R.id.foto_entrevista);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onItemClick(v,position);
                }
            });
        }

        @Override
        public void onClick(View v) {}
        public void setOnItemClickListener(OnItemClickListener listener,int position ){
            this.listener = listener;
            this.position = position;
        }
    }


    List<InterviewUnit> interviews;

    public RVInterviewAdapter(List<InterviewUnit> interviews){
        this.interviews =  interviews;
    }
    @Override
    public RVInterviewAdapter.InterviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_interview_item, parent, false);
        InterviewViewHolder ivh = new InterviewViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(final RVInterviewAdapter.InterviewViewHolder holder, final int position) {
        holder.interviewName.setText(interviews.get(position).name);
        holder.interviewPic.setImageBitmap(interviews.get(position).foto);
        holder.setOnItemClickListener(listener, position);
    }

    @Override
    public int getItemCount() {
        return interviews.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        public  void onItemClick(View v, int position);
    }


}
