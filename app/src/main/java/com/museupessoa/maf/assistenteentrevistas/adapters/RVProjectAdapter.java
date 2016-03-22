package com.museupessoa.maf.assistenteentrevistas.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.museupessoa.maf.assistenteentrevistas.R;
import com.museupessoa.maf.assistenteentrevistas.units.ProjectUnit;

import java.util.List;


public  class RVProjectAdapter extends   RecyclerView.Adapter<RVProjectAdapter.ProjectViewHolder> {

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView projectName;

        ProjectViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.CV_Project);
            projectName = (TextView)itemView.findViewById(R.id.ProjectName);

        }
    }
    List<ProjectUnit> projects;

    public RVProjectAdapter(List<ProjectUnit> projects){
        this.projects =  projects;
    }
    @Override
    public RVProjectAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item_cv, parent, false);
        ProjectViewHolder pvh = new ProjectViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RVProjectAdapter.ProjectViewHolder holder, int position) {
        holder.projectName.setText( projects.get(position).name);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}
