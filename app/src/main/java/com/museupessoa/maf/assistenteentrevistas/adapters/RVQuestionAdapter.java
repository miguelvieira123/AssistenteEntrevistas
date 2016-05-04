package com.museupessoa.maf.assistenteentrevistas.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.units.QuestionUnit;

import java.util.List;

/**
 * Created by Miguel on 29/03/2016.
 */

public  class RVQuestionAdapter extends   RecyclerView.Adapter<RVQuestionAdapter.QuestionViewHolder> {
    private OnItemClickListener listener;
    public static class QuestionViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

        CardView cv;
        TextView question_row;

        private OnItemClickListener listener;
        private int position;

        QuestionViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.InterviewQuestionsCV);
            itemView = cv;
            question_row = (TextView)itemView.findViewById(R.id.InterviewQuestion);
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


    List<QuestionUnit> questions;

    public RVQuestionAdapter(List<QuestionUnit> questions){
        this.questions =  questions;
    }
    @Override
    public RVQuestionAdapter.QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_interview_questions, parent, false);
        QuestionViewHolder ivh = new QuestionViewHolder(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(final RVQuestionAdapter.QuestionViewHolder holder, final int position) {
        holder.question_row.setText(questions.get(position).question);
        holder.setOnItemClickListener(listener, position);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        public  void onItemClick(View v, int position);
    }
}