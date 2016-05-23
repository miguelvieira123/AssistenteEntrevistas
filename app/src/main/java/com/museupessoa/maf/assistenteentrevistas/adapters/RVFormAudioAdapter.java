package com.museupessoa.maf.assistenteentrevistas.adapters;


import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.R;

import java.util.List;

public class RVFormAudioAdapter extends   RecyclerView.Adapter<RVFormAudioAdapter.RVFormAudioViewHolder> {
    private OnItemClickListener listener;

    public static class RVFormAudioViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public CardView cv;
        TextView formName;
        private final  String TAG="AssistenteEntrevistas";
        private OnItemClickListener listener;
        private int position;


        RVFormAudioViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.CV_audioForm);
            formName = (TextView)itemView.findViewById(R.id.audioFormName);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onItemClick(v,position,cv);
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

    List<String> formNames;
    public RVFormAudioAdapter(List<String> formNames){
        this.formNames =  formNames;
    }

    public  void RVUpdateListAdapter(List<String> formNames){
        this.formNames = formNames;
    }


    @Override
    public RVFormAudioAdapter.RVFormAudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_audioform, parent, false);
        RVFormAudioViewHolder pvh = new RVFormAudioViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RVFormAudioAdapter.RVFormAudioViewHolder holder, int position) {
        holder.formName.setText(formNames.get(position));
        holder.setOnItemClickListener(listener, position);

    }

    @Override
    public int getItemCount() {
        return formNames.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        public  void onItemClick(View v, int position,CardView cv );
    }
}
