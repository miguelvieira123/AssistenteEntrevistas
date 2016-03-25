package com.museupessoa.maf.assistenteentrevistas.adapters;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.List;

public class RVNewProjectAdapter extends RecyclerView.Adapter<RVNewProjectAdapter.NewProjetViewHolder> {
    public static class NewProjetViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView itemName;
        private final  String TAG="AssistenteEntrevistas";

        NewProjetViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.NewProjectCV);
            itemName = (TextView)itemView.findViewById(R.id.NewProjectItem);
            cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Log.d(TAG,"1111");
                    return false;
                }
            });
        }
    }

    List<String> items;
    public RVNewProjectAdapter(List<String> items){
        this.items =  items;
    }
    public void RVUpdateListAdapter(List<String> items){
        this.items =  items;
    }
    @Override
    public RVNewProjectAdapter.NewProjetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.newproject_item_cv, parent, false);
        NewProjetViewHolder pvh = new NewProjetViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(RVNewProjectAdapter.NewProjetViewHolder holder, int position) {
        holder.itemName.setText( items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();

    }
}
