package com.museupessoa.maf.assistenteentrevistas.adapters;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.editInterviewPersonForm.AudioForm;

import java.io.File;
import java.util.List;

public class RVFormAudioAdapter extends   RecyclerView.Adapter<RVFormAudioAdapter.RVFormAudioViewHolder> {
    private OnItemClickListener listener;
    List<String> formNames;
    String interview_path;
    public static class RVFormAudioViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public CardView cv;
        TextView formName;
        ImageView playIcon;
        RelativeLayout l;
        private final  String TAG="AssistenteEntrevistas";
        private OnItemClickListener listener;
        private int position;
        MediaPlayer mediaPlayer= new MediaPlayer();


        RVFormAudioViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.CV_audioForm);
            formName = (TextView)itemView.findViewById(R.id.audioFormName);
            playIcon = (ImageView)itemView.findViewById(R.id.audioFormPlay);
            l=(RelativeLayout)itemView.findViewById(R.id.audioFormPlayLayout);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onItemClick(v,position,cv);
                }
            });
            l.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            playIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AudioForm.startPlay(formName.getText().toString());
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


    public RVFormAudioAdapter(List<String> formNames, String interview_path){
        this.formNames =  formNames;
        this.interview_path = interview_path;
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
        File f = new File(interview_path+"/Audio/Form/"+formNames.get(position)+".mp4");
        if(f.exists()){
            Log.e("e",interview_path+"/Audio/Form/"+formNames.get(position)+".mp4");
            holder.playIcon.setBackgroundResource(R.drawable.amarok);
        }
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
