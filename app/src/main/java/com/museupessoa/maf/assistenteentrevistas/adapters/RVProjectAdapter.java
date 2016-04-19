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
    private OnItemClickListener listener;
    public static class ProjectViewHolder extends RecyclerView.ViewHolder  implements View.OnLongClickListener {

        CardView cv;
        TextView projectName;
        private final  String TAG="AssistenteEntrevistas";
        private OnItemClickListener listener;
        private int position;

        ProjectViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.CV_Project);
            projectName = (TextView)itemView.findViewById(R.id.ProjectName);
            cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listener!=null)
                        listener.onItemClick(v,position);
                    return  false;
                }
            });

        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
        public void setOnItemClickListener(OnItemClickListener listener,int position ){
            this.listener = listener;
            this.position = position;
        }
    }

    List<ProjectUnit> projects;
    public RVProjectAdapter(List<ProjectUnit> projects){
        this.projects =  projects;
    }

    public  void RVUpdateListAdapter(List<ProjectUnit> projectUnits){
        this.projects = projectUnits;
    }

    @Override
    public RVProjectAdapter.ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cv_project_item, parent, false);
        ProjectViewHolder pvh = new ProjectViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(RVProjectAdapter.ProjectViewHolder holder, int position) {
        holder.projectName.setText( projects.get(position).name);
        holder.setOnItemClickListener(listener, position);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        public  void onItemClick(View v, int position);
    }
}
