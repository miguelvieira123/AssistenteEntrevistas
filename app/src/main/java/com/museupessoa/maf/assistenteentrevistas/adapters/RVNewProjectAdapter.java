package com.museupessoa.maf.assistenteentrevistas.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.List;
public class RVNewProjectAdapter extends RecyclerView.Adapter<RVNewProjectAdapter.NewProjetViewHolder> {
    private OnItemClickListener listener;
    
    public static class NewProjetViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        CardView cv;
        TextView itemName;
        TextView itemNum;
        private final  String TAG="AssistenteEntrevistas";
        private OnItemClickListener listener;
        private int position;

        NewProjetViewHolder(View itemView, final OnItemClickListener listener){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.NewProjectCV);
            itemName = (TextView)itemView.findViewById(R.id.NewProjectItem);
            itemNum = (TextView)itemView.findViewById(R.id.NewProjectNum);
            cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener!=null)
                        listener.onItemClick(v,position);
                    return  false;
                }
            });
        }
        public void setOnItemClickListener(OnItemClickListener listener,int position ){
            this.listener = listener;
            this.position = position;
        }


        @Override
        public boolean onLongClick(View v) {
            return false;
        }

    }


    List<String> items;
    public RVNewProjectAdapter(List<String> items){
        this.items =  items;
    }
    public  void RVUpdateListAdapter(List<String> items){
        this.items =  items;
    }
    @Override
    public RVNewProjectAdapter.NewProjetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newproject_item_cv, parent, false);
        NewProjetViewHolder pvh = new NewProjetViewHolder(v,listener);
        return pvh;
    }
    @Override
    public void onBindViewHolder(RVNewProjectAdapter.NewProjetViewHolder holder, int position) {
        holder.itemNum.setText(Integer.toString(position+1));
        holder.itemName.setText(items.get(position));
        holder.setOnItemClickListener(listener,position);
    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        public  void onItemClick(View v, int position);
    }

}

